package com.login.facade;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.util.CollectionUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.vo.LoginVO;
import com.login.vo.Phone;
import com.reminder.facade.ReminderFacade;
import com.reminder.vo.ReminderVO;
import com.reminder.vo.ReminderVOComparator;

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
	
	private List<Phone> getUserPhones(String email){
		String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "registered-users-phones",email,"email", false, null, null)+"]";
		 Gson  json = new Gson();
		 List<Phone> existingPhones  = json.fromJson(data, new TypeToken<List<Phone>>() {}.getType());
		 return existingPhones;
	}
	public List<Phone> addPhoneNo(Phone phone, String regID) {
		 String email = new ReminderFacade().getEmail(regID);
		 if (null != email && StringUtils.isNotBlank(phone.getNumber())) {
			 phone.setNumber( phone.getNumber().replaceAll("[^\\d.]", ""));
			 phone.setEmail(email);
			 phone.set_id(phone.getCountryCode()+phone.getNumber()+phone.getEmail());
			 String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "registered-users-phones", phone.get_id(),null, true, null, null)+"]";
			 Gson  json = new Gson();
			 List<Phone> existingPhones  = json.fromJson(data, new TypeToken<List<Phone>>() {}.getType());
			 if (CollectionUtils.isEmpty(existingPhones)) {
				 String newPhone = json.toJson(phone, new TypeToken<Phone>() {}.getType());
				 MangoDB.createNewDocumentInCollection("remind-me-on", "registered-users-phones", newPhone, null);
				 System.out.println(" new Phone added ");
			 }else {
				 System.out.println(" Phone already exits");
				
			 }
			 return getUserPhones(email);
			
			 
		 }else {
			 return new ArrayList<Phone>();
		 }
		
       
	}

}
