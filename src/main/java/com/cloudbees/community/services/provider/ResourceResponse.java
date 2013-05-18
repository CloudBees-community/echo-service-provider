package com.cloudbees.community.services.provider;

import java.util.Map;

/**
 * Resource response
 * <pre>
 *     {
 *          "id": "456",
 *          "billing": {
 *              "plans":[
 *                  {
 *                      "name": "...",
 *                      "message": "...",
 *                      "usage": {
 *                          "build-minute": 2
 *                      }
 *                  }
 *               ],
 *              "message": "...",
 *              "timestamp": 2133423444
 *          },
 *          "config": { ... },
 *          "settings": { ... },
 *          "message": "..."
 *      }
 * </pre>
 *
 * @author Vivek Pandey
 */
public class ResourceResponse {
    /**
     * Resource id
     */
    public String id;

    /**
     * Billing object. see {@link Billing}.
     *
     */
    public Billing billing;

    /**
     * Resource config object. Contains properties specific to the provisioned resource.
     */
    public Map<String, ?> config;

    /**
     * Settings as request. Provider can update it if needed.
     */
    public Map<String, ?> settings;

    /**
     * Provisioned resource message
     */
    public String message;
}
