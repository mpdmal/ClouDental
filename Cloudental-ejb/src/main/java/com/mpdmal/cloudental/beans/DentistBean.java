package com.mpdmal.cloudental.beans;

import java.util.Vector;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.jws.WebService;

import com.mpdmal.cloudental.dao.ActivityDAO;
import com.mpdmal.cloudental.dao.DentistDAO;
import com.mpdmal.cloudental.dao.PatientDAO;
import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.util.CloudentUtils;

@Named
@Stateless
@LocalBean
@WebService
public class DentistBean {
	@EJB DentistDAO _dao;
	@EJB PatientDAO _pdao;
	@EJB ActivityDAO _acvdao;
	
    public DentistBean() {}

	public void setDentistDao (DentistDAO dao) { _dao = dao;}//for testing
	public void setPatientDao (PatientDAO dao) { _pdao = dao;}//for testing
	public void setActivityDao (ActivityDAO dao) { _acvdao = dao;}//for testing
	
    public long countDentists () 		 { 	return _dao.countDentists();   	}    
    public Vector<Dentist> getDentists() { 	return _dao.getDentists();    	}
    public Dentist getDentist (String username) { return _dao.getDentist(username);    }
    public Dentist createDentist(String username, String pwd, String surname, String name) {
    	Dentist d = getDentist(username);
    	if (d == null) {
	    	d = new Dentist();
	    	d.setName(name);
	    	d.setSurname(surname);
	    	d.setPassword(pwd);
	    	d.setUsername(username);
	    	_dao.updateCreate(d, false);
	    	return d;
    	} 
    	CloudentUtils.logWarning("Dentist already exists:"+username);
    	return d;
    }
    public void deleteDentist(String username) {
    	Dentist d = getDentist(username);
    	if (d != null) {
    		for (Patient ptnt : d.getPatients()) {
    	    	for (Activity acv : _acvdao.getActivities(ptnt.getId())) {
    				_acvdao.delete(acv);
    			}	
    	    }
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
			deleteDentist(dentist.getUsername());
		}
    }
}
