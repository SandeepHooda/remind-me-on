package com.login.EndPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import com.login.facade.LoginFacade;
import com.login.vo.LoginVO;
import com.login.vo.Phone;

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

	

}
