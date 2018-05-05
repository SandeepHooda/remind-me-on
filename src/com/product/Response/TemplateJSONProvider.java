package com.product.Response;

import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
/**
 * Every JSON response that goes out of system will go thorough this file as the
 * last point. Modification of the response happens in this file as per the requirement
 * If the response extends TemplateResponse then a series of additional properties 
 * that are defined as part of TemplateResponse will be injected here.  After that
 * JACKSON native implementation will be called to marshal the java object to JSON
 * @Ref {@link BATemplateResponse}
 * @author 
 *
 */
@Provider
@Produces({MediaType.APPLICATION_JSON, "text/json"})
public class TemplateJSONProvider extends JacksonJsonProvider{

	public static final String ANGULAR_JSON_VUL = ")]}',\n";
	public static final String ENCODING = "UTF-8";
	
		
	
	
	/**
	 * This method will be the last method to be called. Any injection has to be done from here
	 * @param entityStream - Will have a empty stream. What ever you write to this stream will appear first in JSON
	 * response.
	 * @param value - 
	 */
	@Override
	 public void writeTo(Object value, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
	            MultivaluedMap<String,Object> httpHeaders, OutputStream entityStream) 
	        throws IOException {
		
		
		 //TODO uncomment this line to protect from JSON vulnerability
		 entityStream.write(ANGULAR_JSON_VUL.getBytes(ENCODING));  // For JSON output to overcome JSON vulnerability http://docs.angularjs.org/api/ng.$http
		 
		 super.writeTo(value, type, genericType, annotations, mediaType, httpHeaders, entityStream);
		
	    }

	
}
