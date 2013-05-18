package com.cloudbees.community.services.provider;

import com.cloudbees.community.services.provider.guice.JacksonConfigurator;
import org.apache.commons.codec.binary.Hex;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Vivek Pandey
 */
public class Utils {
    private static final SessionFactory sessionFactory = buildSessionFactory();
    public static final ProviderManifest manifest = loadManifest();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public  static String sha1Hash(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        return  Hex.encodeHexString(digest.digest(data.getBytes("UTF-8")));
    }

    public static String buildSsoHash(String id, long timestamp) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(":").append(manifest.api.ssoSalt).append(":").append(timestamp);
        return sha1Hash(sb.toString());
    }

    private static ProviderManifest loadManifest() {
        InputStream is = Utils.class.getResourceAsStream("/manifest.json");
        try {
            return new JacksonConfigurator().get().readValue(is, ProviderManifest.class);
        } catch (IOException e) {
            throw new ServiceProviderException("Failed to load manifest", 500);
        }
    }

}
