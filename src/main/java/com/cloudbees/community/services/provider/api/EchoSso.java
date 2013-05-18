package com.cloudbees.community.services.provider.api;

import com.cloudbees.community.services.provider.ServiceProviderException;
import com.cloudbees.community.services.provider.Utils;
import com.cloudbees.community.services.provider.guice.InjectLogger;
import com.cloudbees.community.services.provider.model.Resource;
import com.cloudbees.community.services.provider.model.Subscription;
import org.slf4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author Vivek Pandey
 */
@Path("/cloudbees/sso")
public class EchoSso {

    @InjectLogger
    private Logger logger;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/subscriptions/{id}")
    public Response subscriptionSso(@PathParam("id")String id, @QueryParam("token") String token, @QueryParam("timestamp") long timestamp){
        Subscription subscription = Subscription.find(Long.valueOf(id));
        if(subscription == null){
            throw new ServiceProviderException("Unauthorized", 401);
        }
        try {
            String hash = Utils.buildSsoHash(id, timestamp);
            if(hash.equals(token) && ((System.currentTimeMillis() - timestamp) < 300000L)){ //within 5 mins
                // TODO: set cookie with this authenticated session
                return Response.status(302).location(uriInfo.getBaseUri()).build();
            }else{
                throw new ServiceProviderException("Unauthorized", 401);
            }
        } catch (Exception e) {
            throw new ServiceProviderException(e.getMessage(), 500);
        }
    }

    @GET
    @Path("/resources/{subscription_id}/{resource_id}")
    public Response resourceSso(@PathParam("subscription_id")String subscriptionId,@PathParam("resource_id")String resourceId, @QueryParam("token") String token, @QueryParam("timestamp") long timestamp){
        Resource resource = Resource.find(Long.valueOf(resourceId));
        if(resource == null || (!resource.getSubscription().getId().equals(Long.valueOf(subscriptionId)))){
            throw new ServiceProviderException("Unauthorized", 401);
        }

        try {
            String hash = Utils.buildSsoHash(resourceId, timestamp);
            if(hash.equals(token) && ((System.currentTimeMillis() - timestamp) < 300000L)){ //within 5 mins
                // TODO: set cookie with this authenticated session
                return Response.status(302).location(uriInfo.getBaseUri()).build();
            }else{
                logger.error(String.format("SSO failed for subscription: %s, resource: %s", subscriptionId,resourceId));
                throw new ServiceProviderException("Unauthorized", 401);
            }
        } catch (Exception e) {
            throw new ServiceProviderException(e.getMessage(), 500);
        }
    }
}
