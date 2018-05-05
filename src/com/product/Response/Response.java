package com.product.Response;




/**
 * Every response bean that goes out of system should extend the base class
 * @author 
 *
 */
public abstract class Response {


	private ResponseStatus responseStatus = ResponseStatus.createSuccessStatus();

	/**
	 * Includes a default success code.
	 * @return
	 */
	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}
	
	public void setResponseStatus(ResponseStatus status) {
		responseStatus = status;
	}

	
	
}
