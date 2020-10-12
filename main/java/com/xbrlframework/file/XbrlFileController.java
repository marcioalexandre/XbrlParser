/*
 * created by github.com/marcioAlexandre
 * Jun01, 2018
 * 
 */

package com.xbrlframework.file;

import javax.mail.internet.AddressException;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xbrlframework.instance.InstanceBusiness;

@RestController
public class XbrlFileController {


	@PostMapping(value="/permission")
	private boolean checkPermission(@RequestBody String key) {
		if (key.equals("a6s5df1as68f3a1sef8a6ef31as5fas9f8se31a3s8f4ase86")) {
			return true;
		}else {
			return false;
		}
	}

	@PostMapping(value="/upload-file")
	public String loadXbrlFromFile(@RequestPart("file") MultipartFile file, @RequestPart("email") String email) throws AddressException {
		String message = "";
		if ((file.getOriginalFilename().contains(".xml") 
				|| file.getOriginalFilename().contains(".xbrl"))) 
		{
			if (!file.isEmpty() ) 
			{
				XbrlFileBusiness xfb = new XbrlFileBusiness();
				try {
					xfb.setFileAs(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (xfb.getFileAsDocument() != null) {
					InstanceBusiness ib = new InstanceBusiness();
					ib.setRootNodeFrom(xfb.getFileAsDocument());
					ib.build();
					String jsonReport = xfb.parseToJson(ib.getInstance());
					EmailSender es = new EmailSender();
					es.toGmail(email, file.getOriginalFilename(), jsonReport);
					return jsonReport;
				}
			}else {
				message = "{\"#### xbrlapi ####: [This file is empty]\"}";
			}
		}else {
			message = "{\"#### xbrlapi ####: [It must be a XBRL or XML file]\"}"; 
		}
		return message;
	}

	@PostMapping(value="/upload-uri")
	public String loadXbrlFromURI(@RequestBody String data) {
		JSONObject jsonfile = new JSONObject(data);
		String email = (String) jsonfile.get("email");
		String uri = (String) jsonfile.get("uri");
		try {
			if (uri != null && ! uri.trim().isEmpty()) {
				XbrlFileBusiness xfb = new XbrlFileBusiness();
				try {
					xfb.setFileAs(uri);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (xfb.getFileAsDocument() != null) {
					InstanceBusiness ib = new InstanceBusiness();
					ib.setRootNodeFrom(xfb.getFileAsDocument());
					ib.build();
					String jsonReport = xfb.parseToJson(ib.getInstance());
					EmailSender es = new EmailSender();
					es.toGmail(email, uri, jsonReport);
					return jsonReport;
				}
			}
			return "{\"#### xbrlapi ####: [file has NOT been processed by server successfuly]\"}";
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@PostMapping(value="/preload-file")
	public String getPreLoadFile(@RequestBody MultipartFile file) {
		if (file != null) {
			String message = "";
			if ((file.getOriginalFilename().contains(".xml") 
					|| file.getOriginalFilename().contains(".xbrl"))) 
			{
				if (!file.isEmpty() ) 
				{
					XbrlFileBusiness xfb = new XbrlFileBusiness();
					try {
						xfb.setFileAs(file);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (xfb.getFileAsDocument() != null) {
						InstanceBusiness ib = new InstanceBusiness();
						ib.setRootNodeFrom(xfb.getFileAsDocument());
						ib.putPrefixes();
						ib.putDtsList();
						ib.putFacts();
						String jsonReport = xfb.getPreload(ib.getInstance());
						return jsonReport;
					}
				}else {
					message = "{\"#### xbrlapi ####: [This file is empty]\"}";
				}
			}else {
				message = "{\"#### xbrlapi ####: [It must be a XBRL or XML file]\"}"; 
			}
			return message;
		}else {
			return "{ \"file has not been loaded\" }";
		}

	}

	@PostMapping(value="/preload-uri")
	public String getPreLoadURI(@RequestBody String data) {
		JSONObject jsonfile = new JSONObject(data);
		String uri = (String) jsonfile.get("uri");
		try {
			if (uri != null && !uri.trim().isEmpty()) {
				XbrlFileBusiness xfb = new XbrlFileBusiness();
				try {
					xfb.setFileAs(uri);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (xfb.getFileAsDocument() != null) {
					InstanceBusiness ib = new InstanceBusiness();
					ib.setRootNodeFrom(xfb.getFileAsDocument());
					ib.build();
					String jsonReport = xfb.getPreload(ib.getInstance());
					return jsonReport;
				}
			}
			return "{\"#### xbrlapi ####: [file has NOT been processed by server successfuly]\"}";
		} catch (Exception e) {
			return e.getMessage();
		}

	}
	
}
