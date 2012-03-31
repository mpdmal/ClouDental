package com.mpdmal.cloudental.beans;

import java.util.Vector;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.jws.WebService;

import com.mpdmal.cloudental.dao.DentistDAO;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.util.CloudentUtils;

@Named
@Stateless
@LocalBean
@WebService
public class DentistBean {
	@EJB DentistDAO _dao;
	
    public DentistBean() {}

	public void setDentistDao (DentistDAO dao) { _dao = dao;}//for testing
    public long countDentists () 		 { 	return _dao.countDentists();   	}    
    public Vector<Dentist> getDentists() { 	return _dao.getDentists();    	}
    public Dentist getDentist (String username) { return _dao.getDentist(username);    }
    public void createDentist(String username, String pwd, String surname, String name) {
    	if (getDentist(username) == null) {
	    	Dentist d = new Dentist();
	    	d.setName(name);
	    	d.setSurname(surname);
	    	d.setPassword(pwd);
	    	d.setUsername(username);
	    	_dao.updateCreate(d, false);
	    	return;
    	} 
    	CloudentUtils.logWarning("Dentist already exists:"+username);
    }
    public void deleteDentist(String username) {
    	Dentist d = getDentist(username);
    	if (d != null) {
    		_dao.delete(d);
    		return;
    	} 
    	CloudentUtils.logWarning("Dentist does not exist:"+username);
    }    
    public void updateDentist(Dentist d) {
    	if (getDentist(d.getUsername()) != null) {
    		_dao.updateCreate(d, true);
    		return;
    	}
    	CloudentUtils.logWarning("Dentist does not exist:"+d.getUsername());
    }
    public void deleteDentists() {
    	Vector<Dentist> dentists = _dao.getDentists();
    	for (Dentist dentist : dentists) {
			_dao.delete(dentist);
		}
    }
}
