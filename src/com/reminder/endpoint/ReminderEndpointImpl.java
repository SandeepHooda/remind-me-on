package com.reminder.endpoint;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

import com.login.vo.LoginVO;
import com.reminder.facade.ReminderFacade;
import com.reminder.vo.ReminderVO;
import com.reminder.vo.ToDO;

import mangodb.MangoDB;

public class ReminderEndpointImpl implements ReminderEndpoint{
	
	private ReminderFacade reminderFacade;
	

	@Override
	public Response addReminder(ReminderVO reminderVO, HttpServletRequest request) {
		try{
			String timeZone = (String)request.getSession().getAttribute("timeZoneSettings");
			if(timeZone == null) {
				LoginVO vo = new LoginVO();
				vo.setErrorMessage("Please log in to authenticate ");
				return Response.status(Response.Status.UNAUTHORIZED).entity(vo).build();
			}
			return Response.ok().entity(reminderFacade.addReminder(reminderVO,timeZone )).build();
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}
	
	@Override
	public Response updateReminder(ReminderVO reminderVO, HttpServletRequest request) {
		try{
			HttpSession session = request.getSession();
			String email = (String)session.getAttribute("email");
			if (!reminderVO.getEmail().equalsIgnoreCase(email)) {
				LoginVO vo = new LoginVO();
				vo.setErrorMessage("Please log in to authenticate ");
				return Response.status(Response.Status.UNAUTHORIZED).entity(vo).build();
			}
			return Response.ok().entity(reminderFacade.updateReminder(reminderVO )).build();
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
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
				LoginVO vo = new LoginVO();
				vo.setErrorMessage("Please log in to authenticate ");
				return Response.status(Response.Status.UNAUTHORIZED).entity(vo).build();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}
	
	@Override
	public Response deleteReminder(String reminderID, HttpServletRequest request) {
		try{
			HttpSession session = request.getSession();
			String regID = (String)session.getAttribute("regID");
			if (null != regID) {
				reminderFacade.deleteReminder(reminderID);
				return Response.ok().entity(reminderFacade.getReminders(regID)).build();
			}else {
				LoginVO vo = new LoginVO();
				vo.setErrorMessage("Please log in to authenticate ");
				return Response.status(Response.Status.UNAUTHORIZED).entity(vo).build();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}
	
	@Override
	public Response deleteSnoozedReminder(String reminderID, HttpServletRequest request) {
		try{
			HttpSession session = request.getSession();
			String email = (String)session.getAttribute("email");
			if (null != email) {
				reminderFacade.deleteSnoozedReminder(reminderID);
				return getSnoozedReminders(request);
			}else {
				LoginVO vo = new LoginVO();
				vo.setErrorMessage("Please log in to authenticate ");
				return Response.status(Response.Status.UNAUTHORIZED).entity(vo).build();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}
	
	@Override
	public Response addToDo( ToDO todo,  HttpServletRequest request) {
		try{
			HttpSession session = request.getSession();
			String email = (String)session.getAttribute("email");
			if (null != email) {
				todo.setDateCreated(new Date().getTime());
				todo.set_id(""+todo.getDateCreated()+"_"+email);
				todo.setEmail(email);
				return Response.ok().entity(reminderFacade.addToDo(todo)).build();
			}else {
				LoginVO vo = new LoginVO();
				vo.setErrorMessage("Please log in to authenticate ");
				return Response.status(Response.Status.UNAUTHORIZED).entity(vo).build();
			}
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}
	@Override
	public Response updateToDo( ToDO todo,  HttpServletRequest request) {
		try{
			HttpSession session = request.getSession();
			String email = (String)session.getAttribute("email");
			if (null != email) {
				return Response.ok().entity(reminderFacade.updateToDo(todo)).build();
			}else {
				LoginVO vo = new LoginVO();
				vo.setErrorMessage("Please log in to authenticate ");
				return Response.status(Response.Status.UNAUTHORIZED).entity(vo).build();
			}
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}
	
	@Override
	public Response getToDos( HttpServletRequest request) {
		try{
			HttpSession session = request.getSession();
			String email = (String)session.getAttribute("email");
			if (null != email) {
				return Response.ok().entity(reminderFacade.getToDos(email)).build();
			}else {
				LoginVO vo = new LoginVO();
				vo.setErrorMessage("Please log in to authenticate ");
				return Response.status(Response.Status.UNAUTHORIZED).entity(vo).build();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}
	
	@Override
	public Response markComplete( String toDoID, HttpServletRequest request) {
		try{
			HttpSession session = request.getSession();
			String regID = (String)session.getAttribute("regID");
			if (null != regID) {
				reminderFacade.markComplete(toDoID);
				return getToDos(request);
			}else {
				LoginVO vo = new LoginVO();
				vo.setErrorMessage("Please log in to authenticate ");
				return Response.status(Response.Status.UNAUTHORIZED).entity(vo).build();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}
	
	public ReminderFacade getReminderFacade() {
		return reminderFacade;
	}

	public void setReminderFacade(ReminderFacade reminderFacade) {
		this.reminderFacade = reminderFacade;
	}



	@Override
	public Response getSnoozedReminders(HttpServletRequest request) {
		try{
			HttpSession session = request.getSession();
			String email = (String)session.getAttribute("email");
			if (null != email) {
				return Response.ok().entity(reminderFacade.getSnoozedReminders(email)).build();
			}else {
				LoginVO vo = new LoginVO();
				vo.setErrorMessage("Please log in to authenticate ");
				return Response.status(Response.Status.UNAUTHORIZED).entity(vo).build();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}



	

}
