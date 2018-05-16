package com.login.vo;

import java.io.Serializable;

public class Settings implements Serializable {
	private String _id;
	private String appTimeZone;
	private String userSuppliedTimeZone;
	private int currentCallCredits ;

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

	public int getCurrentCallCredits() {
		return currentCallCredits;
	}

	public void setCurrentCallCredits(int currentCallCredits) {
		this.currentCallCredits = currentCallCredits;
	}

}
