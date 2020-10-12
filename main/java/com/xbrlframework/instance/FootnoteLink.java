/*
 * created by github.com/marcioAlexandre
 * Jun01, 2018
 * 
 */
 
package com.xbrlframework.instance;

import java.util.List;

public class FootnoteLink {
	
	private List<Loc> locs;
	private List<Footnote> footnotes;
	private List<FootnoteArc> footnoteArcs;
	
	public FootnoteLink(List<Loc> locs, List<Footnote> footnotes, List<FootnoteArc> footnoteArcs) {
		this.locs = locs;
		this.footnotes = footnotes;
		this.footnoteArcs = footnoteArcs;
	}

	public List<Loc> getLocs() {
		return locs;
	}

	public void setLocs(List<Loc> locs) {
		this.locs = locs;
	}

	public List<Footnote> getFootnotes() {
		return footnotes;
	}

	public void setFootnotes(List<Footnote> footnotes) {
		this.footnotes = footnotes;
	}

	public List<FootnoteArc> getFootnoteArcs() {
		return footnoteArcs;
	}

	public void setFootnoteArcs(List<FootnoteArc> footnoteArcs) {
		this.footnoteArcs = footnoteArcs;
	}
	

}
