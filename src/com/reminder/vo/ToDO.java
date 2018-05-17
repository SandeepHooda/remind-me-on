package com.reminder.vo;

public class ToDO {
	private String _id;
	private long dateCreated;
	private long dateCompleted;
	private String email;
	private String taskDesc;
	private boolean complete;
	private int order;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTaskDesc() {
		return taskDesc;
	}
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
	public boolean isComplete() {
		return complete;
	}
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public long getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}
	public long getDateCompleted() {
		return dateCompleted;
	}
	public void setDateCompleted(long dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

}
