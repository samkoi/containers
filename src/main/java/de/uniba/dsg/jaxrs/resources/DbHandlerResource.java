package de.uniba.dsg.jaxrs.resources;


import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import de.uniba.dsg.jaxrs.controller.BeverageService;
import de.uniba.dsg.jaxrs.model.api.PaginatedBeverages;
import de.uniba.dsg.jaxrs.model.dto.BeverageDTO;
import de.uniba.dsg.jaxrs.model.dto.BottlePostDTO;
import de.uniba.dsg.jaxrs.model.dto.CratePostDTO;
import de.uniba.dsg.jaxrs.model.error.ErrorMessage;
import de.uniba.dsg.jaxrs.model.error.ErrorType;
import de.uniba.dsg.jaxrs.model.logic.Beverage;
import de.uniba.dsg.jaxrs.model.logic.Bottle;
import de.uniba.dsg.jaxrs.model.logic.Crate;


@Path("beverage")
public class DbHandlerResource {


	private static final Log logger = LogFactory.getLog(DbHandlerResource.class);
	/*
	 * Get All Beverages
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBeverages(@Context final UriInfo info,
			@QueryParam("pageLimit") @DefaultValue("10") final int pageLimit,
			@QueryParam("page") @DefaultValue("1") final int page,
			@QueryParam("search") final String searchTerm,
			@QueryParam("filter") final FilterFunction filterFunction) {
		logger.info("Get all Beverages. Pagination parameter: page-" + page + " pageLimit-" + pageLimit);

		if (page < 1 || page > pageLimit) {
			return createPageErrorResponse(pageLimit, page);
		}

		final PaginationHelper<Beverage> helper = new PaginationHelper<>(BeverageService.instance.getBeverages(searchTerm, filterFunction));
		final PaginatedBeverages response = new PaginatedBeverages(helper.getPagination(info, page, pageLimit),
				BeverageDTO.marshall(helper.getPaginatedList(), info.getBaseUri()), info.getRequestUri());
		return Response.ok(response).build();
	}


	/*
	 * Get Bottle by id
	 */
	@GET
	@Path("/bottle/{bottleId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response getBottleById(@Context final UriInfo info, @PathParam("bottleId") final int id) {
		logger.info("Get bottle with id " + id);
		final Bottle bottle = BeverageService.instance.getBottleById(id);
		if (bottle == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		BeverageDTO beverageDTO = new BeverageDTO(bottle.getId(), bottle.getPrice(), bottle.getInStock(), bottle.getName(), bottle.getVolume(), bottle.isAlcoholic(), bottle.getVolumePercent(), bottle.getSupplier(), info.getBaseUri());
		return Response.ok(beverageDTO).build();
	}

	/*
	* Get all (only) bottles from db
	* Implements pagination
	* Search/Filter is available with GET /beverages
	* so not implementing them again
	 */
	@GET
	@Path("/bottle")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllBottles(@Context final UriInfo info,
								 @QueryParam("pageLimit") @DefaultValue("10") final int pageLimit,
								 @QueryParam("page") @DefaultValue("1") final int page) {

		if (page < 1 || page > pageLimit) {
			return createPageErrorResponse(pageLimit, page);
		}

		final PaginationHelper<Beverage> helper = new PaginationHelper<>(BeverageService.instance.getAllBottles());
		final PaginatedBeverages response = new PaginatedBeverages(helper.getPagination(info, page, pageLimit),
				BeverageDTO.marshall(helper.getPaginatedList(),info.getBaseUri()), info.getRequestUri());
		return Response.ok(response).build();
	}

	/*
	 * Get all (only) crates from db
	 * Implements pagination
	 * Search/Filter is available with GET /beverages
	 * so not implementing them again
	 */
	@GET
	@Path("/crate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCrates(@Context final UriInfo info,
								  @QueryParam("pageLimit") @DefaultValue("10") final int pageLimit,
								  @QueryParam("page") @DefaultValue("1") final int page) {

		if (page < 1 || page > pageLimit) {
			return createPageErrorResponse(pageLimit, page);
		}

		final PaginationHelper<Beverage> helper = new PaginationHelper<>(BeverageService.instance.getAllCrates());
		final PaginatedBeverages response = new PaginatedBeverages(helper.getPagination(info, page, pageLimit),
				BeverageDTO.marshall(helper.getPaginatedList(),info.getBaseUri()), info.getRequestUri());
		return Response.ok(response).build();
	}
	
	/*
	 * Get Crate by id
	 */
	@GET
	@Path("/crate/{crateId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public Response getCrateById(@Context final UriInfo info, @PathParam("crateId") final int id) {
		logger.info("Get crate with id " + id);
		final Crate crate = BeverageService.instance.getCrateById(id);

		if (crate == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		BeverageDTO beverageDTO = new BeverageDTO(crate.getId(),//
				crate.getPrice(),//
				crate.getInStock(),//
				crate.getBottle().getName(),//
				crate.getBottle().getVolume(),//
				crate.isAlcoholic(),//
				crate.getVolumePercent(),//
				crate.getBottle().getSupplier(),//
				crate.getBottle().getId(),//
				crate.getNoOfBottles(),//
				info.getBaseUri());
		return Response.ok(beverageDTO).build();
	}
	
	/*
	 * Create Bottle
	 */
	@POST
	@Path("/bottle")
	public Response createBottle(final BottlePostDTO newBeverage, @Context final UriInfo uriInfo) {
		logger.info("Create beverage " + newBeverage);
		if (newBeverage == null) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorMessage(ErrorType.INVALID_PARAMETER, "Body was empty")).build();
		}
		newBeverage.setId(BeverageService.instance.getAndIncrementBeverageID());
		final Bottle bottle = newBeverage.unmarshall();
		BeverageService.instance.addBottle(bottle);

		return Response.created(UriBuilder.fromUri(uriInfo.getBaseUri()).path(BeverageManagementResource.class)
				.path(BeverageManagementResource.class, "getBottleById").build(bottle.getId())).build();
	}
	
