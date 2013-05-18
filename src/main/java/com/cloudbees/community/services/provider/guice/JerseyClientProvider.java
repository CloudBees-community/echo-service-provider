package com.cloudbees.community.services.provider.guice;

import com.google.inject.Provider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

/**
 * @author Vivek Pandey
 */
public class JerseyClientProvider implements Provider<Client> {
    private static final Client client = createClient();


    @Override
    public Client get() {
        return client;
    }

    private static Client createClient(){
        ClientConfig cc = new DefaultClientConfig(JacksonConfigurator.class);
        cc.getClasses().add(JacksonJsonProvider.class);
        Client c = Client.create(cc);
        c.setFollowRedirects(true);
        return c;
    }


}
