/*
 * created by github.com/marcioAlexandre
 * Jun01, 2018
 * 
 */
 
package com.xbrlframework.instance;

public class Prefix {
	
	private String name;
	private String value;
	
	public Prefix() {
		
	}
	public Prefix(String name, String value) {
		if (name.contains(":")) {
			String[] part = name.split(":");
			this.name = part[1];
		}else {
			this.name = name;
		}
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if (name.contains(":")) {
			String[] part = name.split(":");
			this.name = part[1];
		}else {
			this.name = name;
		}
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
