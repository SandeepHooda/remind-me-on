package com.reminder.vo;

import java.util.Comparator;

public class ReminderVOComparator implements Comparator<ReminderVO>{

	@Override
	public int compare(ReminderVO o1, ReminderVO o2) {
		if (o1.getNextExecutionTime() - o2.getNextExecutionTime() > 0) {
			return 1;
		}else {
			return -1;
		}
		
	}

}
