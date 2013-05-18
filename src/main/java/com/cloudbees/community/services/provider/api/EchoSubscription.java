package com.cloudbees.community.services.provider.api;

import com.cloudbees.community.services.provider.ProviderMap;
import com.cloudbees.community.services.provider.ServiceProviderException;
import com.cloudbees.community.services.provider.SubscriptionRequest;
import com.cloudbees.community.services.provider.SubscriptionResponse;
import com.cloudbees.community.services.provider.guice.InjectLogger;
import com.cloudbees.community.services.provider.model.Subscription;
import com.google.inject.Inject;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
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

    @InjectLogger
    private Logger logger;

    @Inject
    private SessionFactory sessionFactory;

    @Inject
    private ObjectMapper objectMapper;


    @POST
    public SubscriptionResponse create(SubscriptionRequest request) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = null;
        try{
            request.validate();
            tx =session.beginTransaction();
            SubscriptionResponse sr =  new SubscriptionResponse();
            sr.settings = request.settings;
            sr.config = new ProviderMap().put("ECHO_SERVICE_CONFIG", "some_config_str").content();
            Subscription subscription = new Subscription();
            subscription.setCloudbeesAccount(request.cloudbeesAccount);
            subscription.setSettings(objectMapper.writeValueAsString(request.settings));
            subscription.setConfig(objectMapper.writeValueAsString(sr.config));
            subscription.setCallbackUrl(request.callbackUrl);

            session.save(subscription);
            tx.commit();
            sr.id = String.valueOf(subscription.getId());
            return sr;
        }catch (Exception e){
            if (tx != null) {
                tx.rollback();
            }
            if(e instanceof WebApplicationException){
                throw (WebApplicationException)e;
            }
            throw new ServiceProviderException("Subscription creation failed: "+e.getMessage(), 500);
        }
    }

    @PUT
    @Path("{id}")
    public SubscriptionResponse update(@PathParam("id") String id, SubscriptionRequest request) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            Subscription subscription = Subscription.find(Long.valueOf(id), session);
            if(subscription == null){
                throw new ServiceProviderException("Unknown subscription_id: "+id, 400);
            }
            SubscriptionResponse sr =  new SubscriptionResponse();
            if(!request.settings.isEmpty()){
                subscription.setSettings(objectMapper.writeValueAsString(request.settings));
            }
            sr.settings = objectMapper.readValue(subscription.getSettings(), Map.class);
            sr.config = objectMapper.readValue(subscription.getConfig(), Map.class);
            session.save(subscription);
            tx.commit();
            return sr;
        }catch (Exception e){
            if (tx != null) {
                tx.rollback();
            }
            if(e instanceof WebApplicationException){
                throw (WebApplicationException)e;
            }
            throw new ServiceProviderException("Subscription update failed: "+e.getMessage(), 500);
        }
    }

    @DELETE
    @Path("{id}")
    public Map delete(@PathParam("id") String id){
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            Subscription subscription = Subscription.find(Long.valueOf(id), session);
            if(subscription == null){
                throw new ServiceProviderException("Unkown subscription_id: "+id, 400);
            }
            subscription.setDeletedAt(new Date());
            session.save(subscription);
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
