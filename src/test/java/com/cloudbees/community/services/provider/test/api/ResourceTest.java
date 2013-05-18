package com.cloudbees.community.services.provider.test.api;

import com.cloudbees.community.services.provider.ProviderMap;
import com.cloudbees.community.services.provider.Utils;
import com.cloudbees.community.services.provider.test.BaseTest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import junit.framework.Assert;
import org.junit.Test;

import javax.ws.rs.core.UriBuilder;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author Vivek Pandey
 */
public class ResourceTest extends BaseTest{
    @Test
    public void create(){
        Map settings = new ProviderMap().put("email", "joe@example.com").content();
        String subscriptionId = createSubscription(settings);
        createResource(subscriptionId, "echo1",new ProviderMap().put("foo","bar").content());
    }

    @Test
    public void update(){
        Map settings = new ProviderMap().put("email", "joe@example.com").content();
        String subscriptionId = createSubscription(settings);
        String resourceId = createResource(subscriptionId, "echo1",new ProviderMap().put("foo","bar").content());
        updateResource(resourceId, new ProviderMap().put("foo1","bar1").content());
    }

    @Test
    public void delete(){
        Map settings = new ProviderMap().put("email", "joe@example.com").content();
        String subscriptionId = createSubscription(settings);
        String resourceId = createResource(subscriptionId, "echo1",new ProviderMap().put("foo","bar").content());
        deleteResource(resourceId);
    }

    @Test
    public void sso() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Map settings = new ProviderMap().put("email", "joe@example.com").content();
        String subscriptionId = createSubscription(settings);

        String resourceId = createResource(subscriptionId, "echo1",new ProviderMap().put("foo","bar").content());

        long timestamp = System.currentTimeMillis();
        String token = Utils.buildSsoHash(resourceId, timestamp);

        WebResource wr = client.resource(UriBuilder.fromPath(baseUri).path("/cloudbees/sso/resources").path(subscriptionId).path(resourceId).
                queryParam("token", token).queryParam("timestamp", timestamp).build());

        ClientResponse cr =  wr.get(ClientResponse.class);
        Assert.assertEquals(302, cr.getStatus());
    }


}
