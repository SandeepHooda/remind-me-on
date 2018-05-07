package com.reminder.endpoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
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

	

	@Override
	public Response getReminders(HttpServletRequest request) {
		try{
			HttpSession session = request.getSession();
			String regID = (String)session.getAttribute("regID");
			if (null != regID) {
				return Response.ok().entity(reminderFacade.getReminders(regID)).build();
			}else {
				return Response.status(Response.Status.UNAUTHORIZED).entity("Please log in to authenticate ").build();
			}
			
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
