package com.login.EndPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.login.facade.LoginFacade;
import com.login.vo.LoginVO;
import com.login.vo.Phone;
import com.reminder.facade.ReminderFacade;

public class LoginEndpointImpl implements LoginEndpoint {
	private LoginFacade loginFacade;
	public LoginFacade getLoginFacade() {
		return loginFacade;
	}

	public void setLoginFacade(LoginFacade loginFacade) {
		this.loginFacade = loginFacade;
	}
	
	@Override
	public Response validateRegID(String regID, HttpServletRequest request, String appTimeZone) {
		try{
			HttpSession session = request.getSession();
			session.setAttribute("regID", regID);
           if (null != appTimeZone) {
        	   appTimeZone = appTimeZone.replace("@", "/");
           }
			session.setAttribute("timeZoneSettings", appTimeZone);
			LoginVO loginVO = loginFacade.validateRegID(regID,  appTimeZone);
			if (null != loginVO) {
				if (loginVO.getUserSuppliedTimeZone() != null) {
					session.setAttribute("timeZoneSettings", loginVO.getUserSuppliedTimeZone());
					
				}
				session.setAttribute("userName", loginVO.getName());
				return Response.ok().entity(loginVO).build();
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
	public Response addPhoneNo(Phone phone, HttpServletRequest request) {
		try{
			HttpSession session = request.getSession();
			String regID = (String)session.getAttribute("regID");
			return Response.ok().entity(loginFacade.addPhoneNo(phone, regID)).build();
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}
	@Override
	public Response logout(String regID) {
		try{
			
			return Response.ok().entity(loginFacade.logout(regID)).build();
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}

	@Override
	public Response getUserPhones(HttpServletRequest request) {
		try{
			HttpSession session = request.getSession();
			String regID = (String)session.getAttribute("regID");
			String email = new ReminderFacade().getEmail(regID);
			return Response.ok().entity(loginFacade.getUserPhones( email)).build(); 
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
		
	}

	@Override
	public Response deletePhone(String phoneID, HttpServletRequest request) {
		try{
			HttpSession session = request.getSession();
			String regID = (String)session.getAttribute("regID");
			String email = new ReminderFacade().getEmail(regID);
			if (phoneID.endsWith(email)) {
				loginFacade.deletePhone( phoneID);
				return Response.ok().entity(loginFacade.getUserPhones( email)).build(); 
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
	public Response sendOtp(String phoneID, HttpServletRequest request) {
		try{
			HttpSession session = request.getSession();
			String regID = (String)session.getAttribute("regID");
			String email = new ReminderFacade().getEmail(regID);
			if (phoneID.endsWith(email)) {
				String userName = (String)request.getSession().getAttribute("userName");
				if (null == userName) {
					userName = "";
				}
				loginFacade.sendOtp( phoneID, userName);
				return Response.ok().entity(new LoginVO()).build(); 
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
	public Response  confirmOTP( String phoneID,  String otp){
		try{
			return Response.ok().entity(loginFacade.confirmOPT( phoneID,otp)).build(); 
			
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}

	@Override
	public Response getPhoneViaStatus(boolean status, HttpServletRequest request) {
		try{
			HttpSession session = request.getSession();
			String regID = (String)session.getAttribute("regID");
			String email = new ReminderFacade().getEmail(regID);
			return Response.ok().entity(loginFacade.getPhoneViaStatus( email, status)).build(); 
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}

	

}
