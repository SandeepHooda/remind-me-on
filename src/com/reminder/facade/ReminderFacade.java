package com.reminder.facade;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.vo.LoginVO;
import com.reminder.vo.ReminderVO;

import mangodb.MangoDB;

public class ReminderFacade {
	
	public boolean addReminder(ReminderVO reminderVO) {
		
		 Gson  json = new Gson();
         String data = json.toJson(reminderVO, new TypeToken<ReminderVO>() {}.getType());
         String email = getEmail(reminderVO.getRegID());
         if (null != email) {
        	 MangoDB.createNewDocumentInCollection("remind-me-on", "reminders_"+email, data, null);
     		return true;
         }else {
        	 return false;
         }
         
	}
	
	private String getEmail(String regID) {
		String email = null;
		String data = MangoDB.getDocumentWithQuery("remind-me-on", "registered-users", regID, true, null, null);
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
