package com.login.facade;



import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.vo.LoginVO;

import mangodb.MangoDB;

public class LoginFacade {
	
	public LoginVO validateRegID(String regID, String appTimeZone) {
		String data = MangoDB.getDocumentWithQuery("remind-me-on", "registered-users", regID, null,true, null, null);
		 Gson  json = new Gson();
		 LoginVO result  = json.fromJson(data, new TypeToken<LoginVO>() {}.getType());
		 if (null != result && StringUtils.isNotBlank(result.getEmailID())) {
			 result.setAppTimeZone(appTimeZone);
			 data = json.toJson(result, new TypeToken<LoginVO>() {}.getType());
			 MangoDB.updateData("remind-me-on", "registered-users", data, null);//Insert loging time stamp
			 return result;
		 }else {
			 return null;
		 }
		
	}
	
	public LoginVO logout(String regID) {
		MangoDB.deleteDocument("remind-me-on", "registered-users", regID,  null);
		return validateRegID(regID, null);
	}

}
