package com.login.facade;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
			 result.setLoginTime(new Date().getTime());
			 data = json.toJson(result, new TypeToken<LoginVO>() {}.getType());
			 MangoDB.updateData("remind-me-on", "registered-users", data, result.get_id(),null);//Insert loging time stamp
			 return result;
		 }else {
			 return null;
		 }
		
	}
	
	public LoginVO logout(String regID) {
		MangoDB.deleteDocument("remind-me-on", "registered-users", regID,  null);
		return validateRegID(regID, null);
	}
	
	public List<Phone> getUserPhones(String email){
		String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "registered-users-phones",email,"email", false, null, null)+"]";
		 Gson  json = new Gson();
		 List<Phone> existingPhones  = json.fromJson(data, new TypeToken<List<Phone>>() {}.getType());
		 if (!CollectionUtils.isEmpty(existingPhones)) {
			 for (Phone phone: existingPhones) {
				 phone.setOtpCode(null);
				 phone.setOtpSentTime(0);
			 }
		 }
		 return existingPhones;
	}
	public void deletePhone(String phoneID ) {
		String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "registered-users-phones", phoneID,null, true, null, null)+"]";
		 Gson  json = new Gson();
		 List<Phone> existingPhones  = json.fromJson(data, new TypeToken<List<Phone>>() {}.getType());
		 if (!CollectionUtils.isEmpty(existingPhones)) {
			 Phone phone = existingPhones.get(0);
			 if (!phone.isVerified()) {
				 MangoDB.deleteDocument("remind-me-on", "registered-users-phones", phoneID, null);
			 }
		 }
		
	}
	public boolean confirmOPT(String phoneID , String OPT) {
		String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "registered-users-phones", phoneID,null, true, null, null)+"]";
		 Gson  json = new Gson();
		 List<Phone> existingPhones  = json.fromJson(data, new TypeToken<List<Phone>>() {}.getType());
		 if (!CollectionUtils.isEmpty(existingPhones)) {
			 Phone phone = existingPhones.get(0);
			 if (phone.getOtpCode().equalsIgnoreCase(OPT) && ( new Date().getTime() -phone.getOtpSentTime() <3600000 )) {
				 phone.setVerified(true);
				 String newPhone = json.toJson(phone, new TypeToken<Phone>() {}.getType());
				 MangoDB.updateData("remind-me-on", "registered-users-phones", newPhone,  phone.get_id(),null);
				 return true;
			 }
		 }
		 return false;
	}
	public void sendOtp(String phoneID ) {
		String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "registered-users-phones", phoneID,null, true, null, null)+"]";
		 Gson  json = new Gson();
		 List<Phone> existingPhones  = json.fromJson(data, new TypeToken<List<Phone>>() {}.getType());
		 if (!CollectionUtils.isEmpty(existingPhones)) {
			 Phone phone = existingPhones.get(0);
			 phone.setOtpSentTime(new Date().getTime());
			 double otp = Math.random() * 10000;
				int otpInt = (int) otp;
				while(otpInt < 1000) {
					otpInt *= 10;
				}
			 phone.setOtpCode(""+otpInt);
			 String newPhone = json.toJson(phone, new TypeToken<Phone>() {}.getType());
			 MangoDB.updateData("remind-me-on", "registered-users-phones", newPhone,phone.get_id(),  null);
			 
			 data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "registered-users-phones", phoneID,null, true, null, null)+"]";
			 json = new Gson();
			 existingPhones  = json.fromJson(data, new TypeToken<List<Phone>>() {}.getType());
			 if (!CollectionUtils.isEmpty(existingPhones)) {
				 phone = existingPhones.get(0);
				 phone.getOtpCode().equals(""+otpInt);
				 System.out.println(" Opt sent");
			 }
						
		 }
		 
	}
	public List<Phone> addPhoneNo(Phone phone, String regID) {
		 String email = new ReminderFacade().getEmail(regID);
		 if (null != email && StringUtils.isNotBlank(phone.getNumber())) {
			 String phoneNo = phone.getNumber().replaceAll("[^\\d.]", "");
			 while(phoneNo.indexOf("0") ==0) {//remove starting o
				 phoneNo = phoneNo.substring(1);
			 }
			 phone.setNumber( phoneNo);
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
