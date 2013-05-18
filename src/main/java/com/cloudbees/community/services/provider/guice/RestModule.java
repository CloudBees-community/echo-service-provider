package com.cloudbees.community.services.provider.guice;

import com.cloudbees.community.services.provider.SecurityFilter;
import com.cloudbees.community.services.provider.api.EchoResource;
import com.cloudbees.community.services.provider.api.EchoSso;
import com.cloudbees.community.services.provider.api.EchoSubscription;
import com.google.common.collect.ImmutableMap;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.SessionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vivek Pandey
 */
public class RestModule extends ServletModule {

    @Override
    protected void configureServlets() {
        bind(GuiceContainer.class);
        bind(EchoResource.class);
        bind(EchoSubscription.class);
        bind(EchoSso.class);
        bind(JacksonConfigurator.class);
        bind(ObjectMapper.class).toProvider(JacksonConfigurator.class).asEagerSingleton();
        bind(SessionFactory.class).toProvider(HibernateSessionFactory.class).asEagerSingleton();
        bind(Client.class).toProvider(JerseyClientProvider.class).asEagerSingleton();
        Map<String, String> params = new HashMap<String, String>();
        params.put(PackagesResourceConfig.PROPERTY_PACKAGES, "com.cloudbees.community.services.provider.api");

        serve("/*")
                .with(GuiceContainer.class,
                        ImmutableMap.of(JSONConfiguration.FEATURE_POJO_MAPPING,
                                "true"));
        filter("/*").through(SecurityFilter.class);
    }
}
