package de.uniba.dsg.jaxrs.resources.health;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/ready")
public class Readiness {

    @GET
    public Response checkReadiness() {
       System.out.println("Inside Readiness checkReadiness call");
       return Response.ok().build();
    }
}
