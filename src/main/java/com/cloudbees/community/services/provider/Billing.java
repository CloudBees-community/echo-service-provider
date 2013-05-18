package com.cloudbees.community.services.provider;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Billing message includes subscription or resource specific billing details.
 * <pre>
 * "billing": {
 *  "plans":[
 *      {
 *          "name": "...",
 *          "message": "...",
 *          "usage": {
 *              "build-minute": 2
 *          }
 *      }],
 *      "message": "...",
 *      "timestamp": 121212121
 *      "message": "...",
 *      "timestamp": 2133423444
 * }</pre>
 *
 * @author Vivek Pandey
 *
 */
public class Billing {
    /**
     * Array of billing {@link Plan}
     */
    @JsonProperty("plans")
    public List<Plan> plans;

    /**
     * Message to go with billing. Optional.
     */
    @JsonProperty("message")
    public String message;

    /**
     * Timestamp in number of milli seconds since epoch (Jan 1, 1970, 00:00:00 GMT)
     */
    @JsonProperty("timestamp")
    public String timestamp;

    /**
     * Abstraction of CloudBees billing plan
     */
    public static class Plan{
        /**
         * Name of the plan as appears in the manifest billing subscription-name element.
         *
         * <pre><code>
         * "billing" : [
         *      {
         *          "subscription-name" : "..."
         *          ....
         *      }, ...
         * ]
         * </code></pre>
         */
        @JsonProperty("name")
        public String name;

        /**
         * Optional
         */
        @JsonProperty("message")
        public String message;

        /**
         * Optional. If your billing plan is usage based then stuff in unit-of-measure and quantity.
         *
         * the unit of measure should match exactly as defined in the manifest.
         *
         * {"build-minute": 20}
         *
         */
        @JsonProperty("usage")
        public Map<String, Integer> usage;
    }
}
