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
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.util.exception.DentistExistsException;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;

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
    public Dentist createDentist(String username, String pwd, String surname, String name) 
    															throws DentistExistsException {
    	Dentist d = getDentist(username);
    	if (d != null)
        	throw new DentistExistsException(d.getSurname(), "Already exists, Wont create.");

    	d = new Dentist();
    	d.setName(name);
    	d.setSurname(surname);
    	d.setPassword(pwd);
    	d.setUsername(username);
    	_dao.updateCreate(d, false);
    	return d;
    }
    
    public void deleteDentist(String username) throws DentistNotFoundException {
    	Dentist d = getDentist(username);
    	if (d == null) 
    		throw new DentistNotFoundException(username, " Cannot delete");
    		
   		_dao.delete(d);
    }
    	
    public void updateDentist(Dentist d) throws DentistNotFoundException {
    	if (getDentist(d.getUsername()) == null)  
    		throw new DentistNotFoundException(d.getUsername(), " Cannot update");

   		_dao.updateCreate(d, true);
    }
    	
    public void deleteDentists() {
    	Vector<Dentist> dentists = _dao.getDentists();
    	for (Dentist dentist : dentists) {
			try {
				deleteDentist(dentist.getUsername());
			} catch (DentistNotFoundException e) {
				e.printStackTrace();
			}
		}
    }
}
