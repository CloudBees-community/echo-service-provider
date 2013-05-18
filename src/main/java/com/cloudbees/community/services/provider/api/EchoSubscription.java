package com.cloudbees.community.services.provider.api;

import com.cloudbees.community.services.provider.ProviderMap;
import com.cloudbees.community.services.provider.SubscriptionRequest;
import com.cloudbees.community.services.provider.SubscriptionResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.Map;
import java.util.UUID;

/**
 * Subscription endpoint
 *
 * CloudBees calls this endpoint for any subscription management events (create, update, delete)
 *
 * @author Vivek Pandey
 *
 */
@Path("/cloudbees/subscriptions")
@Produces("application/json")
@Consumes("application/json")
public class EchoSubscription {

    @POST
    public SubscriptionResponse create(SubscriptionRequest request) {
        SubscriptionResponse sr =  new SubscriptionResponse();
        sr.id = UUID.randomUUID().toString();
        sr.settings = request.settings;
        sr.config = new ProviderMap().put("ECHO_SERVICE_ENDPOINT", "http://echo-service.cloudbees.net/api");
        return sr;
    }

    @PUT
    @Path("id")
    public SubscriptionResponse update(@PathParam("id") String id, SubscriptionRequest request) {
        //check if the subscription id exists in DB, if not then throw exception
        SubscriptionResponse sr =  new SubscriptionResponse();
        sr.config = new ProviderMap().put("ECHO_SERVICE_ENDPOINT", "http://echo-service.cloudbees.net");
        return sr;
    }

    @DELETE
    @Path("id")
    public Map delete(@PathParam("id") String id){
        //check if the subscription id exists in DB, if not then throw exception
        //delete resource in DB
        return new ProviderMap().put("status", "ok");
    }
}
