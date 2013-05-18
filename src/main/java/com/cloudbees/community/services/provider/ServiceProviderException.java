package com.cloudbees.community.services.provider;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * @author Vivek Pandey
 */
public class ServiceProviderException extends WebApplicationException {
    private int httpCode;

    public ServiceProviderException(String message, int httpCode) {
        super(Response.status(httpCode).entity(new ProviderMap().put("error", message).content()).build());
    }

}
