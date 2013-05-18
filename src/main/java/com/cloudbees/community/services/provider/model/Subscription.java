package com.cloudbees.community.services.provider.model;

import com.cloudbees.community.services.provider.Utils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * @author Vivek Pandey
 */
@Entity
@Table(name = "subscriptions")
public class Subscription extends AbstractModel{
    private String cloudbeesAccount;

    private String settings;

    private String config;

    private String callbackUrl;

    private Set<Resource> resources;


    @Column(name = "cloudbees_account", nullable = false)
    public String getCloudbeesAccount() {
        return cloudbeesAccount;
    }

    public void setCloudbeesAccount(String cloudbeesAccount) {
        this.cloudbeesAccount = cloudbeesAccount;
    }

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

    @Column(name = "callback_url")
    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    @OneToMany(cascade= CascadeType.ALL,mappedBy="subscription")
    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    public static Subscription find(Long id, Session session){
        Query query = session.createQuery("from Subscription where id = :id and deleted_at is NULL");
        query.setParameter("id", id);
        return (Subscription) query.uniqueResult();
    }

    public static Subscription find(Long id){
        Session session = Utils.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Subscription sub =  find(id, session);
        tx.commit();
        return sub;
    }

}
