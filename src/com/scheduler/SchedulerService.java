package com.scheduler;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.communication.email.EmailAddess;
import com.communication.email.EmailVO;
import com.communication.email.MailService;
import com.communication.phone.text.Key;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.vo.Settings;
import com.reminder.facade.ReminderFacade;
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
			boolean oneTimeReminder = "Once".equalsIgnoreCase(reminderVO.getFrequencyWithDate());
			//Notify user
			notifyUser(reminderVO, oneTimeReminder);
			
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
					MangoDB.createNewDocumentInCollection("remind-me-on", "reminders", data, null);
					//MangoDB.createNewDocumentInCollection("remind-me-on", "reminders", data, null);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
	private static void notifyUser(ReminderVO reminderVO, boolean oneTimeReminder) {
		//Send email
		try {
			//After notification delete the reminder
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
			MailService.sendSimpleMail(emalVO);
			if (oneTimeReminder) {
				MangoDB.deleteDocument("remind-me-on", "reminders", reminderVO.get_id(), null);
			}
				
		
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void snoozReminders(List<ReminderVO> currentReminders) {
		 Gson  json = new Gson();
		 String data = json.toJson(currentReminders, new TypeToken<List<ReminderVO>>() {}.getType());
		MangoDB.createNewDocumentInCollection("remind-me-on", "reminders-snooz", data, null);
	}
}
