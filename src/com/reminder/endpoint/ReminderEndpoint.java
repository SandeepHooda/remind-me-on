package com.reminder.endpoint;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.reminder.vo.ReminderVO;

public interface ReminderEndpoint {
	@POST
	@Path("/reminder")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response addReminder( ReminderVO reminderVO);

}
