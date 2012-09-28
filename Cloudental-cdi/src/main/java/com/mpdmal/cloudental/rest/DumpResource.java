package com.mpdmal.cloudental.rest;

import java.util.Vector;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.mpdmal.cloudental.beans.DentistBean;
import com.mpdmal.cloudental.entities.Dentist;

@Path("dump")
@ManagedBean
public class DumpResource {
    @Inject
    DentistBean dbn;

    @GET
    @Produces("application/xml")
    public String getDentistDump() {
    	String ans = Dentist.DENTIST_NODE;
    	Vector<Dentist> dentists = dbn.getDentists();
    	for (Dentist dentist : dentists) {
			ans+=dentist.getXML();
		}
    	ans += Dentist.DENTIST_ENDNODE;
    	return ans;
    }
}