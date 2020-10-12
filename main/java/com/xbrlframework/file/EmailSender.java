/*
 * created by github.com/marcioAlexandre
 * 6 Jun 2019
 *
 */

package com.xbrlframework.file;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {
	
	// https://www.tutorialspoint.com/java/java_sending_email.htm
	// https://stackoverflow.com/questions/10509699/must-issue-a-starttls-command-first
	
	private String getName(String filename) {
		String name = "";
		if (filename.indexOf("/") > -1) {
			String[] aux = filename.split("/");
			String temp = aux[aux.length-1];
			aux = temp.split("\\.");
			name = aux[0];			
		}else {
			String[] aux = filename.split("\\.");
			name = aux[0];
		}
		return name;
	}
	
	protected void execute(String emailto, String filename, String xbrljson) throws AddressException {
		 	
			//...your email host
		   
	} 

}
