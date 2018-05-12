package com.reminder.vo;

public class ReminderVO {
	private String _id;
	private String regID;
	private String email;
	private String date;
	private String time;
	private String reminderSubject;
	private String reminderText;
	private String repeatFrequency;
	private boolean makeACall;
	private boolean sendText;
	private String frequencyWithDate; //Once , Monthly, yearly
	private String dayRepeatFrequency;//First Monday
	private String frequencyType = "Day";//Day or Date
	private String displayTime ;
	private long nextExecutionTime;
	private String selectedPhone;

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
	public boolean isMakeACall() {
		return makeACall;
	}
	public void setMakeACall(boolean makeACall) {
		this.makeACall = makeACall;
	}
	public boolean isSendText() {
		return sendText;
	}
	public void setSendText(boolean sendText) {
		this.sendText = sendText;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getFrequencyWithDate() {
		return frequencyWithDate;
	}
	public void setFrequencyWithDate(String frequencyWithDate) {
		this.frequencyWithDate = frequencyWithDate;
	}
	public String getDayRepeatFrequency() {
		return dayRepeatFrequency;
	}
	public void setDayRepeatFrequency(String dayRepeatFrequency) {
		this.dayRepeatFrequency = dayRepeatFrequency;
	}
	public String getFrequencyType() {
		return frequencyType;
	}
	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDisplayTime() {
		return displayTime;
	}
	public void setDisplayTime(String displayTime) {
		this.displayTime = displayTime;
	}
	public long getNextExecutionTime() {
		return nextExecutionTime;
	}
	public void setNextExecutionTime(long nextExecutionTime) {
		this.nextExecutionTime = nextExecutionTime;
	}
	public String getSelectedPhone() {
		return selectedPhone;
	}
	public void setSelectedPhone(String selectedPhone) {
		this.selectedPhone = selectedPhone;
	}

}
