package com.product.Response;

public class CommunicationResponse {
	private String message;
	private boolean emailSent = false;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isEmailSent() {
		return emailSent;
	}

	public void setEmailSent(boolean emailSent) {
		this.emailSent = emailSent;
	}

}
