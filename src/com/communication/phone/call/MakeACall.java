package com.communication.phone.call;

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
	public static void call(String phoneNo, String messageID) {
		log.info("Hero making a call sms");
		String httpsURL  = "https://post-master.herokuapp.com/MakeACall?phone="+phoneNo+"&messageID="+messageID;
		
		String responseStr = "";
		 try {
			
		        URL url = new URL(httpsURL);
	            HTTPRequest req = new HTTPRequest(url, HTTPMethod.GET, lFetchOptions);
	            HTTPResponse res = fetcher.fetch(req);
	            responseStr =(new String(res.getContent()));
	            log.info("Call scheduled ");
	        } catch (Exception e) {
	        	log.info("Call schedule error "+e.getLocalizedMessage());
	        	e.printStackTrace();
	        	
	        }
		
	}

}
