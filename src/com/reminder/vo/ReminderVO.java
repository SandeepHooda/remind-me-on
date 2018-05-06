package com.reminder.vo;

public class ReminderVO {
	
	private String regID;
	private String date;
	private String time;
	private String reminderSubject;
	private String reminderText;
	private String repeatFrequency;
	public String getRegID() {
		return regID;
	}
	public void setRegID(String regID) {
		this.regID = regID;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getReminderSubject() {
		return reminderSubject;
	}
	public void setReminderSubject(String reminderSubject) {
		this.reminderSubject = reminderSubject;
	}
	public String getReminderText() {
		return reminderText;
	}
	public void setReminderText(String reminderText) {
		this.reminderText = reminderText;
	}
	public String getRepeatFrequency() {
		return repeatFrequency;
	}
	public void setRepeatFrequency(String repeatFrequency) {
		this.repeatFrequency = repeatFrequency;
	}

}
