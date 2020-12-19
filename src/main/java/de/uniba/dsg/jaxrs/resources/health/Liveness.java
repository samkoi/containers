package de.uniba.dsg.jaxrs.resources.health;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/live")
public class Liveness {

    @GET
    public Response checkLiveness() {
        System.out.println("Inside Liveness checkReadiness call");
        return Response.ok().build();
    }
}
