package com.cloudbees.community.services.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

/**
 * @author Vivek Pandey
 */
public class ErrorMessage {
    private String error;

    public ErrorMessage(String error) {
        this.error = error;
    }

    public ErrorMessage() {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error){
        this.error = error;
    }

    public Response response(int status){
        logger.error(error);
        return Response.status(status).entity(this).build();
    }

    private final Logger logger = LoggerFactory.getLogger(ErrorMessage.class);
}
