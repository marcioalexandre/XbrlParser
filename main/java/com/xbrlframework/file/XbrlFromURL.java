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
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XbrlFromURL implements Xbrl {
	
	private URL url;
	
	protected XbrlFromURL (String address) {
		URL url = null;
		try {
			url = new URL(address);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.url = url;		
	}
	
	public XbrlFile processing() {
		if (url != null) {
			Document doc = null;
			try {
				InputStream inputStream = url.openStream();
				Reader reader = new InputStreamReader(inputStream,"UTF-8");
				InputSource is = new InputSource(reader);
				DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				doc = dBuilder.parse(is);
			}catch (IOException e) {
				System.out.println("IOException: "+e.getMessage());
			}catch (ParserConfigurationException e) {
				System.out.println("ParserConfigurationException: "+e.getMessage());
			}catch (SAXException e) {
				System.out.println("SAXException: "+e.getMessage());
			}
			return this.setFileAs(doc);
		}
		return null;
	}

}
