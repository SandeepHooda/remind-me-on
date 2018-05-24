package com.scheduler;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.communication.email.EmailAddess;
import com.communication.email.EmailVO;
import com.communication.email.MailService;
import com.communication.phone.call.MakeACall;
import com.communication.phone.text.Key;
import com.communication.phone.text.SendSMS;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.vo.Settings;
import com.reminder.facade.ReminderFacade;
import com.reminder.vo.CallLogs;
import com.reminder.vo.ReminderVO;

import mangodb.MangoDB;

public class SchedulerService {

	public static List<ReminderVO> getRemindersToBeExecuted(){
		 List<ReminderVO> passedReminders = new ArrayList<>();
		 String sortByExecutionTimeAsc =  "&s=%7B%22nextExecutionTime%22%3A%201%7D";
		String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "reminders", null, null, false, null, sortByExecutionTimeAsc)+"]";
		 Gson  json = new Gson();
		 List<ReminderVO> result  = json.fromJson(data, new TypeToken<List<ReminderVO>>() {}.getType());
		 for (ReminderVO  reminder: result) {
			 //Don't consider reminder comming in near future else it will cause problem with finding next execution time
			 if (new Date().getTime() - reminder.getNextExecutionTime() > 0 /*|| (reminder.getNextExecutionTime() - new Date().getTime()) < 10*60*1000*/) {
				 System.out.println(" Fire this event "+reminder.getReminderSubject() +" "+reminder.getReminderText());
				 passedReminders.add(reminder);
			 }
		 }
		 return passedReminders;
	}
	
	public static void executeReminderAndReschedule(List<ReminderVO> currentReminders) {
		Map<String, Settings> settingsMap = new HashMap<>();
		for(ReminderVO reminderVO : currentReminders) {
			try {
				
				boolean oneTimeReminder = "Once".equalsIgnoreCase(reminderVO.getFrequencyWithDate());
				//Notify user
				
				notifyUser(reminderVO);
				
				//Only if notification was success then only reschedule / update that reminder
				
				if (!oneTimeReminder) {
					
					//Get settings 
					Settings settings = settingsMap.get(reminderVO.getEmail());
					if (null == settings) {
						Gson  json = new Gson();
						 String settingsJson = MangoDB.getDocumentWithQuery("remind-me-on", "registered-users-settings", reminderVO.getEmail(), null,true, null, null);
						 settings = json.fromJson(settingsJson, new TypeToken<Settings>() {}.getType());
						 settingsMap.put(reminderVO.getEmail(), settings);
					}
					
					try {
						
						
						String timeZone = settings.getUserSuppliedTimeZone();
						if (null == timeZone) {
							timeZone = settings.getAppTimeZone();
						}
						
						//Update reminder next execution time in DB
						reminderVO.setNextExecutionTime(ReminderFacade.nextReminder(reminderVO, timeZone).getTime());
						Gson  json = new Gson();
						String data = json.toJson(reminderVO, new TypeToken<ReminderVO>() {}.getType());
						//MangoDB.createNewDocumentInCollection("remind-me-on", "reminders", data, null);
						MangoDB.updateData("remind-me-on", "reminders", data,reminderVO.get_id(), null);
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					
				}else {
					MangoDB.deleteDocument("remind-me-on", "reminders", reminderVO.get_id(), null);
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	private static void notifyUser(ReminderVO reminderVO) throws Exception {
		//Send email
		
			EmailVO emalVO = new EmailVO();
			emalVO.setUserName("personal.reminder.notification@gmail.com");
			emalVO.setPassword(Key.email);
			emalVO.setSubject(reminderVO.getReminderSubject());
			emalVO.setHtmlContent(reminderVO.getReminderText());
			EmailAddess from = new EmailAddess();
			from.setAddress(emalVO.getUserName());
			
			List<EmailAddess> receipients = new ArrayList<>();
			EmailAddess to = new EmailAddess();
			to.setAddress(reminderVO.getEmail());
			emalVO.setFromAddress(from);
			receipients.add(to);
			emalVO.setToAddress(receipients);
			if (!MailService.sendSimpleMail(emalVO)) {
				throw new Exception("Couln't not notify user via email");
			}
			
			
				
		
			
		
		//Get user balance to check if he has funds or not
		Settings settings =  new Settings();
		Gson  json = new Gson();
		String email = reminderVO.getEmail();
		String settingsJson = MangoDB.getDocumentWithQuery("remind-me-on", "registered-users-settings", email, null,true, null, null);
		settings = json.fromJson(settingsJson, new TypeToken<Settings>() {}.getType());
		if (null == settings ) {
			 settings = new Settings();
			 settings.set_id(email);
		 }
			
		  
		
		CallLogs callLog = null;
		//Prepare a log of this call
		if ((reminderVO.isMakeACall() || reminderVO.isSendText()) &&  settings.getCurrentCallCredits() >=1) {
			callLog = new CallLogs();
			callLog.set_id(""+new Date().getTime());
			callLog.setFrom(reminderVO.getEmail());
			callLog.setTo(reminderVO.getSelectedPhone());
			
			callLog.setMessage(reminderVO.getReminderSubject() +" "+reminderVO.getReminderText());
			String logsJson = json.toJson(callLog, new TypeToken<CallLogs>() {}.getType());
			 MangoDB.createNewDocumentInCollection("remind-me-on", "call-logs", logsJson, null);
		}
		
		//Place a call
		if (reminderVO.isMakeACall() && settings.getCurrentCallCredits() >=5) {
			if (!MakeACall.call(callLog.getTo(), callLog.get_id())) {
				throw new Exception("Couln't not notify user via Phone");
			}
			
			//Make a call above the comment and then update settings
			 settings.setCurrentCallCredits(settings.getCurrentCallCredits() -5);
		}
		  
		
		
		//Send SMS
		
			
			 if (reminderVO.isSendText() && settings.getCurrentCallCredits() >=1) {
				 String message = callLog.getMessage();
				 if (null == message) {
					 message = "Reminder for your.";
				 }
				 if (message.length() > 160) {
					 message = message.substring(0, 160);
				 }
				 if (!SendSMS.sendText(callLog.getTo(),callLog.getMessage())) {
						throw new Exception("Couln't not notify user via Phone");
					}
				 
				//Send above the comment and then update settings
				 settings.setCurrentCallCredits(settings.getCurrentCallCredits() -1);
			 }
		
		
			 settingsJson = json.toJson(settings, new TypeToken<Settings>() {}.getType());
			 MangoDB.updateData("remind-me-on", "registered-users-settings", settingsJson, reminderVO.getEmail(), null);
			 
		
		
		
	}
	
	public static void snoozReminders(List<ReminderVO> currentReminders) {
		 Gson  json = new Gson();
		 String data = json.toJson(currentReminders, new TypeToken<List<ReminderVO>>() {}.getType());
		MangoDB.createNewDocumentInCollection("remind-me-on", "reminders-snooz", data, null);
	}
}
