package com.cloudbees.community.services.provider;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Map;

/**
 * Represents resource request
 *
 * <pre>
 * {
 *      "subscription_id": "...",
 *      "id": "...",
 *      "resource_type": "...",
 *      "callback_url": "...",
 *      "settings": {
 *          "key" : "value"
 *      }
 * }</pre>
 *
 * @author Vivek Pandey
 */
public class ResourceRequest {
    /**
     * Subscription id to which the provisioned resource belongs to.
     */
    @JsonProperty("subscription_id")
    public String subscriptionId;

    /**
     * Resource identifier
     */
    @JsonProperty("id")
    public String id;

    /**
     * Resource type, should match the resource "type" from manifest.json
     */
    @JsonProperty("resource_type")
    public String resourceType;


    /**
     * CloudBees resource callback URL specific to this provisioned resource.
     *
     * We will use this to call back cloudbees for any resource updates
     * back to CloudBees, for example to report resource specific usage.
     */
    @JsonProperty("callback_url")
    public String callbackUrl;

    /**
     * Settings property bag
     */
    @JsonProperty("settings")
    public Map settings;

    /**
     * configuration element
     */
    @JsonProperty("config")
    public Map config;


    /**
     * If invalid throws {@link ServiceProviderException}
     */
    @JsonIgnore
    public void validate(){
        if(subscriptionId == null){
            throw new ServiceProviderException("id is required parameter", 400);
        }

        if(id == null){
            throw new ServiceProviderException("id is required parameter", 400);
        }

        if(callbackUrl == null){
            throw new ServiceProviderException("callback_url is required parameter", 400);
        }

        if(resourceType == null || !resourceType.equals("echo")){
            throw new ServiceProviderException("Only 'echo' resource_type supported", 400);
        }

    }

}
