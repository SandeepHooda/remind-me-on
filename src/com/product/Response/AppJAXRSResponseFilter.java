package com.product.Response;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;



@Provider

public class AppJAXRSResponseFilter implements ContainerResponseFilter {

	@Context 
	UriInfo uriInfo;
	
	
	@Context
	HttpServletRequest request;

	@Override
	public void filter(ContainerRequestContext requestContext,
			ContainerResponseContext responseContext) throws IOException {	
		Object responseEntity = responseContext.getEntity();
		
		Method[] allMethods = responseEntity.getClass().getMethods();
		
		 for (Method m : allMethods) {
				
					String parameterType  = m.getParameterTypes()[0].getCanonicalName();
					try {
						Object vals[] = new Object[1];
						vals[0] = null; 
						
						if (!"boolean".equals(parameterType) && !"int".equals(parameterType) && !"double".equals(parameterType) ){
							m.invoke(responseEntity,vals);
						}else  {
							if ("int".equals(parameterType) || "double".equals(parameterType)){
								m.invoke(responseEntity,0);
							}
							
						}
					} catch (IllegalArgumentException e) {
						System.out.println(""+e);
					} catch (IllegalAccessException e) {
						System.out.println(""+e);
					} catch (InvocationTargetException e) {
						System.out.println(""+e);
					}
		 }
		

	}
	
	
	
	

}
