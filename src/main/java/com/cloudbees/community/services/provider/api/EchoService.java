package com.cloudbees.community.services.provider.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.Map;

/**
 * Echo service.
 *
 * This service to be executed by an app deployed on CloudBees RUN@cloud platform
 *
 * @author Vivek Pandey
 */
@Path("/api")
public class EchoService {

    @Path("/echo")
    @GET
    public Map echo(@Context UriInfo uriInfo){
        return uriInfo.getQueryParameters();
    }

    @Path("/")
    @GET
    public Map echoRoot(@Context UriInfo uriInfo){
        return uriInfo.getQueryParameters();
    }

}
