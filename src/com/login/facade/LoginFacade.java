package com.login.facade;

import mangodb.MangoDB;

public class LoginFacade {
	
	public boolean validateRegID(String regID) {
		String data = MangoDB.getDocumentWithQuery("remind-me-on", "registered-users", regID, true, null, null);
		return data.indexOf(regID) >0 ? true:false;
	}
	
	public boolean logout(String regID) {
		MangoDB.deleteDocument("remind-me-on", "registered-users", regID,  null);
		return validateRegID(regID);
	}

}
