package com.cloudbees.community.services.provider;

import com.google.inject.Singleton;
import com.sun.jersey.core.util.Base64;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Vivek Pandey
 */
@Singleton
public class SecurityFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (authenticate((HttpServletRequest) request)) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse)response).setStatus(401);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print("{\"error\":\"Not authorized\"}");

        }
    }

    @Override
    public void destroy() {

    }

    private boolean authenticate(HttpServletRequest request) throws IOException, ServletException {
        String authHeader = request.getHeader("authorization");
        if(authHeader == null){
            return false;
        }
        String[] authHeaders = authHeader.split(" ");
        if(authHeaders.length < 2){
            return false;
        }
        String encodedValue = authHeader.split(" ")[1];
        String decodedValue = Base64.base64Decode(encodedValue);
        String[] creds = decodedValue.split(":");
        if(creds.length != 2){
            return false;
        }
        String username = creds[0];
        String password = creds[1];
        return username.equals("echo-service-user") && password.equals("12345");
    }
}
