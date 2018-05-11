package com.login.EndPoint;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.login.vo.Phone;


@Path("")
public interface LoginEndpoint {
	
	@GET
	@Path("/login/validate/{regID}/timeZone/{timeZone}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response validateRegID(@PathParam("regID") String regID, @Context HttpServletRequest request, @PathParam("timeZone") String timeZone);
	
	@GET
	@Path("/logout/{regID}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response logout(@PathParam("regID") String regID);
	
	@POST
	@Path("/phone")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response addPhoneNo( Phone phone,  @Context HttpServletRequest request);

}
