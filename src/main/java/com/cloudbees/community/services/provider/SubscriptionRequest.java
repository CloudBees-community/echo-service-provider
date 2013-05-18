package com.cloudbees.community.services.provider;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Subscription request
 *
 * <pre>
 *     {
 *          "cloudbees_account": "...",
 *          "plan":"new_plan",
 *          "settings": {
 *              "key":"value"
 *          }
 *     }
 * </pre>
 *
 * @author Vivek Pandey
 *
 */
public class SubscriptionRequest {

    /**
     * CloudBees account name of the subscriber. This is for information purposes only. It's never used by
     * the apis to manage subscriptions.
     */
    @JsonProperty("cloudbees_account")
    public String cloudbeesAccount;

    /**
     * Subscription plan name
     */
    @JsonProperty("plan")
    public String plan;

    /**
     * Subscription callback url. We will use this to call back cloudbees for any subscription updates
     * back to CloudBees, for example to report subscription specific usage.
     */
    @JsonProperty("callback_url")
    public String callbackUrl;

    /**
     * Settings property bag
     */
    @JsonProperty("settings")
    public Map <String, ?> settings = new HashMap<String, Object>();

    /**
     * If invalid throws {@link ServiceProviderException}
     */
    @JsonIgnore
    public void validate(){
        if(cloudbeesAccount == null){
            throw new ServiceProviderException("cloudbees_account is required parameter", 400);
        }

        if(callbackUrl == null){
            throw new ServiceProviderException("callback_url is required parameter", 400);
        }
    }
}
