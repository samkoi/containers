package de.uniba.dsg.jaxrs.resources;


import de.uniba.dsg.jaxrs.model.dto.BottlePostDTO;
import de.uniba.dsg.jaxrs.model.dto.CratePostDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;

@Path("beverage")
public class BeverageManagementResource {



	Client client = ClientBuilder.newClient();

	private static final Log logger = LogFactory.getLog(BeverageManagementResource.class);

	/*
	 * Get All Beverages
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBeverages(@Context final UriInfo info,
			@QueryParam("pageLimit") @DefaultValue("10") final int pageLimit,
			@QueryParam("page") @DefaultValue("1") final int page, @QueryParam("search") final String searchTerm,
			@QueryParam("filter") final FilterFunction filterFunction) {
		logger.info("Get all Beverages. Pagination parameter: page-" + page + " pageLimit-" + pageLimit);

		WebTarget target = client.target("http://db:9999/v1/beverage")//
				.queryParam("pageLimit", pageLimit)//
				.queryParam("page", page)//
				.queryParam("search", searchTerm)//
				.queryParam("filter", filterFunction);

		return target.request(MediaType.APPLICATION_JSON).get(Response.class);
	}

	/*
	 * Get Bottle by id
	 */
	@GET
	@Path("/bottle/{bottleId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response getBottleById(@Context final UriInfo info, @PathParam("bottleId") final int id) {
		logger.info("Get bottle with id " + id);
		WebTarget target = client.target("http://db:9999/v1/beverage/bottle/{bottleId}").resolveTemplate("bottleId", id);
		return target.request(MediaType.APPLICATION_JSON).get();

	}

	/*
	 * Get all (only) bottles from db Implements pagination Search/Filter is
	 * available with GET /beverages so not implementing them again
	 */
	@GET
	@Path("/bottle")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllBottles(@Context final UriInfo info,
			@QueryParam("pageLimit") @DefaultValue("10") final int pageLimit,
			@QueryParam("page") @DefaultValue("1") final int page) {

		WebTarget target = client.target("http://db:9999/v1/beverage/bottle").queryParam("pageLimit", pageLimit)
				.queryParam("page", page);

		return target.request(MediaType.APPLICATION_JSON).get();
	}

	/*
	 * Get all (only) crates from db Implements pagination Search/Filter is
	 * available with GET /beverages so not implementing them again
	 */
	@GET
	@Path("/crate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCrates(@Context final UriInfo info,
			@QueryParam("pageLimit") @DefaultValue("10") final int pageLimit,
			@QueryParam("page") @DefaultValue("1") final int page) {
		WebTarget target = client.target("http://db:9999/v1/beverage/crate").queryParam("pageLimit", pageLimit)
				.queryParam("page", page);

		return target.request(MediaType.APPLICATION_JSON).get();
	}

	/*
	 * Get Crate by id
	 */
	@GET
	@Path("/crate/{crateId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response getCrateById(@Context final UriInfo info, @PathParam("crateId") final int id) {
		logger.info("Get crate with id " + id);

		WebTarget target = client.target("http://db:9999/v1/beverage/crate/{crateId}")//
				.resolveTemplate("crateId", id);
		target.getUriBuilder().uri(info.getBaseUri());
		return target.request(MediaType.APPLICATION_JSON).get();
	}


	
	/*
	 * Create Bottle
	 */
	@POST
	@Path("/bottle")
	public Response createBottle(final BottlePostDTO newBeverage, @Context final UriInfo uriInfo) {
		logger.info("Create beverage " + newBeverage);
		
		WebTarget target = client.target("http://db:9999/v1/beverage/bottle");

		return target.request().post(Entity.json(newBeverage));
	}
	
	/*
	 * Create Crate
	 */
	@POST
	@Path("/crate")
	public Response createCrate(final CratePostDTO newBeverage, @Context final UriInfo uriInfo) {
		logger.info("Create beverage " + newBeverage);

		WebTarget target = client.target("http://db:9999/v1/beverage/crate");
		target.register(uriInfo);
		return target.request().post(Entity.json(newBeverage));
	}
	
	/*
	 * update a Bottle
	 */
	@PUT
	@Path("bottle/{bottle-id}")
	public Response updateBottle(@Context final UriInfo uriInfo, @PathParam("bottle-id") final int id,
			final BottlePostDTO updatedBeverage) {
		logger.info("Update bottle " + updatedBeverage);
		WebTarget target = client.target("http://db:9999/v1/beverage/bottle/{bottle-id}").resolveTemplate("bottle-id", id);

		return target.request().put(Entity.json(updatedBeverage));
	}
	
	/*
	 * update a Crate
	 */
	@PUT
	@Path("crate/{crate-id}")
	public Response updateCrate(@Context final UriInfo uriInfo, @PathParam("crate-id") final int id,
			final CratePostDTO updatedBeverage) {
		logger.info("Update crate " + updatedBeverage);
		WebTarget target = client.target("http://db:9999/v1/beverage/crate/{crate-id}").resolveTemplate("crate-id", id);
		
		return target.request().put(Entity.json(updatedBeverage));
	}
	
	/*
	 * Delete Bottle
	 */
	@DELETE
    @Path("bottle/{bottleId}")
    public Response deleteSpecificBottle(@PathParam("bottleId") final int id) {
        logger.info("Delete bottle with id " + id);
        WebTarget target = client.target("http://db:9999/v1/beverage/bottle/{bottle-id}").resolveTemplate("bottle-id", id);

		return target.request().delete();
    }
	
	/*
	 * Delete Crate
	 */
	@DELETE
    @Path("crate/{crateId}")
    public Response deleteSpecificCrate(@PathParam("crateId") final int id) {
        logger.info("Delete crate with id " + id);
        WebTarget target = client.target("http://db:9999/v1/beverage/crate/{crate-id}").resolveTemplate("crate-id", id);

		return target.request().delete();
    }

}
