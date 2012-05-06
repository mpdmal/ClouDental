package com.mpdmal.cloudental.beans;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.jws.WebService;

import com.mpdmal.cloudental.beans.base.AbstractEaoService;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.InvalidPasswordException;


@Named
@Stateless
@LocalBean
@WebService
public class LoginBean extends AbstractEaoService {
	@EJB DentistBean dentistEao;
	
    public LoginBean() {}

    public Dentist doLogin(String username, String password) 
    									throws DentistNotFoundException, 
    									InvalidPasswordException {
    	Dentist d = dentistEao.getDentist(username);
    	if (d == null)
    		throw new DentistNotFoundException(username);
    	
    	if (!d.getPassword().equals(password))
    		throw new InvalidPasswordException(" for user:"+username);
    	
    	CloudentUtils.logMessage("successfully logged in "+username);
    	return d;
    }
}
