package com.login.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
public interface LoginEndpoint {
	
	@GET
	@Path("/login/validate/{regID}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response validateRegID(@PathParam("regID") String regID);
	
	@GET
	@Path("/logout/{regID}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response logout(@PathParam("regID") String regID);

}
