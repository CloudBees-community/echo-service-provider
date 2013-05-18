package com.cloudbees.community.services.provider.model;

import com.cloudbees.community.services.provider.Utils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Vivek Pandey
 */
@Entity
@Table(name = "resources")
public class Resource extends AbstractModel {

    private String settings;

    private String config;

    private String callbackUrl;

    private Subscription subscription;


    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    @Column(name="callback_url", nullable = false)
    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name="subscription_id", nullable=false)
    public Subscription getSubscription(){
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public static Resource find(Long id, Session session){
        Query query = session.createQuery("from Resource where id = :id and deletedAt is NULL");
        query.setParameter("id", id);
        return (Resource) query.uniqueResult();
    }

    public static Resource find(Long id){
        Session session = Utils.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Resource resource =  find(id, session);
        tx.commit();
        return resource;
    }

}
