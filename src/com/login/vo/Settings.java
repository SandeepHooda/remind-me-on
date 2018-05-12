package com.login.vo;

public class Settings {
	private String _id;
	private String appTimeZone;
	private String userSuppliedTimeZone;

	public String getAppTimeZone() {
		return appTimeZone;
	}

	public void setAppTimeZone(String appTimeZone) {
		this.appTimeZone = appTimeZone;
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

}
