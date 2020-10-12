/*
 * created by github.com/marcioAlexandre
 * 15 Feb 2020
 *
 */

package com.xbrlframework.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XbrlFromMultipartFile implements Xbrl {
	
	private MultipartFile file;
	
	protected XbrlFromMultipartFile(MultipartFile file) throws Exception{
		if (file != null) {
			this.file = file;
		}else {
			throw new Exception("File cannot be null!");
		}
	}
	
	public XbrlFile processing(){
		if (file != null && !file.isEmpty()) {

			InputStream inputStream = null;
			Reader reader = null;
			DocumentBuilder dBuilder = null;
			Document doc = null;
			
			try {
				
				inputStream = file.getInputStream();
				reader = new InputStreamReader(inputStream,"UTF-8");
				InputSource is = new InputSource(reader);
				is.setEncoding("UTF-8");
				dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();  
				doc = dBuilder.parse(is);
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (SAXException | IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} 
			
			
			if (this.isXbrlDoc(doc)) {
				return this.setFileAs(doc);
			}
			
			return null;
			
		}
		return null;
	}

}
