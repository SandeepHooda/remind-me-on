package com.login.vo;

import java.util.List;

public class GoogleGeoLocation {
	
	private String status;
	private List<Google_address_components> results;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Google_address_components> getResults() {
		return results;
	}
	public void setResults(List<Google_address_components> results) {
		this.results = results;
	}
	
	

}
