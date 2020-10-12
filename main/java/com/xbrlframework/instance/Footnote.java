/*
 * created by github.com/marcioAlexandre
 * Jun01, 2018
 * 
 */
 
package com.xbrlframework.instance;

public class Footnote {
	
	private String label;
	private String group;
	private String footnoteType;
	private String footnote;
	private String language;
	
	public Footnote(String label, String group, String footnoteType, String footnote, String language) {
		this.label = label;
		this.group = group;
		this.footnoteType = footnoteType;
		this.footnote = removeInvalidJsonChars(footnote);
		this.language = language;
	}
	
	public String getLabel() {
		return label;
	}

	public String getGroup() {
		return group;
	}

	public String getFootnoteType() {
		return footnoteType;
	}
	public void setFootnoteType(String footnoteType) {
		this.footnoteType = footnoteType;
	}
	public String getFootnote() {
		return footnote;
	}

	public String getLanguage() {
		return language;
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
