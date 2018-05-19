package com.login.facade;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.util.CollectionUtils;

import com.Constants;
import com.communication.phone.text.Key;
import com.communication.phone.text.SendSMS;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.vo.LoginVO;
import com.login.vo.OtpCounter;
import com.login.vo.Phone;
import com.login.vo.Settings;
import com.reminder.facade.ReminderFacade;

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
			 result.setEmailID(email);
			 result.setAppTimeZone(appTimeZone);
			 result.setLoginTime(new Date().getTime());
			 data = json.toJson(result, new TypeToken<LoginVO>() {}.getType());
			 //MangoDB.createNewDocumentInCollection("remind-me-on", "registered-users", data,null);//Insert loging time stamp
			 MangoDB.updateData("remind-me-on", "registered-users", data, result.get_id(),null);//Insert loging time stamp
			 //Update settings 
			 String settingsJson = MangoDB.getDocumentWithQuery("remind-me-on", "registered-users-settings", email, null,true, null, null);
			 Settings settings = json.fromJson(settingsJson, new TypeToken<Settings>() {}.getType());
			 if (null == settings ) {
				 settings = new Settings();
				 settings.set_id(email);
			}
			 settings.setAppTimeZone(appTimeZone);
			 result.setUserSettings(settings);
			 settingsJson = json.toJson(settings, new TypeToken<Settings>() {}.getType());
			 MangoDB.createNewDocumentInCollection("remind-me-on", "registered-users-settings", settingsJson, null);
			 return result;
		 }else {
			 return null;
		 }
		
	}
	
	public LoginVO logout(String regID, HttpSession session) {
		MangoDB.deleteDocument("remind-me-on", "registered-users", regID,  null);
		session.invalidate();
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
			 //1. User can't delete verified phone no
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
				 //MangoDB.createNewDocumentInCollection("remind-me-on", "registered-users-phones", newPhone,  null);
				 MangoDB.updateData("remind-me-on", "registered-users-phones", newPhone,  phone.get_id(),null);
				 return true;
			 }
		 }
		 return false;
	}
	
	//Max 3 OTP per user/email
	private boolean hasOptQuotaForDayExceeded(String email) {
		//OtpCounter
		String sortByTimeSentDesc =  "&s=%7B%22_id%22%3A%20-1%7D";
		 String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "opt-counter", email,"email", false, null,sortByTimeSentDesc)+"]";
		 Gson  json = new Gson();
		 List<OtpCounter> optsSent  = json.fromJson(data, new TypeToken<List<OtpCounter>>() {}.getType());
		 System.out.println(optsSent);
		
		boolean exccededQuota = false;
		if (optsSent.size() >=3 ) {//Max 3 OTP in a day
			if( (new Date().getTime() - optsSent.get(2).get_id()) < Constants.aDay) {
				exccededQuota = true;
				
			}
		}
		return exccededQuota;
	}
	
	
	public void sendOtp(String phoneID ,String userName) {
		//1. Get the phone details from DB on which OTP needs to be sent.
		String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "registered-users-phones", phoneID,null, true, null, null)+"]";
		 Gson  json = new Gson();
		 List<Phone> existingPhones  = json.fromJson(data, new TypeToken<List<Phone>>() {}.getType());
		 if (!CollectionUtils.isEmpty(existingPhones)) {
			 Phone phone = existingPhones.get(0);
			 
			 //2. Check user quote max # OPT per user
			 if(hasOptQuotaForDayExceeded(phone.getEmail())) {
				 System.out.println("Exceeded max 3 quota OTP per user by user " +phone.getEmail());
				 Constants.reportIssue("Too many OTP request ", "Only 3 OTP allowed per user By "+phone.getEmail()+" "+phone.getCountryCode()+" "+phone.getNumber());
				 return;//Exceeded max 3 quota OTP per user
			 }
			 //3. MAx 1 OPT on a phone in a day
			 if (phone.getOtpSentTime() == 0 ||  ( (new Date().getTime() -phone.getOtpSentTime()) > Constants.aDay )) {
				 //Sent OPT 1 OTP in a day
				 phone.setOtpSentTime(new Date().getTime());
				 double otp = Math.random() * 10000;
					int otpInt = (int) otp;
					while(otpInt < 1000) {
						otpInt *= 10;
					}
				 phone.setOtpCode(""+otpInt);
				 String newPhone = json.toJson(phone, new TypeToken<Phone>() {}.getType());
				 //MangoDB.createNewDocumentInCollection("remind-me-on", "registered-users-phones", newPhone, null);
				 MangoDB.updateData("remind-me-on", "registered-users-phones", newPhone,phone.get_id(),  null);
				 
				 //4. Check if DB update with OPT code  is success or not. only then call SMS API
				 data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "registered-users-phones", phoneID,null, true, null, null)+"]";
				 json = new Gson();
				 existingPhones  = json.fromJson(data, new TypeToken<List<Phone>>() {}.getType());
				 if (!CollectionUtils.isEmpty(existingPhones)) {
					 phone = existingPhones.get(0);
					 if(phone.getOtpCode().equals(""+otpInt)) {//Yes OTP updated in DB is same as what we want to send
						 String destination = phone.getNumber();
						 String countryCode = Key.countryCodeMap.get(phone.getCountryCode());
						 if (null != countryCode) {
							 destination = countryCode+destination;
							
							 try {
								 //5. Call SMS API
								SendSMS.sendText(destination, userName+" OTP to verify your phone no is "+otpInt );
								OtpCounter otpCounter = new OtpCounter();
								otpCounter.set_id(new Date().getTime());
								otpCounter.setEmail(phone.getEmail());
								otpCounter.setOtpSentOn(new Date().toString());
								otpCounter.setPhoneNumber(Long.parseLong(destination));
								String otpCounterJson = json.toJson(otpCounter, new TypeToken<OtpCounter>() {}.getType());
								MangoDB.createNewDocumentInCollection("remind-me-on", "opt-counter", otpCounterJson,null);
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
			 }else {
				 Constants.reportIssue("Too many OTP request ", "Only 1 OTP allowed per user on a given phone User details "+phone.getEmail()+" "+phone.getCountryCode()+" "+phone.getNumber());
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
