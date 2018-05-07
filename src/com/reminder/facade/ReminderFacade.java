package com.reminder.facade;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.vo.LoginVO;
import com.reminder.vo.ReminderVO;

import mangodb.MangoDB;

public class ReminderFacade {
	
	public boolean addReminder(ReminderVO reminderVO) {
		
		 Gson  json = new Gson();
         
         String email = getEmail(reminderVO.getRegID());
         if (null != email) {
        	 reminderVO.setEmail(email);
        	 String data = json.toJson(reminderVO, new TypeToken<ReminderVO>() {}.getType());
        	 MangoDB.createNewDocumentInCollection("remind-me-on", "reminders", data, null);
     		return true;
         }else {
        	 return false;
         }
         
	}
	public List<ReminderVO> getReminders(String regID) {
		 String email = getEmail(regID);
		 if (null != email) {
			 String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "reminders", email,"email", false, null, null)+"]";
			 Gson  json = new Gson();
			 List<ReminderVO> result  = json.fromJson(data, new TypeToken<List<ReminderVO>>() {}.getType());
			 return result;
		 }else {
			 return new ArrayList<ReminderVO>();
		 }
		
        
	}
	
	private String getEmail(String regID) {
		String email = null;
		String data = MangoDB.getDocumentWithQuery("remind-me-on", "registered-users", regID, null,true, null, null);
		if (null != data) {
			Gson  json = new Gson();
			LoginVO loginVO = json.fromJson(data, new TypeToken<LoginVO>() {}.getType());
			if (null != loginVO) {
				email = loginVO.getEmailID();
			}
			
		}
		
		return email;
	}

}
