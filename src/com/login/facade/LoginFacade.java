package com.login.facade;



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.util.CollectionUtils;

import com.communication.email.MailService;
import com.communication.phone.text.Key;
import com.communication.phone.text.SendSMS;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.vo.LoginVO;
import com.login.vo.Phone;
import com.login.vo.Settings;
import com.reminder.facade.ReminderFacade;
import com.reminder.vo.ReminderVO;
import com.reminder.vo.ReminderVOComparator;

import mangodb.MangoDB;

public class LoginFacade {
	private static final Logger log = Logger.getLogger(LoginFacade.class.getName());
	
	public LoginVO validateRegID(String regID, String appTimeZone) {
		log.info("regID logged into system "+regID);
		String data = MangoDB.getDocumentWithQuery("remind-me-on", "registered-users", regID, null,true, null, null);
		 Gson  json = new Gson();
		 LoginVO result  = json.fromJson(data, new TypeToken<LoginVO>() {}.getType());
		 String email = result.getEmailID();
		 if (null != result && StringUtils.isNotBlank(email)) {
			 result.setAppTimeZone(appTimeZone);
			 result.setLoginTime(new Date().getTime());
			 data = json.toJson(result, new TypeToken<LoginVO>() {}.getType());
			 MangoDB.createNewDocumentInCollection("remind-me-on", "registered-users", data,null);//Insert loging time stamp
			 
			 //Update settings 
			 String settingsJson = MangoDB.getDocumentWithQuery("remind-me-on", "registered-users-settings", email, null,true, null, null);
			 Settings settings = json.fromJson(settingsJson, new TypeToken<Settings>() {}.getType());
			 if (null == settings ) {
				 settings = new Settings();
				 settings.set_id(email);
			}
			 settings.setAppTimeZone(appTimeZone);
			 settingsJson = json.toJson(settings, new TypeToken<Settings>() {}.getType());
			 MangoDB.createNewDocumentInCollection("remind-me-on", "registered-users-settings", settingsJson, null);
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
	public List<String> getPhoneViaStatus(String email, boolean status){
		List<Phone> allPhones = getUserPhones( email);
		List<String> filteredList = new ArrayList<String>();
		if (!CollectionUtils.isEmpty(allPhones)) {
			for (Phone aPhone: allPhones) {
				if(aPhone.isVerified() == status) {
					filteredList.add(Key.countryCodeMap.get(aPhone.getCountryCode())+aPhone.getNumber());
				}
			}
		}
		return filteredList;
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
				 MangoDB.createNewDocumentInCollection("remind-me-on", "registered-users-phones", newPhone,  null);
				 return true;
			 }
		 }
		 return false;
	}
	public void sendOtp(String phoneID ,String userName) {
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
			 MangoDB.createNewDocumentInCollection("remind-me-on", "registered-users-phones", newPhone, null);
			 
			 data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "registered-users-phones", phoneID,null, true, null, null)+"]";
			 json = new Gson();
			 existingPhones  = json.fromJson(data, new TypeToken<List<Phone>>() {}.getType());
			 if (!CollectionUtils.isEmpty(existingPhones)) {
				 phone = existingPhones.get(0);
				 phone.getOtpCode().equals(""+otpInt);
				 String destination = phone.getNumber();
				 String countryCode = Key.countryCodeMap.get(phone.getCountryCode());
				 if (null != countryCode) {
					 destination = countryCode+destination;
					
					 try {
						SendSMS.sendText(destination, userName+" OTP to verify your phone no is "+otpInt );
						System.out.println(" Opt sent");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
				 }else {
					 log.info(" countryCode not present for "+phone.getCountryCode());
				 }
				 
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
