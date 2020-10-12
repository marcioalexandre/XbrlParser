/*
 * created by github.com/marcioAlexandre
 * Jun01, 2018
 * 
 */

package com.xbrlframework.file;

import java.time.LocalDateTime;

/*
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
*/

import org.w3c.dom.Document;

//@Entity
public class XbrlFile {
	
	//@Id
	//@GeneratedValue
	private int id;
	private String name;
	private long size;
	private int prefixNumber;
	private int dtsNumber;
	private int contextNumber;
	private int unitNumber;
	private int factNumber;
	private int footnoteNumber;
	private LocalDateTime parsingDate;
	
	//@Transient
	private Document documentFile;
	
	XbrlFile() {
		name = null;
		size = 0;
		documentFile = null;
		parsingDate = LocalDateTime.now();
	}
	
	public XbrlFile(long id, String name, long size, int prefixNumber, 
			int dtsNumber, int contextNumber, int unitNumber, int factNumber, Document documentFile) {
		this.name = name;
		this.size = size;
		this.documentFile = documentFile;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getPrefixNumber() {
		return prefixNumber;
	}

	public void setPrefixNumber(int prefixNumber) {
		this.prefixNumber = prefixNumber;
	}
	
	public int getContextNumber() {
		return contextNumber;
	}

	public void setContextNumber(int contextNumber) {
		this.contextNumber = contextNumber;
	}

	public int getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(int unitNumber) {
		this.unitNumber = unitNumber;
	}

	public int getDtsNumber() {
		return dtsNumber;
	}

	public void setDtsNumber(int dtsNumber) {
		this.dtsNumber = dtsNumber;
	}

	public int getFactNumber() {
		return factNumber;
	}

	public void setFactNumber(int factNumber) {
		this.factNumber = factNumber;
	}

	public int getFootnoteNumber() {
		return footnoteNumber;
	}

	public void setFootnoteNumber(int footnoteNumber) {
		this.footnoteNumber = footnoteNumber;
	}

	public void setDocumentFile(Document documentFile) {
		this.documentFile = documentFile;
	}

	public Document getDocumentFile() {
		return documentFile;
	}

	public LocalDateTime getParsingDate() {
		return parsingDate;
	}

	public void setParsingDate(LocalDateTime parsingDate) {
		this.parsingDate = parsingDate;
	}
	
}
