package com.cloudbees.community.services.provider.api;

import com.cloudbees.community.services.provider.ProviderMap;
import com.cloudbees.community.services.provider.ResourceRequest;
import com.cloudbees.community.services.provider.ResourceResponse;
import com.cloudbees.community.services.provider.ServiceProviderException;
import com.cloudbees.community.services.provider.SubscriptionRequest;
import com.cloudbees.community.services.provider.guice.InjectLogger;
import com.cloudbees.community.services.provider.model.Resource;
import com.cloudbees.community.services.provider.model.Subscription;
import com.google.inject.Inject;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import java.util.Date;
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
    @InjectLogger
    private Logger logger;

    @Inject
    private SessionFactory sessionFactory;

    @Inject
    private ObjectMapper objectMapper;

    @POST
    public ResourceResponse create(ResourceRequest request) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = null;

        try{
            tx = session.beginTransaction();
            request.validate();

            Subscription subscription = Subscription.find(Long.valueOf(request.subscriptionId), session);
            if(subscription == null){
                throw new ServiceProviderException("Unknown subscription id: "+request.subscriptionId, 400);
            }

            Resource resource = new Resource();
            resource.setSettings(objectMapper.writeValueAsString(request.settings));
            resource.setCallbackUrl(request.callbackUrl);
            Map config = new ProviderMap().put("ECHO_RESOURCE_CONFIG", "some_config_str").content();

            resource.setConfig(objectMapper.writeValueAsString(config));

            resource.setSubscription(subscription);
            session.save(resource);
            ResourceResponse resourceResponse = new ResourceResponse();
            resourceResponse.config = config;
            resourceResponse.settings = request.settings;
            resourceResponse.id = String.valueOf(resource.getId());
            tx.commit();
            return resourceResponse;
        }catch (Exception e){
            logger.error(e.getMessage());
            if (tx != null) {
                tx.rollback();
            }
            if (e instanceof WebApplicationException){
                throw (WebApplicationException)e;
            }

            throw new ServiceProviderException("Resource creation failed: "+e.getMessage(), 500);
        }
    }

    @PUT
    @Path("{id}")
    public ResourceResponse update(@PathParam("id") String id, SubscriptionRequest request) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();

            Resource resource = Resource.find(Long.valueOf(id), session);
            if(resource == null){
                throw new ServiceProviderException("Unknown Resource id: "+id, 400);
            }
            resource.setSettings(objectMapper.writeValueAsString(request.settings));
            session.save(resource);
            ResourceResponse resourceResponse = new ResourceResponse();
            resourceResponse.config = objectMapper.readValue(resource.getSettings(),Map.class);
            resourceResponse.settings = request.settings;
            tx.commit();
            return resourceResponse;
        }catch (Exception e){
            if (tx != null) {
                tx.rollback();
            }
            if (e instanceof WebApplicationException){
                throw (WebApplicationException)e;
            }

            throw new ServiceProviderException("Resource creation failed: "+e.getMessage(), 500);
        }

    }

    @DELETE
    @Path("{id}")
    public Map delete(@PathParam("id") String id){
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            Resource resource = Resource.find(Long.valueOf(id), session);
            if(resource == null){
                throw new ServiceProviderException("Unknown Resource id: "+id, 400);
            }
            resource.setDeletedAt(new Date());
            session.save(resource);
            tx.commit();
            return new ProviderMap().put("status", "ok").content();

        }catch (Exception e){
            if (tx != null) {
                tx.rollback();
            }
            if(e instanceof WebApplicationException){
                throw (WebApplicationException)e;
            }
            throw new ServiceProviderException("Subscription deletion failed: "+e.getMessage(), 500);
        }

    }

}
