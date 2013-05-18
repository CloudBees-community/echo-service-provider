package com.cloudbees.community.services.provider.api;

import com.cloudbees.community.services.provider.ErrorMessage;
import com.cloudbees.community.services.provider.ResourceRequest;
import com.cloudbees.community.services.provider.ResourceResponse;
import com.cloudbees.community.services.provider.SubscriptionRequest;
import com.cloudbees.community.services.provider.SubscriptionResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import java.util.Map;

/**
 * Resource endpoint
 *
 * CloudBees calls this endpoint for any subscription management events (create, update, delete)
 * <pre>
 * { "subscription_id": "...",
 *      "id": "...",
 *      "resource_type": "...",
 *      "callback_url": "...",
 *      "settings": {
 *          "key" : "value"
 *      }
 * }</pre>
 *
 * @author Vivek Pandey
 *
 */

@Path("/cloudbees/resources")
@Produces("application/json")
@Consumes("application/json")
public class EchoResource {
    @POST
    public ResourceResponse create(ResourceRequest request) {
        throw new WebApplicationException(new ErrorMessage("Not implemented").response(400));
    }

    @PUT
    @Path("id")
    public SubscriptionResponse update(@PathParam("id") String id, SubscriptionRequest request) {
        throw new WebApplicationException(new ErrorMessage("Not implemented").response(400));
    }

    @DELETE
    @Path("id")
    public Map delete(@PathParam("id") String id){
        throw new WebApplicationException(new ErrorMessage("Not implemented").response(400));
    }

}
