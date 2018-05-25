package com.login.vo;

import java.util.Map;
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
	private Settings userSettings;
	private String ipAddress;
	private IPtoLocation ipAddressLocation;
	private String userAgent;
	private String completeHeaders;
	private UserAgent userAgentObj;
	private Map<String, String> requestHeaders;
	private GoogleGeoLocation googleGeoLocation;
	 
	
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
	public Settings getUserSettings() {
		return userSettings;
	}
	public void setUserSettings(Settings userSettings) {
		this.userSettings = userSettings;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getCompleteHeaders() {
		return completeHeaders;
	}
	public void setCompleteHeaders(String completeHeaders) {
		this.completeHeaders = completeHeaders;
	}
	public UserAgent getUserAgentObj() {
		return userAgentObj;
	}
	public void setUserAgentObj(UserAgent userAgentObj) {
		this.userAgentObj = userAgentObj;
	}
	public Map<String, String> getRequestHeaders() {
		return requestHeaders;
	}
	public void setRequestHeaders(Map<String, String> requestHeaders) {
		this.requestHeaders = requestHeaders;
	}
	public IPtoLocation getIpAddressLocation() {
		return ipAddressLocation;
	}
	public void setIpAddressLocation(IPtoLocation ipAddressLocation) {
		this.ipAddressLocation = ipAddressLocation;
	}
	public GoogleGeoLocation getGoogleGeoLocation() {
		return googleGeoLocation;
	}
	public void setGoogleGeoLocation(GoogleGeoLocation googleGeoLocation) {
		this.googleGeoLocation = googleGeoLocation;
	}
	

}
