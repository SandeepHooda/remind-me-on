package com.communication.phone.call;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.login.facade.LoginFacade;

public class MakeACall {
	
	private static final Logger log = Logger.getLogger(LoginFacade.class.getName());

	private static FetchOptions lFetchOptions = FetchOptions.Builder.doNotValidateCertificate().setDeadline(300d);
	private static URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
	public static boolean call(String phoneNo, String messageID) throws IOException {
		log.info("Hero making a call sms to "+phoneNo);
		String httpsURL  = "https://post-master.herokuapp.com/MakeACall?phone="+phoneNo+"&messageID="+messageID;
		
		String responseStr = "";
		
			
		        URL url = new URL(httpsURL);
	            HTTPRequest req = new HTTPRequest(url, HTTPMethod.GET, lFetchOptions);
	            HTTPResponse res = fetcher.fetch(req);
	            if (res.getResponseCode() == 200) {
	            	responseStr =(new String(res.getContent()));
		            log.info("Call scheduled "+responseStr);
	            	return true;
	            }else {
	            	return false;
	            }
	            
	       
		
	}

}
