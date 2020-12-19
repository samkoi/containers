package de.uniba.dsg.jaxrs.resources;


import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;




@Path("beverage")
public class BeverageCustomerResource {

	Client client = ClientBuilder.newClient();

	private static final Log logger = LogFactory.getLog(BeverageCustomerResource.class);

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
		return target.request(MediaType.APPLICATION_JSON).get();
	}

}
