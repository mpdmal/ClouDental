package com.mpdmal.cloudental.beans;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.jws.WebService;

import com.mpdmal.cloudental.dao.DentistDAO;
import com.mpdmal.cloudental.entities.Dentist;


@Named
@Stateless
@LocalBean
@WebService
public class LoginBean {
	@EJB DentistDAO _ddao;
	
    public LoginBean() {}

	public void setDentistDao (DentistDAO dao) { _ddao = dao;}//for testing
    public Dentist doLogin(String username, String password) {
    	if (_ddao.getDentist(username) == null)
    		return null;
    	
    	Dentist d = _ddao.getDentist(username);
    	if (!d.getPassword().equals(password))
    		return null;
    	return d;
    }
}
