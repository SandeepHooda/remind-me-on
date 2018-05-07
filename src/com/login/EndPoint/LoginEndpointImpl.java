package com.login.EndPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import com.login.facade.LoginFacade;

public class LoginEndpointImpl implements LoginEndpoint {
	private LoginFacade loginFacade;
	public LoginFacade getLoginFacade() {
		return loginFacade;
	}

	public void setLoginFacade(LoginFacade loginFacade) {
		this.loginFacade = loginFacade;
	}
	
	@Override
	public Response validateRegID(String regID, HttpServletRequest request) {
		try{
			HttpSession session = request.getSession();
			session.setAttribute("regID", regID);
			return Response.ok().entity(loginFacade.validateRegID(regID)).build();
		}catch(Exception e){
			return Response.serverError().entity("Internal Server error").build();
		}
	}

	@Override
	public Response logout(String regID) {
		try{
			
			return Response.ok().entity(loginFacade.logout(regID)).build();
		}catch(Exception e){
			return Response.serverError().entity("Internal Server error").build();
		}
	}

}
