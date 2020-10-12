/*
 * created by github.com/marcioAlexandre
 * Jun01, 2018
 * 
 */
 
package com.xbrlframework.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a XBRL Instance class
 * @author marcio May05th, 2018
 *
 */
public class Instance {
	private List<Prefix> prefixList;
	private List<Dts> dtsList;
	private List<Fact> factList;
	private Map<String, Context> contextMap;
	private Map<String, Unit> unitMap;
	private Map<String, Footnote> footnoteMap;
	private final String documentType = "http://www.xbrl.org/CR/2017-05-02/xbrl-json";
	
	Instance() {
		prefixList = new ArrayList<Prefix>();
		dtsList = new ArrayList<Dts>();
		factList = new ArrayList<Fact>();
		contextMap = new HashMap<>();
		unitMap = new HashMap<>();
		footnoteMap = new HashMap<>();
		
	}

	public List<Prefix> getPrefixList() {
		return prefixList;
	}

	void setPrefixList(List<Prefix> prefixList) {
		this.prefixList = prefixList;
	}

	public List<Dts> getDtsList() {
		return dtsList;
	}

	void setDtsList(List<Dts> dtsList) {
		this.dtsList = dtsList;
	}

	public List<Fact> getFactList() {
		return factList;
	}

	void setFactList(List<Fact> factList) {
		this.factList = factList;
	}

	public String getDocumentType() {
		return documentType;
	}

	public Map<String, Footnote> getFootnoteMap() {
		return footnoteMap;
	}

	public void setFootnoteList(Map<String, Footnote> footnoteMap) {
		this.footnoteMap = footnoteMap;
	}

	public Map<String, Context> getContextMap() {
		return contextMap;
	}

	public void setContextMap(Map<String, Context> contextMap) {
		this.contextMap = contextMap;
	}

	public void setFootnoteMap(Map<String, Footnote> footnoteMap) {
		this.footnoteMap = footnoteMap;
	}

	public Map<String, Unit> getUnitMap() {
		return unitMap;
	}

	public void setUnitMap(Map<String, Unit> unitMap) {
		this.unitMap = unitMap;
	}
	
	

	
}
