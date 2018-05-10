package com.reminder.facade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.vo.LoginVO;
import com.reminder.vo.ReminderVO;

import mangodb.MangoDB;

public class ReminderFacade {
	public static String timeFormat = "yyyy_M_d HH_mm";
	public boolean addReminder(ReminderVO reminderVO, String timeZone) throws ParseException {
		
		 Gson  json = new Gson();
         
         String email = getEmail(reminderVO.getRegID());
         if (null != email) {
        	 reminderVO.setEmail(email);
        	 SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        	 TimeZone userTimeZone	=	TimeZone.getTimeZone(timeZone);
        	 System.out.println(" User timezone "+timeZone);
     		sdf.setTimeZone(userTimeZone);
     		Date reminderDate = sdf.parse(nextReminder(reminderVO));
     		reminderVO.setNextExecutionTime(reminderDate.getTime());
     		if (reminderVO.get_id() == null) {
     			reminderVO.set_id(""+reminderVO.getNextExecutionTime()+Math.random());
     		}
        	 String data = json.toJson(reminderVO, new TypeToken<ReminderVO>() {}.getType());
        	 MangoDB.createNewDocumentInCollection("remind-me-on", "reminders", data, null);
     		return true;
         }else {
        	 return false;
         }
         
	}
	
	private String nextReminder(ReminderVO reminderVO) {
		if ("Monthly".equalsIgnoreCase(reminderVO.getFrequencyWithDate())) {
			String[] dateSplit = reminderVO.getDate().split("_");
			Calendar cal = new GregorianCalendar();
			cal.set(Calendar.DATE, Integer.parseInt(dateSplit[2]));
			{//If date is 31 then and this month don't have 31 then cals months needs to be adjusted
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));// This actually sets correct next month 
			}
			if (cal.getTime().getTime() < new Date().getTime()){//Reminder date has past in this month
				cal.add(Calendar.MONTH, 1);
			}
			return ""+cal.get(Calendar.YEAR)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+cal.get(Calendar.DATE)+" "+reminderVO.getTime();
		}
		return reminderVO.getDate()+" "+reminderVO.getTime();
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
