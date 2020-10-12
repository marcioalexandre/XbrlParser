/*
 * created by github.com/marcioAlexandre
 * Jun01, 2018
 * 
 */
 
package com.xbrlframework.instance;

public class FootnoteArc {
	
	private String footnoteType;
	private String from;
	private String to;
	
	public FootnoteArc(String footnoteType, String from, String to) {
		this.footnoteType = footnoteType;
		this.from = from;
		this.to = to;
	}
	public String getFootnoteType() {
		return footnoteType;
	}
	public void setFootnoteType(String footnoteType) {
		this.footnoteType = footnoteType;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
	

}
