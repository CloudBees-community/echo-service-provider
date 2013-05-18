package com.cloudbees.community.services.provider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vivek Pandey
 */
public class ProviderMap  {
    private final Map map = new HashMap();

    public ProviderMap put(String key, Object value){
        map.put(key,value);
        return this;
    }

    public Map content(){
        return map;
    }
}
