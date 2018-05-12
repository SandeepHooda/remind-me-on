package com.reminder.facade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.vo.LoginVO;
import com.reminder.vo.ReminderVO;

import mangodb.MangoDB;

public class ReminderFacade {
	public static String timeFormat = "yyyy_M_d HH_mm";
	private static Map<String, Integer> dayToCalDay = new HashMap<String, Integer>();
	private static Map<String, Integer> wordsToMath = new HashMap<String, Integer>();
	static {
		dayToCalDay.put("Monday", Calendar.MONDAY);
		dayToCalDay.put("Tuesday", Calendar.TUESDAY);
		dayToCalDay.put("Wednesday", Calendar.WEDNESDAY);
		dayToCalDay.put("Thrusday", Calendar.THURSDAY);
		dayToCalDay.put("Friday", Calendar.FRIDAY);
		dayToCalDay.put("Saturday", Calendar.SATURDAY);
		dayToCalDay.put("Sunday", Calendar.SUNDAY);
		
		wordsToMath.put("First", 1);
		wordsToMath.put("Second", 2);
		wordsToMath.put("Third", 3);
		wordsToMath.put("Fourth", 4);
	}
	public boolean addReminder(ReminderVO reminderVO, String timeZone) throws ParseException {
		
		 Gson  json = new Gson();
         
         String email = getEmail(reminderVO.getRegID());
         if (null != email) {
        	 reminderVO.setEmail(email);
        	 
     		Date reminderDate = nextReminder(reminderVO, timeZone);
     		reminderVO.setNextExecutionTime(reminderDate.getTime());
     		if (reminderVO.get_id() == null) {
     			reminderVO.set_id(""+reminderVO.getNextExecutionTime()+Math.random());
     		}
     		reminderVO.set_id(reminderVO.get_id()+"_"+email);
        	 String data = json.toJson(reminderVO, new TypeToken<ReminderVO>() {}.getType());
        	 MangoDB.createNewDocumentInCollection("remind-me-on", "reminders", data, null);
     		return true;
         }else {
        	 return false;
         }
         
	}
	
	
	public static Date nextReminder(ReminderVO reminderVO, String timeZone) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
	   	 TimeZone userTimeZone	=	TimeZone.getTimeZone(timeZone);
	   	 System.out.println(" User timezone "+timeZone);
		sdf.setTimeZone(userTimeZone);
		if ("Day".equalsIgnoreCase(reminderVO.getFrequencyType())) {
			String[] split = reminderVO.getDayRepeatFrequency().split(" ");
			Calendar cal = new GregorianCalendar();
			cal.set(Calendar.DAY_OF_WEEK, dayToCalDay.get(split[1]));
			cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, wordsToMath.get(split[0]));
		
			if (cal.getTime().getTime() < new Date().getTime()){//Reminder date has past in this month
				cal.add(Calendar.MONTH, 1);
				cal.set(Calendar.DAY_OF_WEEK, dayToCalDay.get(split[1]));
				cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, wordsToMath.get(split[0]));
			}
			return sdf.parse(""+cal.get(Calendar.YEAR)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+cal.get(Calendar.DATE)+" "+reminderVO.getTime());
		}else if ("Monthly".equalsIgnoreCase(reminderVO.getFrequencyWithDate())) {
			String[] dateSplit = reminderVO.getDate().split("_");
			Calendar cal = new GregorianCalendar();
			cal.set(Calendar.DATE, Integer.parseInt(dateSplit[2]));
			{//If date is 31 then and this month don't have 31 then cals months needs to be adjusted
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));// This actually sets correct next month 
			}
			if (cal.getTime().getTime() < new Date().getTime()){//Reminder date has past in this month
				cal.add(Calendar.MONTH, 1);
			}
			return sdf.parse(""+cal.get(Calendar.YEAR)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+cal.get(Calendar.DATE)+" "+reminderVO.getTime());
		}else if ("Yearly".equalsIgnoreCase(reminderVO.getFrequencyWithDate())) {
			String[] dateSplit = reminderVO.getDate().split("_");
			Calendar cal = new GregorianCalendar();
			cal.set(Calendar.MONTH, (Integer.parseInt(dateSplit[1])) -1);
			cal.set(Calendar.DATE, Integer.parseInt(dateSplit[2]));
			
			if (cal.getTime().getTime() < new Date().getTime()){//Reminder date has past in this month
				cal.add(Calendar.YEAR, 1);
			}
			return sdf.parse(""+cal.get(Calendar.YEAR)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+cal.get(Calendar.DATE)+" "+reminderVO.getTime());
		}
		return sdf.parse(reminderVO.getDate()+" "+reminderVO.getTime());
	}
	public List<ReminderVO> getReminders(String regID) {
		 String email = getEmail(regID);
		 if (null != email) {
			 String sortByExecutionTimeAsc =  "&s=%7B%22nextExecutionTime%22%3A%201%7D";
			 String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "reminders", email,"email", false, null,sortByExecutionTimeAsc)+"]";
			 Gson  json = new Gson();
			 List<ReminderVO> result  = json.fromJson(data, new TypeToken<List<ReminderVO>>() {}.getType());
			 //Collections.sort(result, new ReminderVOComparator());
			 return result;
		 }else {
			 return new ArrayList<ReminderVO>();
		 }
		
        
	}
	public void deleteReminder(String reminderID ) {
		
		MangoDB.deleteDocument("remind-me-on", "reminders", reminderID, null);
			
		
       
	}
	
	public String getEmail(String regID) {
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