	/*
	 * Create Crate
	 */
	@POST
	@Path("/crate")
	public Response createCrate(final CratePostDTO newBeverage, @Context final UriInfo uriInfo) {
		logger.info("Create beverage " + newBeverage);
		if (newBeverage == null) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorMessage(ErrorType.INVALID_PARAMETER, "Body was empty")).build();
		}
		
		int id = BeverageService.instance.getAndIncrementBeverageID();
		Bottle bottle = BeverageService.instance.getBottleById(newBeverage.getBottleId());
		if (bottle == null) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorMessage(ErrorType.INVALID_PARAMETER, "no matching Bottle for Bottle ID")).build();
		}
		Crate crate = new Crate(id, bottle, newBeverage.getNoOfBottles(), newBeverage.getPrice(), newBeverage.getInStock());
		BeverageService.instance.addCrate(crate);


		return Response.created(UriBuilder.fromUri(uriInfo.getBaseUri()).path(BeverageManagementResource.class)
				.path(BeverageManagementResource.class, "getCrateById").build(crate.getId())).build();
	}
	
	/*
	 * update a Bottle
	 */
	@PUT
	@Path("bottle/{bottle-id}")
	public Response updateBottle(@Context final UriInfo uriInfo, @PathParam("bottle-id") final int id,
			final BottlePostDTO updatedBeverage) {
		logger.info("Update bottle " + updatedBeverage);
		if (updatedBeverage == null) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorMessage(ErrorType.INVALID_PARAMETER, "Body was empty")).build();
		}

		final Bottle bottle = BeverageService.instance.getBottleById(id);
		if (bottle == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		updatedBeverage.setId(id);
		final Bottle resultBottle = BeverageService.instance.updateBottle(id, updatedBeverage.unmarshall());

		BeverageDTO beverageDTO = new BeverageDTO(resultBottle.getId(), resultBottle.getPrice(), resultBottle.getInStock(), resultBottle.getName(), resultBottle.getVolume(), resultBottle.isAlcoholic(), resultBottle.getVolumePercent(), resultBottle.getSupplier(), uriInfo.getBaseUri());
		return Response.ok().entity(beverageDTO).build();
	}
	
	/*
	 * update a Crate
	 */
	@PUT
	@Path("crate/{crate-id}")
	public Response updateCrate(@Context final UriInfo uriInfo, @PathParam("crate-id") final int id,
			final CratePostDTO updatedBeverage) {
		logger.info("Update crate " + updatedBeverage);
		if (updatedBeverage == null) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorMessage(ErrorType.INVALID_PARAMETER, "Body was empty")).build();
		}
		updatedBeverage.setId(id);
		final Crate crate = BeverageService.instance.getCrateById(id);
		final Bottle bottle = BeverageService.instance.getBottleById(updatedBeverage.getBottleId());
		if (crate == null || bottle == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		final Crate resultCrate = BeverageService.instance.updateCrate(id, updatedBeverage.unmarshall(bottle));
		BeverageDTO beverageDTO = new BeverageDTO(resultCrate.getId(),//
				resultCrate.getPrice(),//
				resultCrate.getInStock(),//
				resultCrate.getBottle().getName(),//
				resultCrate.getBottle().getVolume(),//
				resultCrate.isAlcoholic(),//
				resultCrate.getVolumePercent(),//
				resultCrate.getBottle().getSupplier(),//
				resultCrate.getBottle().getId(),//
				resultCrate.getNoOfBottles(),//
				uriInfo.getBaseUri());
		return Response.ok().entity(beverageDTO).build();
	}
	
	/*
	 * Delete Bottle
	 */
	@DELETE
    @Path("bottle/{bottleId}")
    public Response deleteSpecificBottle(@PathParam("bottleId") final int id) {
        logger.info("Delete bottle with id " + id);
        final boolean deleted = BeverageService.instance.deleteBottle(id);

        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok().build();
        }
    }
	
	/*
	 * Delete Crate
	 */
	@DELETE
    @Path("crate/{crateId}")
    public Response deleteSpecificCrate(@PathParam("crateId") final int id) {
        logger.info("Delete crate with id " + id);
        final boolean deleted = BeverageService.instance.deleteCrate(id);

        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok().build();
        }
    }
	
	private Response createPageErrorResponse(final int pageLimit, final int page) {
		final ErrorMessage errorMessage;
		if(pageLimit <1) {
			errorMessage = new ErrorMessage(ErrorType.INVALID_PARAMETER,
					"PageLimitis less than 1. Read the documentation for a proper handling!");
		}else if (page < 1) {
			errorMessage = new ErrorMessage(ErrorType.INVALID_PARAMETER,
					"Page less than 1. Read the documentation for a proper handling!");
		}else {
			errorMessage = new ErrorMessage(ErrorType.INVALID_PARAMETER,
					"PageLimit is " + pageLimit + " but page number "+ page + " was requested. Read the documentation for a proper handling!");
		}
		return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
	}



}
