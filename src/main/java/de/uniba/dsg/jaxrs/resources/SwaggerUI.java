
package de.uniba.dsg.jaxrs.resources;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import de.uniba.dsg.jaxrs.Configuration;

@Path("/swagger-ui")
public class SwaggerUI {
	
	private static final Properties properties = Configuration.loadProperties();
    private static final String SWAGGER_UI_FILES = "META-INF/resources/webjars/swagger-ui/" + properties.getProperty("swaggeruiversion");
    private static final String BEVERAGE_FILE_NAME = "beverage.yaml";
    private static final String MANAGEMENT_FILE_NAME = "manager.yaml";
    private static final Logger logger = Logger.getLogger("SwaggerUI");
    
    @GET
    public Response showSwaggerUI() throws URISyntaxException {
        String resourcePath = "";
        if (properties.getProperty("service").equals("management")) {
            logger.info("loading manager.json");
            resourcePath = MANAGEMENT_FILE_NAME;
        } else if (properties.getProperty("service").equals("beverage")) {
            logger.info("loading beverage.json");
            resourcePath = BEVERAGE_FILE_NAME;
        }
        return Response.seeOther(new URI("/v1/swagger-ui/index.html?url="+resourcePath)).build();
    }

    @GET
    @Path("{file}")
    public Response showSwaggerUI(@PathParam("file") final String file) {
        String resourcePath = null;
        String service = properties.getProperty("service");
        if (file.contains(".yaml")) {
            if ((service.equals("management") && file.equals(MANAGEMENT_FILE_NAME)) ||
                    service.equals("beverage") && file.equals(BEVERAGE_FILE_NAME)){
                logger.info("loading manager.json");
                resourcePath = file;
            } else{
                resourcePath = null;
            }
        } else {
            resourcePath = String.format("%s/%s", SWAGGER_UI_FILES, file);
        }
        InputStream resource = getClass().getClassLoader().getResourceAsStream(resourcePath);
        return Objects.isNull(resource)
                ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok().entity(resource).build();
    }
}