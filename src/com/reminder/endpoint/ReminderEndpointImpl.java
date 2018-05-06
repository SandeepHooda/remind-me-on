package com.reminder.endpoint;

import javax.ws.rs.core.Response;

import com.reminder.facade.ReminderFacade;
import com.reminder.vo.ReminderVO;

public class ReminderEndpointImpl implements ReminderEndpoint{
	
	private ReminderFacade reminderFacade;

	@Override
	public Response addReminder(ReminderVO reminderVO) {
		try{
			
			return Response.ok().entity(reminderFacade.addReminder(reminderVO)).build();
		}catch(Exception e){
			return Response.serverError().entity("Internal Server error").build();
		}
	}

	public ReminderFacade getReminderFacade() {
		return reminderFacade;
	}

	public void setReminderFacade(ReminderFacade reminderFacade) {
		this.reminderFacade = reminderFacade;
	}

}
