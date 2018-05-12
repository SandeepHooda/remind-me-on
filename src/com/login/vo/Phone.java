package com.login.vo;

public class Phone {
	private String _id;
	private String countryCode;
	private String number;
	private String email;
	private String otpCode;
	private boolean verified;
	private long otpSentTime;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOtpCode() {
		return otpCode;
	}
	public void setOtpCode(String otpCode) {
		this.otpCode = otpCode;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public long getOtpSentTime() {
		return otpSentTime;
	}
	public void setOtpSentTime(long otpSentTime) {
		this.otpSentTime = otpSentTime;
	}

}
