package mangodb;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;


public class MangoDB {
	public static final String mlabKeyReminder = "oEEHExhtLS3QShn3Y2Kl4_a4nampQKj9";
	public static final String noCollection = "";
	private static final Logger log = Logger.getLogger(MangoDB.class.getName());
	private static FetchOptions lFetchOptions = FetchOptions.Builder.doNotValidateCertificate().setDeadline(300d);
	private static URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
	
	public static String getDocumentWithQuery(String dbName, String collection,  String documentKey, String keyName, boolean isKeyString, String mlabApiKey, String query){
		if (null == mlabApiKey) {
			mlabApiKey = mlabKeyReminder;
		}
		String httpsURL  = "https://api.mlab.com/api/1/databases/"+dbName+"/collections/"+collection+"?apiKey="+mlabApiKey;
		if (null != documentKey){
			if (isKeyString){
				httpsURL += "&q=%7B%22_id%22:%22"+documentKey+"%22%7D";
			}else {
				httpsURL += "&q=%7B%22"+keyName+"%22:%22"+documentKey+"%22%7D";
			}
			
		}
		
		if (null != query ){
			httpsURL +=  query;
		}
		//System.out.println("This is the url "+httpsURL);
		String responseStr = "";
		 try {
			
		        URL url = new URL(httpsURL);
	            HTTPRequest req = new HTTPRequest(url, HTTPMethod.GET, lFetchOptions);
	            HTTPResponse res = fetcher.fetch(req);
	            responseStr =(new String(res.getContent()));
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	log.warning("Error while gettiung data dbName: "+dbName+" collection :"+collection+" documentKey: "+documentKey+e.getLocalizedMessage());
	        	return null;
	        }
		
		
			responseStr = responseStr.replaceFirst("\\[", "").trim();
			 if (responseStr.indexOf("]") >= 0){
				
				 responseStr = responseStr.substring(0, responseStr.length()-1);
			 }
		
		 return responseStr;
	}
	public static String getADocument(String dbName, String collection, String documentKey,String keyName,  boolean isKeyString, String mlabApiKey){
		return getDocumentWithQuery(dbName,  collection,  documentKey,keyName, isKeyString, mlabApiKey, null);
	}
	public static String getADocument(String dbName, String collection,  String documentKey, String keyName, String mlabApiKey){
		
		return getDocumentWithQuery(dbName,  collection,  documentKey, keyName,true, mlabApiKey, null);
		
	}
	public static String getData(String db, String collection,  String apiKey ){
		if (null == apiKey) {
			apiKey = mlabKeyReminder;
		}
		db = db.toLowerCase();
		return getADocument(db,collection,null,null,apiKey);
	}
	
	
	
public static void createNewDocumentInCollection(String dbName,String collection,  String data, String key){
	if (null == key) {
		key = mlabKeyReminder;
	}
		String httpsURL = "https://api.mlab.com/api/1/databases/"+dbName+"/collections/"+collection+"?apiKey="+key;
		
		 try {
			
		        URL url = new URL(httpsURL);
	            HTTPRequest req = new HTTPRequest(url, HTTPMethod.POST, lFetchOptions);
	            HTTPHeader header = new HTTPHeader("Content-type", "application/json");
	            
	            req.setHeader(header);
	           
	            req.setPayload(data.getBytes());
	            fetcher.fetch(req);
	            
	 
	        } catch (IOException e) {
	        	
	        }
	}
	
	public static void updateData(String dbName,String collection, String data,  String apiKey){
		if (null == apiKey) {
			apiKey = mlabKeyReminder;
		}
		String httpsURL = "https://api.mlab.com/api/1/databases/"+dbName+"/collections/"+collection+"?apiKey="+apiKey;
		/*if (null != documentKey){
			httpsURL += "&q=%7B%22_id%22:%22"+documentKey+"%22%7D";
			
		}*/
		
		 try {
			
		        URL url = new URL(httpsURL);
	            HTTPRequest req = new HTTPRequest(url, HTTPMethod.PUT, lFetchOptions);
	            HTTPHeader header = new HTTPHeader("Content-type", "application/json");
	            
	            req.setHeader(header);
	           
	            req.setPayload(data.getBytes());
	            fetcher.fetch(req);
	            
	           //log.info("Updated the DB  collection "+collection+data);
	 
	        } catch (IOException e) {
	        	 log.info("Error while  upfdating DB  collection "+collection+" Message "+e.getMessage());
	        	e.printStackTrace();
	        	
	        }
		
	}
	
public static void deleteDocument(String dbName,String collection,  String dataKeyTobeDeleted, String key){
	if (null == key) {
		key = mlabKeyReminder;
	}
		
		String httpsURL = "https://api.mlab.com/api/1/databases/"+dbName+"/collections/"+collection+"/"+dataKeyTobeDeleted+"?apiKey="+key;
		 HttpURLConnection connection = null;
		 try {
			
			 URL url = new URL(httpsURL);
	            HTTPRequest req = new HTTPRequest(url, HTTPMethod.DELETE, lFetchOptions);
	            HTTPHeader header = new HTTPHeader("Content-type", "application/json");
	            
	            req.setHeader(header);
	            fetcher.fetch(req);
	            
	         
	        } catch (IOException e) {
	        	e.printStackTrace();
	        } finally {
	            if (connection != null) {
	                connection.disconnect();
	              }
	            }
	}
	

}
