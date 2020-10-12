/*
 * created by github.com/marcioAlexandre
 * Jun01, 2018
 * 
 */
 
package com.xbrlframework.instance;

import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class InstanceBusiness {
	
	private Element rootNode;
	private Instance instance;
	
	public InstanceBusiness() {
		rootNode = null;
		instance = new Instance();
	}
	     
    /**
     * <p><b>getRootNode</b></p>
     * <p>get the root node of the XBRL instance document</p>
     * 
     * @return Element
     */
    public Element getRootNode() {
    	return rootNode;
    }
    
    /**
     * <p><b>setRootNode</b></p>
     * <p>set the root node of a XBRL instance document.</p>
     * 
     * @return Element
     */
    public void setRootNodeFrom(Document doc) {
    	if (doc != null) {
    		rootNode = doc.getDocumentElement();
    	}else {
    		rootNode = null;
    	}
    }
    
    /**
     * <p><b>getPrefixes</b></p>
     * <p>get all namespaces/prefixes contained in XBRL root node</p>
     * 
     * @param Element
     * @return List<Prefix>
     */
    public List<Prefix> getPrefixes(Element rootNode){
    	List<Prefix> prefixList = new ArrayList<Prefix>();
    	if (rootNode != null) {
			NamedNodeMap namespaceList = rootNode.getAttributes();
			for (int i=0; i< namespaceList.getLength();i++) {
				Node namespace = namespaceList.item(i);
				Prefix prefix = new Prefix();
				prefix.setName(namespace.getNodeName());
				prefix.setValue(namespace.getNodeValue());
				prefixList.add(prefix);
			}
    	}
		return prefixList;
    }
    
    /**
     * <p><b>putPrefixes</b></p>
     * <p>put Prefix list in Instance object</p>
     */
    public void putPrefixes() {
    	List<Prefix> prefixList = this.getPrefixes(this.getRootNode());
    	instance.setPrefixList(prefixList);
    }
    
    /**
     * <p><b>getRefType</b></p>
     * <p>Get the type of DTS reference component</p>
     * 
     * @link http://www.xbrl.org/Specification/xbrl-json/CR-2017-05-02/xbrl-json-CR-2017-05-02.html#component-dts-reference
     * @return String
     * @param String
     */
    public String getRefType(String ref) {
    	if (ref.toLowerCase().contains(":schema")) {
    		return "schema";
    	}else if (ref.toLowerCase().contains(":linkbase")) {
    		return "linkbase";
    	}else if (ref.toLowerCase().contains(":role")) {
    		return "role";
    	}else if (ref.toLowerCase().contains(":arcrole")) {
    		return "arcrole";
    	}else {
    		return null;
    	}
    }
    
    /**
     * <p><b>getXbrlNodeChildrenFrom(element)</b></p>
     * <p>Get all child nodes from and Element node.</p>
     *
     * @return NodeList
     */
    public NodeList getNodeChildrenFrom(Element node) {
		if (node != null) {
			if (node.hasChildNodes()) {
				NodeList nodeChildren = node.getChildNodes();
				return nodeChildren;
			}
		}
		return null;
    }

    /**
     * <p><b>getNodeChildrenFrom(node)</b></p>
     * <p>Get all child nodes from and Node node.</p>
     *
     * @return NodeList
     */
    public NodeList getNodeChildrenFrom(Node node) {
		NodeList nodeChildren = null;
		if (node != null) {
			if (node.hasChildNodes()) {
				nodeChildren = node.getChildNodes();
			}
		}
		return nodeChildren;
    }    
    
    /**
     * <p><b>getSpecificNodeList</b></p>
     * <p>Get a Node list (whose name is given by user) from a specific child-node list</p>
     * @param nodeList
     * @param name
     * @return List<Node>
     */
    public List<Node> getSpecificNodeList(NodeList nodeList, String name) {
    	if (nodeList != null && name != null) {
    		List<Node> specificNodeList = new ArrayList<Node>();
    		for (int i=0; i < nodeList.getLength(); i++) {
    			Node currentNode = nodeList.item(i);
    			if (currentNode.getNodeName().toLowerCase().contains(name.toLowerCase())){
    				specificNodeList.add(currentNode);
    			}
    		}
    		return specificNodeList;
    	}
    	return null;
    }
    
    /**
     * <p><b>getSpecificNodeList</b></p>
     * <p>Get a specific sub-node (whose name is given by user) from a specific child-node set.</p>
     * 
     * @param nodeList
     * @param name
     * @return Node
     */
    public Node getSpecificNode(NodeList nodeList, String name) {
    	if (nodeList != null && name != null) {
    		for (int i=0; i < nodeList.getLength(); i++) {
    			Node currentNode = nodeList.item(i);
    			if (currentNode.getNodeName().toLowerCase()
    					.contains(name.toLowerCase())){
    				Node specificNode = currentNode;
    				return specificNode;
    			}
    		}
    	}
    	return null;
    }  
    
    /**
     * <p><b>getSpecificAttributeValue</b></p>
     * <p>Get the value of some attribute (param) which is contained in a specific Node (param). 
     * 'Name' param is the attribute name.</p>
     * 
     * @param node
     * @param name
     * @return
     */
    public String getSpecificAttributeValue(Node node, String name) {
    	if (node != null && name != null) {
	    	NamedNodeMap attrs = node.getAttributes();
			for (int i=0; i<attrs.getLength(); i++) {
				String attrName = attrs.item(i).getNodeName();
				if (attrName.toLowerCase().contains(name.toLowerCase())) {
					String value = attrs.item(i).getNodeValue();
					return value.trim();
				}
			}
    	}
    	return null;
    }
    
    
    /**<p><b>getDtsList</b></p>
     * <p>Get all dts from Element root node. Dts can be from: schema, linkbase, arc, arcrole.</p>
     * 
     * @param node
     * @return
     */
    public List<Dts> getDtsList(Element node){
		if (node != null) {
			List<Dts> dtsList = new ArrayList<Dts>();
			NodeList xbrlNodeChildren = this.getNodeChildrenFrom(node);
			List<Node> dtsNodeList = this.getSpecificNodeList(xbrlNodeChildren, "ref");
			for (Node dts: dtsNodeList) {
				Dts tempDts = null;
				String name = this.getRefType(dts.getNodeName());
				String value = this.getSpecificAttributeValue(dts, "href");
				if (name != null && value != null) {
					tempDts = new Dts(name, value);
				}
				if (tempDts != null) {
					dtsList.add(tempDts);
				}
			}
			return dtsList;
		}
		return null;
    }
    
    /**
     * <p><b>setDtsListInInstance</b></p>
     * <p>Set the Dts list into Instance object</p>
     *
     */
    public void putDtsList(){
    	List<Dts> dtsList = this.getDtsList(this.rootNode);
		instance.setDtsList(dtsList);
    }
    
    /**
     * <p><b>getPeriod(element)</b></p>
     * <p>Get all Period nodes from a Context node.</p>
     * 
     * @param contextNodeChildren
     * @return
     * @throws Exception
     */
	public Period getPeriod(NodeList contextNodeChildren){
    	if (contextNodeChildren != null) {
    		Period period;
	    	Node periodNode = this.getSpecificNode(contextNodeChildren, "period");
	    	if (periodNode != null) {
		    	String tempPeriod = periodNode.getTextContent()
		    			.trim()
		    			.replace("	", "")
		    			.replace(" ", "");
		    	
		    	if (tempPeriod.contains("\n")) {
		    		String[] temp = tempPeriod.split("\n");
					period = new PeriodStartEnd(temp[0].trim(), temp[temp.length-1].trim());	
				}else{
					period = new PeriodInstant(tempPeriod.trim());
				}
				return period;
	    	}
		}
    	return null;
    }
       
	/**
     * <p><b>getEntity(element)</b></p>
     * <p>Get all Entity nodes from a Context node.</p>
     * 
	 * @param contextNodeChildren
	 * @return
	 */
    public Entity getEntity(NodeList contextNodeChildren) {
    	Node entityNode = this.getSpecificNode(contextNodeChildren, "entity");
		if (entityNode != null) {
			String[] tempEntity = entityNode.getTextContent().trim().split("\n");
			Entity entity = new Entity(tempEntity[0]);
			return entity;
		}
		return null;
    }
        
    /**
     * <p><b>getContexts</b></p>
     * <p>Get all Context nodes from a XBRL root node (Element).</p>
     * 
     * @param xbrlNode
     * @return
     * @throws Exception
     */
    private Map<String, Context> getContexts(){
    	
    	if (this.rootNode != null) {
    		Map<String, Context> contextMap = new HashMap<>();
    		NodeList xbrlNodeChildren = this.getNodeChildrenFrom(rootNode);
    		List<Node> contextNodeList = this.getSpecificNodeList(xbrlNodeChildren, "context");
    		for (Node contextNode: contextNodeList) {

				String id = this.getSpecificAttributeValue(contextNode, "id");
				NodeList contextNodeChildren = this.getNodeChildrenFrom(contextNode);

				// getting Entity
				Entity entity = this.getEntity(contextNodeChildren);

				// getting period
				Period period = this.getPeriod(contextNodeChildren);

				Context context = new Context(id.trim(), entity, period);
				contextMap.put(id, context);
    		}
    		return contextMap;
    	}
    	return null;
    }
    
    /**
     * put all contexts from XBRL-XML file in a Instance object
     */
    public void putContexts() {
    	Map<String, Context> contextMap = this.getContexts();
    	instance.setContextMap(contextMap);
    }
    
    /**
     * <p><b>getUnits</b></p>
     * <p>Get all Unit nodes from a XBRL root node (Element).</p>
     * 
     * @param xbrlNode
     * @return
     */
    private Map<String, Unit> getUnits(){
    	Map<String, Unit> unitHashMap = new HashMap<>();
    	if (rootNode != null) {
    		NodeList xbrlNodeChildren = this.getNodeChildrenFrom(rootNode);
    		List<Node> unitNodeList = this.getSpecificNodeList(xbrlNodeChildren, "unit");
  		
    		for (Node unitNode: unitNodeList) {
				String id = this.getSpecificAttributeValue(unitNode, "id");
				String value = unitNode.getTextContent()
						.trim()
						.replace("	", "");

				if (value.contains("\n")) {
					String[] temp = value.split("\n");
					value = "unitNumerator:"+temp[0].trim()+"/unitDenominator:"+temp[temp.length-1].trim();
				}
				
				Unit unit = new Unit(id, value);
				unitHashMap.put(id, unit);
    		}
    	}
    	return unitHashMap;	
    }
    
    public void putUnits() {
    	instance.setUnitMap(this.getUnits());
    }
      
    
    /**
     * get all fact from XBRL-XML file
     * 
     * @return List<Fact>
     */
    public List<Fact> getFacts(){
    	List<Fact> facts = new ArrayList<>();
    	//Thread thread = null;
    	NodeList nodes = this.getNodeChildrenFrom(this.getRootNode());
    	for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (this.isFact(node)) {
				Fact fact = this.getFact(node);
				facts.add(fact);
			}
		}
		return facts;
    }    
    
    /**
     * build a Fact object from facts contained in XBRL-XML file
     * 
     * @param node
     * @return
     */
    public Fact getFact(Node node) {
		NamedNodeMap attrs = node.getAttributes();

		Fact fact = null;
		String attrId = "", attrUnit = "", attrName = "", attrContext = "", attrDecimals = "";
		List<Attribute> attributes = new ArrayList<>();

		for (int i = 0; i < attrs.getLength(); i++) {
			Node attr = null;
			attr = attrs.item(i);

			attrName = attr.getNodeName().trim().toLowerCase();

			if (attrName.contains("id")) {
				attrId = attr.getNodeValue().trim();
			} else if (attrName.contains("unitref")) {
				attrUnit = attr.getNodeValue().trim();
			} else if (attrName.contains("contextref")) {
				attrContext = attr.getNodeValue().trim();
			} else if (attrName.contains("decimals")) {
				attrDecimals = attr.getNodeValue().trim();
			} else {
				Attribute otherAttr = new Attribute();
				otherAttr.setName(attrName);
				otherAttr.setValue(attr.getNodeValue());
				attributes.add(otherAttr);
			}
		}
		fact = new Fact(attrId, node.getNodeName().trim(), attrContext, attrUnit, attrDecimals,
				node.getTextContent().trim(), attributes);
		return fact;
    }
    
    /**
     * <p><b>isFact</b></p>
     * <p>Verify if the node is a XBRL Fact</p>
     * @param node
     * @return
     */
	private boolean isFact(Node node) {
		if (node.hasAttributes()) {
			NamedNodeMap attrs = node.getAttributes();
			for (int i=0; i < attrs.getLength(); i++) {
				Node attr = attrs.item(i);
				if (attr.getNodeName().toLowerCase().contains("contextref") ||
					attr.getNodeName().toLowerCase().contains("unitref") ){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * put all facts from XBRL-XML file in a Instance object
	 * 
	 */
	public void putFacts() {
		List<Fact> facts = this.getFacts();
		instance.setFactList(facts);
	}
	
	/**
	 * <p><b>getFootnoteByLabel</b></p>
	 * <p>get footnote node by its 'label' attr, which is the 'to' attr of footnoteArc node.</p>
	 * <p></p>
	 * @param label
	 * @param foots
	 * @return
	 */
	private Footnote getFootnoteByLabel(String label, List<Footnote> foots) {
		return foots.stream()
				.filter(f -> f.getLabel().toLowerCase().contains(label.toLowerCase()))
				.findFirst().get();
	}
	
	/**
	 * <p><b>getFootnoteArcByFrom</b></p>
	 * <p>get footnoteArc by its 'from' attr, which is the 'label' attr of Loc node.</p>
	 * 
	 * @param label
	 * @param footArcs
	 * @return
	 */
	private FootnoteArc getFootnoteArcByFrom(String label, List<FootnoteArc> footArcs) {
		return footArcs.stream()
				.filter(f -> f.getFrom().toLowerCase().contains(label.toLowerCase()))
				.findFirst().get();
	}
	
	/**
	 * <p><b>putFootnotesAsHash</b></p>
	 * <p>put all footnotes as a hash.</p> 
	 * <p>The Loc 'href' attr is the key (which is the Fact id of the current report) and footnote is the value.</p>
	 * @return
	 */
	private Map<String, Footnote> getFootnoteMap(){
		if (this.getFootnoteLink() != null 
				&& (this.getFootnoteLink().getLocs().size() > 0
				&& this.getFootnoteLink().getFootnotes().size() > 0
				&& this.getFootnoteLink().getFootnoteArcs().size() > 0)) {
			List<Loc> locs = this.getFootnoteLink().getLocs();
				if (locs != null) {
					Map<String, Footnote> footHash = new HashMap<>();;
					for (Loc loc: locs) {
						String id = loc.getHref();
						FootnoteArc footArc = this.getFootnoteArcByFrom(loc.getLabel(), this.getFootnoteLink().getFootnoteArcs());
						Footnote foot = this.getFootnoteByLabel(footArc.getTo(),this.getFootnoteLink().getFootnotes());
						foot.setFootnoteType(footArc.getFootnoteType());
						footHash.put(id, foot);
					}
					return footHash;
			}
		}
		return null;
	}
	
	/**
	 * put all footnotes from XBRL-XML file in a Instance object
	 * 
	 */
	public void putFootnoteLink() {
		Map<String, Footnote> footMap = this.getFootnoteMap();
		if (footMap != null) {
			instance.setFootnoteList(footMap);
		}
	}
	
	/**
	 * <p><b>getFootnoteLink</b></p>
	 * <p>get the footnoteLink node of the current XBRL-XML file</p>
	 * @return
	 */
	private FootnoteLink getFootnoteLink(){
		if (getRootNode() != null) {
			NodeList nodes = this.getNodeChildrenFrom(this.getRootNode());
			Node footnoteLinkNode = this.getSpecificNode(nodes, "footnoteLink");
			if (footnoteLinkNode != null) {
				if (footnoteLinkNode.hasChildNodes()) {
					List<Loc> locs = new ArrayList<>();
					List<Footnote> footnotes = new ArrayList<>();
					List<FootnoteArc> footnoteArcs = new ArrayList<>();
					
					String group = this.getSpecificAttributeValue(footnoteLinkNode, "role");
					NodeList footLinkChildren = footnoteLinkNode.getChildNodes();
								
					for (int i = 0; i < footLinkChildren.getLength(); i++) {
						
						Node footLinkChild = footLinkChildren.item(i);
						String footLinkChildName = footLinkChild.getNodeName();
		
						if (footLinkChildName.toLowerCase().contains("loc")) {
							Loc loc = null;
							if (footLinkChild.hasAttributes()) {
								String href = this.getSpecificAttributeValue(footLinkChild, "href");
								String label = this.getSpecificAttributeValue(footLinkChild, "label");
								loc = new Loc(href, label);
								locs.add(loc);
							}
						} else if (footLinkChildName.toLowerCase().contains("footnote")	&& !footLinkChildName.toLowerCase().contains("footnotearc")) {
							Footnote foot = null;
							if (footLinkChild.hasAttributes()) {
								String label = this.getSpecificAttributeValue(footLinkChild, "label");
								String footnote = footLinkChild.getTextContent().trim();
								String language = this.getSpecificAttributeValue(footLinkChild, "lang");
								foot = new Footnote(label, group, null, footnote, language);
								footnotes.add(foot);
							}
						} else if (footLinkChildName.toLowerCase().contains("footnotearc")) {
							FootnoteArc footArc = null;
							if (footLinkChild.hasAttributes()) {
								String footnoteType = this.getSpecificAttributeValue(footLinkChild, "arcrole");
								String from = this.getSpecificAttributeValue(footLinkChild, "from");
								String to = this.getSpecificAttributeValue(footLinkChild, "to");
								footArc = new FootnoteArc(footnoteType, from, to);
								footnoteArcs.add(footArc);
							}
						}
		
					}
					if (locs.size() == 0 && footnotes.size() == 0 && footnoteArcs.size() == 0) {
						return null;
					}else {
						return new FootnoteLink(locs,footnotes,footnoteArcs);
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * <p><b>getInstance</b></p>
	 * Just get the current Instance object
	 * 
	 * @return
	 */
	public Instance getInstance() {
		return this.instance;
	}
	
	/**
	 * build all Instance object from XBRL-XML file
	 */
	public void build() {
		try {
			this.putPrefixes();
			this.putContexts();
			this.putUnits();
			this.putDtsList();
			this.putFacts();
			this.putFootnoteLink();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
