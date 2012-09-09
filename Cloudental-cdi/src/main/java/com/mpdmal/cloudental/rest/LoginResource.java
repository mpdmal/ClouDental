package com.mpdmal.cloudental.rest;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.mpdmal.cloudental.beans.LoginBean;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

@Path("login/{username}/{password}")
@ManagedBean
public class LoginResource {
    @Inject
    LoginBean lbn;
    
    @POST
    @Produces("application/xml")
    public String login(@PathParam("username") String username, @PathParam("password") String password) {
    	try {
			return lbn.doLogin(username, password).getBASICXML();
		} catch (CloudentException e) {
	    	return e.getXML();
		}
    }
}