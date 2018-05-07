package com.reminder.vo;

public enum DayRepeatFrequency {
	EVERY_MONDAY("Every Monday"),
	EVERY_TUESDAY("Every Tuesday"),
	EVERY_WEDNESDAY("Every Wednesday"),
	EVERY_THRUSDAY("Every Thrusday"),
	EVERY_FRIDAY("Every Friday"),
	EVERY_SATURDAY("Every Saturday"),
	EVERY_SUNDAY("Every Sunday"),
	EVERY_WEEKDAY("Every WeekDay"),
	
	FIRST_MONDAY("First Monday"),
	FIRST_TUESDAY("First Tuesday"),
	FIRST_WEDNESDAY("First Wednesday"),
	FIRST_THRUSDAY("First Thrusday"),
	FIRST_FRIDAY("First Friday"),
	FIRST_SATURDAY("First Saturday"),
	FIRST_SUNDAY("First Sunday"),
	
	SECOND_MONDAY("Second Monday"),
	SECOND_TUESDAY("Second Tuesday"),
	SECOND_WEDNESDAY("Second Wednesday"),
	SECOND_THRUSDAY("Second Thrusday"),
	SECOND_FRIDAY("Second Friday"),
	SECOND_SATURDAY("Second Saturday"),
	SECOND_SUNDAY("Second Sunday"),
	
	THIRD_MONDAY("Third Monday"),
	THIRD_TUESDAY("Third Tuesday"),
	THIRD_WEDNESDAY("Third Wednesday"),
	THIRD_THRUSDAY("Third Thrusday"),
	THIRD_FRIDAY("Third Friday"),
	THIRD_SATURDAY("Third Saturday"),
	THIRD_SUNDAY("Third Sunday"),
	
	FOURTH_MONDAY("Fourth Monday"),
	FOURTH_TUESDAY("Fourth Tuesday"),
	FOURTH_WEDNESDAY("Fourth Wednesday"),
	FOURTH_THRUSDAY("Fourth Thrusday"),
	FOURTH_FRIDAY("Fourth Friday"),
	FOURTH_SATURDAY("Fourth Saturday"),
	FOURTH_SUNDAY("Fourth Sunday");
	
	private String fequency ;
	private DayRepeatFrequency(String fequency) {
		this.fequency = fequency;
	}
	public String getFequency() {
		return fequency;
	}
	public void setFequency(String fequency) {
		this.fequency = fequency;
	}
	

}
