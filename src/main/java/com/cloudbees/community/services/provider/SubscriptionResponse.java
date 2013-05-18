package com.cloudbees.community.services.provider;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Map;

/**
 * Subscription Response returned by the provider
 * <pre>
 * {
 *      "id": "12345"
 *      "config": {
 *          "...": "..."
 *      },
 *      "settings": {
 *          "key": "value"
 *      },
 *      "billing": {
 *          "plans":[
 *              {
 *                  "name": "...",
 *                  "message": "...",
 *                  "usage": {
 *                    "build-minute": 2
 *                  }
 *              }],
 *          "message": "...",
 *          "timestamp": 2133423444
 *      },
 *      "message": "..."
 * }</pre>
 *
 * @author Vivek Pandey
 *
 */
public class SubscriptionResponse {
    /**
     *
     * Subscription id. CloudBees will refer to this subscription using this id in subsequent subscription management
     * calls such as update or delete subscription calls.
     *
     */
    @JsonProperty("id")
    public String id;

    /**
     *
     * Configuration related to this subscription.
     *
     * The configuration object should carry configuration settings. For example, an Email service provider will have
     *
     * "config": {
     *     "SMTP_HOST": "...",
     *     "USERNAME": "...",
     *     "PASSWORD": "..."
     * }
     *
     */
    @JsonProperty("config")
    public Map<String, ?> config;

    /**
     * In general echo of the settings object passed in the request. Provider can add or remove existing
     * information in to it.
     *
     * Optional
     */
    @JsonProperty("settings")
    public Map<String, ?> settings;

    /**
     *
     * If a subscription or resource needs to be billed by Cloudbees, you want to return this info here.
     *
     */
    @JsonProperty("billing")
    public Billing billing;

    /**
     * Provisioned subscription related message
     */
    @JsonProperty("message")
    public String message;
}
