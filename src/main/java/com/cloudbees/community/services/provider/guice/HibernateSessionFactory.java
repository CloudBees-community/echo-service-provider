package com.cloudbees.community.services.provider.guice;

import com.cloudbees.community.services.provider.Utils;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.hibernate.SessionFactory;

/**
 * @author Vivek Pandey
 */
@Singleton
public class HibernateSessionFactory implements Provider<SessionFactory> {
    private static final SessionFactory sf = Utils.getSessionFactory();

    public SessionFactory get(){
        return sf;
    }
}
