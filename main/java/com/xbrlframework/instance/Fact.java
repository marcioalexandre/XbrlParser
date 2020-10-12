/*
 * created by github.com/marcioAlexandre
 * Jun01, 2018
 * 
 */
 
package com.xbrlframework.instance;

import java.util.List;

public class Fact {

	private String id;
	private String name;
	private String contextRef;
	private String unitRef;
	private String decimals;
	private String value;
	
	public Fact(String id, String name, String contextRef, String unitRef, String decimal, String value, List<Attribute> attributes) {
		this.id = id;
		this.name = name;
		this.contextRef = contextRef;
		this.unitRef = unitRef;
		this.decimals = decimal;
		this.value = removeInvalidJsonChars(value);
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getContextRef() {
		return contextRef;
	}

	public String getUnitRef() {
		return unitRef;
	}

	public String getDecimals() {
		return decimals;
	}

	public String getValue() {
		return value;
	}
	
	private String removeInvalidJsonChars(String string) {
		return string
				.replaceAll("\n"," ")
				.replaceAll("\t", " ")
				.replaceAll("\f"," ")
				.replaceAll("\b", " ")
				.replace("\\","\\\\")
				.replace("	"," ")
				.replace("\"", "'")
				.replace("&nbsp;", " ");
	}
	

}
