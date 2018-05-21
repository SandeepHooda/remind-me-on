package com.reminder.endpoint;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.reminder.vo.ReminderVO;
import com.reminder.vo.ToDO;

public interface ReminderEndpoint {
	@POST
	@Path("/reminder")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response addReminder( ReminderVO reminderVO,  @Context HttpServletRequest request);
	
	@PUT
	@Path("/reminder")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response updateReminder( ReminderVO reminderVO,  @Context HttpServletRequest request);
	
	@GET
	@Path("/reminder")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getReminders( @Context HttpServletRequest request);
	
	@DELETE
	@Path("/reminder/reminderID/{reminderID}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response deleteReminder( @PathParam("reminderID") String reminderID, @Context HttpServletRequest request);
	
	@POST
	@Path("/todo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response addToDo( ToDO todo,  @Context HttpServletRequest request);
	
	@POST
	@Path("/todo/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response updateToDo( ToDO todo,  @Context HttpServletRequest request);
	
	@GET
	@Path("/todo")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getToDos( @Context HttpServletRequest request);
	
	@DELETE
	@Path("/todo/id/{toDoID}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response markComplete( @PathParam("toDoID") String toDoID, @Context HttpServletRequest request);
	
	@GET
	@Path("/snoozed/reminder")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getSnoozedReminders( @Context HttpServletRequest request);
	
	@DELETE
	@Path("/snoozed/reminder/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response deleteSnoozedReminder( @PathParam("id") String id, @Context HttpServletRequest request);

}
