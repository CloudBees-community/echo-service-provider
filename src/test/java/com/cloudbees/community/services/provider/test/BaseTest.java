package com.cloudbees.community.services.provider.test;

import com.cloudbees.community.services.provider.ProviderMap;
import com.cloudbees.community.services.provider.guice.GuiceConfig;
import com.cloudbees.community.services.provider.guice.JacksonConfigurator;
import com.cloudbees.community.services.provider.guice.JerseyClientProvider;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import junit.framework.Assert;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.After;
import org.junit.Before;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.util.Map;

/**
 * @author Vivek Pandey
 */
public abstract class BaseTest {
    protected String account = "acc"+System.currentTimeMillis();
    protected final Client client = new JerseyClientProvider().get();
    protected String baseUri;

    protected int port;
    private Server server;
    public static final ObjectMapper om = new JacksonConfigurator().get();


    @Before
    public void setup() throws Exception {
        client.addFilter(new HTTPBasicAuthFilter("echo-service-user", "12345"));
        client.addFilter(new LoggingFilter(System.out));
        client .setFollowRedirects(false);
        server = new Server();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(0);
        server.addConnector(connector);

        ServletContextHandler sch = new ServletContextHandler(server, "/");
        sch.addFilter(GuiceFilter.class, "/*", null);
        sch.addServlet(DefaultServlet.class, "/");
        sch.addEventListener(new GuiceConfig());
        server.start();
        port = server.getConnectors()[0].getLocalPort();
        baseUri = "http://localhost:"+port;
    }

    @After
    public void tearDown() throws Exception {
        if (server != null) server.stop();
    }

    protected String createSubscription(Map settings){
        Map request = new ProviderMap().put("cloudbees_account", account).put("settings", settings).
                put("callback_url", UriBuilder.fromPath(baseUri).path("api/vendor/callback").path("subscriptions").build()).content();


        WebResource wr = client.resource(UriBuilder.fromPath(baseUri).path("/cloudbees/subscriptions").build());

        ClientResponse cr =  wr.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class,request);
        Assert.assertEquals(200, cr.getStatus());
        Map resp = cr.getEntity(Map.class);

        String subscriptionId = (String) resp.get("id");
        Assert.assertNotNull(subscriptionId);
        Map settingsResp = (Map)resp.get("settings");
        Assert.assertNotNull(settingsResp);
        return subscriptionId;
    }

    protected void updateSubscription(String subscriptionId, Map settings){
        Map request = new ProviderMap().put("settings", settings).content();

        WebResource wr = client.resource(UriBuilder.fromPath(baseUri).path("/cloudbees/subscriptions").path(subscriptionId).build());

        ClientResponse cr =  wr.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).put(ClientResponse.class,request);
        Assert.assertEquals(200, cr.getStatus());
        Map resp = cr.getEntity(Map.class);

        Assert.assertNotNull(subscriptionId);
        Map settingsResp = (Map)resp.get("settings");
        Assert.assertNotNull(settingsResp);
    }

    protected void deleteSubscription(String subscriptionId){
        WebResource wr = client.resource(UriBuilder.fromPath(baseUri).path("/cloudbees/subscriptions").path(subscriptionId).build());

        ClientResponse cr =  wr.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).delete(ClientResponse.class);
        Assert.assertEquals(200, cr.getStatus());
    }

    protected String createResource(String subscriptionId, String id, Map settings){
        Map request = new ProviderMap().put("id", id).put("subscription_id", subscriptionId).put("settings", settings).put("resource_type", "echo").
                put("callback_url", UriBuilder.fromPath(baseUri).path("api/vendor/callback").path("resources").build()).content();


        WebResource wr = client.resource(UriBuilder.fromPath(baseUri).path("/cloudbees/resources").build());

        ClientResponse cr =  wr.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class,request);
        Assert.assertEquals(200, cr.getStatus());
        Map resp = cr.getEntity(Map.class);

        String resourceId = (String) resp.get("id");
        Assert.assertNotNull(resourceId);
        Map settingsResp = (Map)resp.get("settings");
        Assert.assertNotNull(settingsResp);
        return resourceId;
    }

    protected void updateResource(String resourceId, Map settings){
        Map request = new ProviderMap().put("settings", settings).content();


        WebResource wr = client.resource(UriBuilder.fromPath(baseUri).path("/cloudbees/resources").path(resourceId).build());

        ClientResponse cr =  wr.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).put(ClientResponse.class,request);
        Assert.assertEquals(200, cr.getStatus());
        Map resp = cr.getEntity(Map.class);

        Map settingsResp = (Map)resp.get("settings");
        Assert.assertNotNull(settingsResp);
    }

    protected void deleteResource(String resourceId){
        WebResource wr = client.resource(UriBuilder.fromPath(baseUri).path("/cloudbees/resources").path(resourceId).build());

        ClientResponse cr =  wr.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).delete(ClientResponse.class);
        Assert.assertEquals(200, cr.getStatus());
    }

    private static Client createClient(){
        ClientConfig cc = new DefaultClientConfig(JacksonConfigurator.class);
        cc.getClasses().add(JacksonJsonProvider.class);
        Client c = Client.create(cc);
        c.setFollowRedirects(true);
        return c;
    }
}
