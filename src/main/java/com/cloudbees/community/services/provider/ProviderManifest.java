package com.cloudbees.community.services.provider;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Vivek Pandey
 */
public class ProviderManifest {
    public String id;
    public String name;

    public Billing[] billing;

    @JsonProperty("settings")
    public Map settings;

    public Api api;

    public static class Billing{
        @JsonProperty("name")
        public String name;
        @JsonProperty("subscription-name")
        public String subscriptionName;
        @JsonProperty("period")
        public String period;
        @JsonProperty("price")
        public Double price;
        @JsonProperty("term-strategy")
        Map<String,Object> termStrategy;
    }

    public static class Api{
        public String production;
        public String test;
        public String username;
        public String password;
        @JsonProperty("sso_salt")
        public String ssoSalt;
        @JsonProperty("config_vars")
        public List<String> configVars;
        public List<String> actions;

        @JsonProperty("sso_params")
        public List<String> ssoParams=new ArrayList<String>();

        @JsonProperty("account_prefix")
        public String accountPrefix;

        @JsonProperty("oauth_client_id")
        public String oauthClientId;

        /**
         * Subscription level context_parameters. It can carry any key:value pair, value could be a pattern. If a client
         * (SDK or GC) sends such key value pair, the received key must be checked and if the value is pattern or set of
         * values, the validation is imposed.
         */
        @JsonProperty("context_parameters")
        public Map<String,String> contextParameters;


        @JsonProperty("service_bindings")
        public List<String> serviceBindings;


        public List<Resource> resources;

        public static class Resource{
            public String type;
            @JsonProperty("config_vars")
            public List<String> configVars;
            @JsonProperty("default")
            public boolean defaultResource;
            public List<String> actions;
            @JsonProperty("resource-ui")
            public Map resourceUI;

            /**
             * Resource level context_parameters. It can carry any key:value pair, value could be a pattern. If a client
             * (SDK or GC) sends such key value pair, the received key must be checked and if the value is pattern or set of
             * values, the validation is imposed.
             */

            @JsonProperty("context_parameters")
            public Map<String,String> contextParameters;

            @JsonIgnore
            public boolean hasContextParam(String param){
                return contextParameters != null && contextParameters.containsKey(param);
            }

            public boolean validAction(String action){
                if(actions == null || actions.isEmpty()){
                    return false;
                }
                for(String a:actions){
                    if(a.equals(action)){
                        return true;
                    }
                }
                return false;
            }
        }

        @JsonIgnore
        public Resource getResource(String resourceType){
            if(resources == null){
                return null;
            }

            Resource defaultResource=null;
            for(Resource r:resources){
                if(r.defaultResource){
                    defaultResource = r;
                }
                if(r.type.equals(resourceType)){
                    return r;
                }
            }
            return defaultResource;
        }

        @JsonIgnore
        public String defaultResource(){
            if(resources == null){
                return null;
            }
            for(Resource r: resources){
                if(r.defaultResource){
                    return r.type;
                }
            }
            return null;
        }

        public boolean validAction(String action){
            if(actions == null || actions.isEmpty()){
                return false;
            }
            for(String a:actions){
                if(a.equals(action)){
                    return true;
                }
            }
            return false;
        }


    }

}
