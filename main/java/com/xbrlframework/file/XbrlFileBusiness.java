/*
 * created by github.com/marcioAlexandre
 * Jun01, 2018
 * 
 */
 
package com.xbrlframework.file;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import org.w3c.dom.Document;

import com.xbrlframework.instance.Context;
import com.xbrlframework.instance.Dts;
import com.xbrlframework.instance.Fact;
import com.xbrlframework.instance.Footnote;
import com.xbrlframework.instance.Instance;
import com.xbrlframework.instance.PeriodForever;
import com.xbrlframework.instance.PeriodInstant;
import com.xbrlframework.instance.PeriodStartEnd;
import com.xbrlframework.instance.Prefix;
import com.xbrlframework.instance.Unit;

public class XbrlFileBusiness {
	
	private XbrlFile xfile;
	private XbrlFromURL fileFromURL;
	private XbrlFromMultipartFile fileFromMultipartFile;
	

	public void setFileAs(MultipartFile file){
		try {
			fileFromMultipartFile = new XbrlFromMultipartFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (fileFromMultipartFile != null) {
			xfile = fileFromMultipartFile.processing();
		}else {
			try {
				throw new Exception ("File cannot be empty!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	public void setFileAs(String address){
		fileFromURL = new XbrlFromURL(address);
		xfile = fileFromURL.processing();
	}
	
	public Document getFileAsDocument() {
		if (xfile != null) {
			return xfile.getDocumentFile();
		}
		return null;
	}
	
	private StringBuilder printFact(StringBuilder json, Fact fact, Instance instance) {
		json.append("    { \n"); //open fact
		if (fact.getId() != null && !fact.getId().isEmpty())
			json.append("      \"id\":\""+fact.getId()+"\", \n");
		if (fact.getValue() != null && !fact.getValue().isEmpty())
			json.append("      \"value\":\""+fact.getValue()+"\", \n");
		json.append("      \"aspect\": { \n");
		json.append("        \"xbrl:concept\":\""+fact.getName()+"\", \n");
		
		//context
		if (instance.getContextMap() != null) {
			xfile.setContextNumber(instance.getContextMap().size());
			// -- entity
			Map<String,Context> contextMap = instance.getContextMap();
			Optional<Context> optContext = contextMap.values().stream()
					.filter(c -> c.getId().toLowerCase().contains(fact.getContextRef().toLowerCase()))
					.findFirst();
			if (optContext.isPresent()) {
				Context context = optContext.get(); 
			
				json.append("        \"xbrl:entity\":\""+context.getEntity().getCid()+"\", \n");
				// -- period					
				if (context.getPeriod() instanceof PeriodInstant) {
					PeriodInstant period = (PeriodInstant) context.getPeriod();
					json.append("        \"xbrl:periodInstant\":\""+period.getInstantPeriodvalue()+"\"");
				}else if (context.getPeriod() instanceof PeriodStartEnd) {
					PeriodStartEnd period = (PeriodStartEnd) context.getPeriod();
					json.append("        \"xbrl:periodStart\":\""+period.getStartValue()+"\", \n");
					json.append("        \"xbrl:periodEnd\":\""+period.getEndValue()+"\"");
				}else {
					PeriodForever period = (PeriodForever) context.getPeriod();
					json.append("        \""+period.getValue()+"\"");
				}
			}
		}
		
		//unit
		if (instance.getUnitMap() != null) {
			xfile.setUnitNumber(instance.getUnitMap().size());
			Unit unit = instance.getUnitMap().get(fact.getUnitRef());
			if (unit != null) {
				json.append(",\n"); // ',' from period, expecting unit
				json.append("        \"xbrl:unit\":\""+unit.getValue()+"\" \n");
			}else {
				json.append("\n"); //not expecting unit
			}
		}else {
			json.append("\n"); //not expecting unit
		}
		
		json.append("      }"); // closed aspect
		
		//footnote
		if (instance.getFootnoteMap() != null) {
			xfile.setFootnoteNumber(instance.getFootnoteMap().size());
			Footnote footnote = instance.getFootnoteMap().get("#" + fact.getId());
			if (footnote != null) {
				json.append(",\n"); // expecting footnote
				json.append("      \"footnote\": { \n");
				json.append("        \"group\":\"" + footnote.getGroup() + "\", \n");
				json.append("        \"footnoteType\":\"" + footnote.getFootnoteType() + "\", \n");
				json.append("        \"footnote\":\"" + footnote.getFootnote() + "\", \n");
				json.append("        \"language\":\"" + footnote.getLanguage() + "\" \n");
				json.append("      } \n");
			}
		}
		json.append("\n    }\n"); //close fact
		return json;
	}
	
	private void printFacts(StringBuilder json, Instance instance){
		if (instance.getFactList() != null) {
			xfile.setFactNumber(instance.getFactList().size());
			json.append("	\"fact\": [\n");
			Queue<Fact> qfact = new ConcurrentLinkedQueue<>(
					Collections.unmodifiableList(instance.getFactList())
					);
			String comma = "";
			while (qfact.peek() != null) {
				Fact fact = qfact.poll();
				json.append(comma);
				comma = ",";
				this.printFact(json, fact, instance);
			}

			json.append("]\n");
		}
	}
	
	private void printPrefixes(StringBuilder json, Instance instance) {
		if (instance.getPrefixList() != null) {
			xfile.setPrefixNumber(instance.getPrefixList().size());
			Optional<Prefix> optXbrliPrefix = instance.getPrefixList().stream()
					.filter(p -> p.getName().equals("xbrli"))
					.findFirst();
			if (!optXbrliPrefix.isPresent()) {
				instance.getPrefixList().add(new Prefix("xbrli", "http://www.xbrl.org/2003/instance"));
			}
			
			Optional<Prefix> optXbrlPrefix = instance.getPrefixList().stream()
					.filter(p -> p.getName().equals("xbrl"))
					.findFirst();
			if (optXbrlPrefix.isPresent()) {
				instance.getPrefixList().remove(optXbrlPrefix.get());
			}
			
			instance.getPrefixList().add(new Prefix("xbrl","http://www.xbrl.org/CR/2017-05-02/oim"));
			json.append("  \"prefix\" : { \n");
			for (Prefix prefix: instance.getPrefixList()) {
				json.append("    \""+prefix.getName()+"\":\""+prefix.getValue()+"\", \n");
			}
			json.deleteCharAt(json.toString().trim().length()-1);  //delete last "," of object
			json.append("  }, \n");
		}
	}
	
	private void printDtses(StringBuilder json, Instance instance) {
		if (instance.getDtsList() != null) {
			xfile.setDtsNumber(instance.getDtsList().size());
			json.append("  \"dts\" : { \n");
			for (Dts dts: instance.getDtsList()) {
				json.append("    \""+dts.getName()+"\":\""+dts.getHref()+"\", \n");
			}
			json.deleteCharAt(json.toString().trim().length()-1); //delete last "," of object
			json.append("  }, \n");
		}
	}
	
	public String parseToJson(Instance instance) {
		// report
		StringBuilder json = new StringBuilder("{\n"); //root
		json.append("  \"report\" : {\n"); //start of report
		if (instance != null) {
			try {
				json.append("    \"documentType\":\""+instance.getDocumentType()+"\", \n");
				printPrefixes(json, instance);
				printDtses(json, instance);
				printFacts(json, instance);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		json.append("  } \n"); //end of report
		json.append("} \n"); //root

		return json.toString().trim();
	}
		
	private boolean isJSONValid(String test) {
		if (test != null) {
		    try {
		        new JSONObject(test);
		    } catch (JSONException ex) {
		        try {
		            new JSONArray(test);
		        } catch (JSONException ex1) {
		        	ex1.printStackTrace();
		            return false;
		        }
		    }
		    return true;
		}
		return false;
	}
	

	public String getJustPrefixes(Instance instance) {
		StringBuilder json = new StringBuilder("{\n");
		if (instance.getPrefixList() != null) {
			
			xfile.setPrefixNumber(instance.getPrefixList().size());
			Optional<Prefix> optXbrliPrefix = instance.getPrefixList().stream()
					.filter(p -> p.getName().equals("xbrli"))
					.findFirst();
			if (!optXbrliPrefix.isPresent()) {
				instance.getPrefixList().add(new Prefix("xbrli", "http://www.xbrl.org/2003/instance"));
			}
			Optional<Prefix> optXbrlPrefix = instance.getPrefixList().stream()
					.filter(p -> p.getName().equals("xbrl"))
					.findFirst();
			if (optXbrlPrefix.isPresent()) {
				instance.getPrefixList().remove(optXbrlPrefix.get());
			}
			
			instance.getPrefixList().add(new Prefix("xbrl","http://www.xbrl.org/CR/2017-05-02/oim"));
			
			String comma = "";
			
			for (Prefix prefix: instance.getPrefixList()) {
				json.append(comma);
				comma = ",\n";
				json.append("      \""+prefix.getName()+"\":\""+prefix.getValue()+"\"");
				
			}
		}
		if (json != null && json.length() != 0) {
			json.append("}\n");
			if (json != null && isJSONValid(json.toString())) {
				return json.toString();
			}
		}
		return null;
	}
	

	public String getJustDts(Instance instance) {
		StringBuilder json = new StringBuilder("{\n\n");
		if (instance != null && instance.getDtsList() != null) {
			xfile.setDtsNumber(instance.getDtsList().size());
			String comma = "";
			for (Dts dts: instance.getDtsList()) {
				json.append(comma);
				comma = ",\n		";
				json.append("\""+dts.getName()+"\":\""+dts.getHref()+"\"");
			}
		}
		if (json != null && json.length() != 0) {
			json.append("\n    }\n");
			if (isJSONValid(json.toString())) {
				return json.toString();
			}
		}
		return null;
	}
	
	public String getJustFacts(Instance instance) {
		StringBuilder json = new StringBuilder(" [\n")  ;
		if (instance.getFactList() != null) {
			
			xfile.setFactNumber(instance.getFactList().size());

			Queue<Fact> qfact = new ConcurrentLinkedQueue<>(
					Collections.unmodifiableList(instance.getFactList())
					);
			String comma = "";
			while (qfact.peek() != null) {
				Fact fact = qfact.poll();
				json.append(comma);
				comma = ",\n";
				this.printFact(json, fact, instance);
			}			
		}
		
		if (json != null && json.length() != 0) {
			json.append("\n  ]\n");
			
			if (json != null && isJSONValid(json.toString())) {
				return json.toString();
			}
		}

		return null;
	}	
	
	public void printPreloadFacts(StringBuilder json, Instance instance) {
		if (instance.getFactList() != null) {
			xfile.setFactNumber(instance.getFactList().size());
			json.append("  \"fact\": [\n");
			json.append("      { \"msg\" : \"wait a moment, still loading "+xfile.getFactNumber()+" facts...\" }");
			json.append("  ]\n");
		}
	}

	public String getPreload(Instance instance) {
		// report
		StringBuilder json = new StringBuilder("{\n"); // root
		json.append("  \"report\" : {\n"); // start of report
		if (instance != null) {
			try {
				json.append("    \"documentType\":\"" + instance.getDocumentType() + "\", \n");
				printPrefixes(json, instance);
				printDtses(json, instance);
				printPreloadFacts(json, instance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		json.append("  } \n"); // end of report
		json.append("} \n"); // root
		
		if (json != null && isJSONValid(json.toString())) {
			return json.toString();
		}
		
		return null;
	}
}
