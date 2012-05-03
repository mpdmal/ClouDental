package com.mpdmal.cloudental.beans;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.jws.WebService;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.util.CloudentUtils;


@Named
@Stateless
@LocalBean
@WebService
public class LoginBean {
	@EJB DentistBean dentistEao;
	
    public LoginBean() {}

    public Dentist doLogin(String username, String password) {
    	Dentist d = dentistEao.getDentist(username);
    	if (d == null)
    		return null;
    	
    	if (!d.getPassword().equals(password))
    		return null;
    	CloudentUtils.logMessage("successfully logged in "+username);
    	return d;
    }
}
