package com.reminder.endpoint;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.reminder.vo.ReminderVO;

public interface ReminderEndpoint {
	@POST
	@Path("/reminder")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response addReminder( ReminderVO reminderVO,  @Context HttpServletRequest request);
	
	@GET
	@Path("/reminder")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getReminders( @Context HttpServletRequest request);
	
	@DELETE
	@Path("/reminder/reminderID/{reminderID}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response deleteReminder( @PathParam("reminderID") String reminderID, @Context HttpServletRequest request);

}
