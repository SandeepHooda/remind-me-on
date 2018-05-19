package com.login.vo;

public class OtpCounter {
	private long _id;
	private String email;
	private String otpSentOn;
	private long phoneNumber;
	public String toString() {
		return email+" "+otpSentOn;
	}
	public long get_id() {
		return _id;
	}
	public void set_id(long _id) {
		this._id = _id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOtpSentOn() {
		return otpSentOn;
	}
	public void setOtpSentOn(String otpSentOn) {
		this.otpSentOn = otpSentOn;
	}
	public long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
