package de.uniba.dsg.jaxrs.API;



import de.uniba.dsg.jaxrs.resources.health.Liveness;
import de.uniba.dsg.jaxrs.resources.health.Readiness;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.uniba.dsg.jaxrs.resources.DbHandlerResource;

import java.util.HashSet;
import java.util.Set;


public class DbHandlerApi extends ExamplesApi {

    private static final Log logger = LogFactory.getLog(DbHandlerApi.class);

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> resources = new HashSet<>();
        logger.info("in example API");
        resources.add(DbHandlerResource.class);
        resources.add(Liveness.class);
        resources.add(Readiness.class);
        return resources;
    }
}