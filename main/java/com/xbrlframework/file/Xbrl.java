/*
 * created by github.com/marcioAlexandre
 * 15 Feb 2020
 *
 */

package com.xbrlframework.file;

import org.w3c.dom.Document;

interface Xbrl {
	
	default boolean isXbrlDoc(Document file) {
		if (file == null) {
			return false;
		} else if (file.getDocumentElement().getNodeName().toLowerCase().contains("xbrl")) {
			return true;
		} else {
			return false;
		}
	}
	
	default XbrlFile setFileAs(Document file) {
		XbrlFile xfile = new XbrlFile();
		xfile = new XbrlFile();
		if (file != null) {
			if (this.isXbrlDoc(file)) {
				xfile.setDocumentFile(file);
				return xfile;
			}
		}
		return null;
	}
	
	abstract XbrlFile processing();

}
