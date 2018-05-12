package com.login.vo;

import java.util.UUID;

public class LoginVO {
	
	private String _id ;
	private String regID ;
	private String emailID;
	private String name;
    private String appTimeZone;
	private String userSuppliedTimeZone;
	private String errorMessage;
	private long loginTime;
	
	public LoginVO() {
		this.regID = UUID.randomUUID().toString();
		this._id = this.regID;
	}
	public String getRegID() {
		return regID;
	}
	public void setRegID(String regID) {
		this.regID = regID;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	
	public String getUserSuppliedTimeZone() {
		return userSuppliedTimeZone;
	}
	public void setUserSuppliedTimeZone(String userSuppliedTimeZone) {
		this.userSuppliedTimeZone = userSuppliedTimeZone;
	}
	public String getAppTimeZone() {
		return appTimeZone;
	}
	public void setAppTimeZone(String appTimeZone) {
		this.appTimeZone = appTimeZone;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public long getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

}
