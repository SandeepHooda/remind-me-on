package com.product.Response;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JSONFormatter implements ContextResolver<ObjectMapper>{	
	
	    private ObjectMapper objectMapper;

	    public JSONFormatter() throws Exception {
	        this.objectMapper = new ObjectMapper();
	        // SimpleDateFormat is  not thread safe so use a new instance every tim
	    this.objectMapper
    	.configure(MapperFeature.USE_ANNOTATIONS, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)       
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);  // to write data as string and not as numbers
	    this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		    
	    }

	    @Override
	    public ObjectMapper getContext(Class<?> objectType) {
	        return objectMapper;
	    }
}

