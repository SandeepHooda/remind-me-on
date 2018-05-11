package com.reminder.endpoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import com.login.vo.LoginVO;
import com.reminder.facade.ReminderFacade;
import com.reminder.vo.ReminderVO;

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
	public ReminderFacade getReminderFacade() {
		return reminderFacade;
	}

	public void setReminderFacade(ReminderFacade reminderFacade) {
		this.reminderFacade = reminderFacade;
	}



	

}
