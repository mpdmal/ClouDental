package com.mpdmal.cloudental.rest;

import java.util.Vector;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.mpdmal.cloudental.beans.DentistBean;
import com.mpdmal.cloudental.entities.Dentist;

@Path("first")
@ManagedBean
public class FirstResource {
    @SuppressWarnings("unused")
    @Context
    private UriInfo context;

    @Inject
    DentistBean dbn;
    public FirstResource() {}

    @GET
    @Produces("application/xml")
    public String getXml() {
    	String ans = "<dentists>";
    	Vector<Dentist> dentists = dbn.getDentists();
    	for (Dentist dentist : dentists) {
			ans+=dentist.getXML();
		}
    	ans += "</dentists>";
    	return ans;
    }

    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }

}