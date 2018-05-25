package com.login.EndPoint;

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

import com.login.vo.LatLang;
import com.login.vo.Phone;


@Path("")
public interface LoginEndpoint {
	
	@GET
	@Path("/login/validate/{regID}/timeZone/{timeZone}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response validateRegID(@PathParam("regID") String regID, @Context HttpServletRequest request, @PathParam("timeZone") String timeZone);
	
	@GET
	@Path("/callcredits/regid/{regID}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getCallCredits(@PathParam("regID") String regID, @Context HttpServletRequest request);
	
	@GET
	@Path("/logout/{regID}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response logout(@PathParam("regID") String regID,  @Context HttpServletRequest request);
	
	@POST
	@Path("/phone")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response addPhoneNo( Phone phone,  @Context HttpServletRequest request);
	
	@GET
	@Path("/phone")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getUserPhones(  @Context HttpServletRequest request);
	
	@GET
	@Path("/phone/verified/{status}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getPhoneViaStatus(@PathParam("status") boolean status , @Context HttpServletRequest request);
	
	@DELETE
	@Path("/phone/phoneID/{phoneID}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response deletePhone( @PathParam("phoneID") String phoneID, @Context HttpServletRequest request);
	
	@GET
	@Path("/phone/verify/ID/{phoneID}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response sendOtp( @PathParam("phoneID") String phoneID, @Context HttpServletRequest request);
	
	@GET
	@Path("/phone/phoneID/{phoneID}/confirmOtp/{otp}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response confirmOTP( @PathParam("phoneID") String phoneID, @PathParam("otp") String otp);
	
	
	@POST
	@Path("/feedback")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response feedback( String feedback,  @Context HttpServletRequest request);
	
	@GET
	@Path("/recordLoginSucess")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response recordLoginSucess( @Context HttpServletRequest request);
	
	@POST
	@Path("/updatePreciseLocation")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response updatePreciseLocation(LatLang latLang , @Context HttpServletRequest request);
	

}
