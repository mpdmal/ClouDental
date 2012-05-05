package com.mpdmal.cloudental.beans;

import java.util.Vector;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.jws.WebService;
import javax.persistence.Query;

import com.mpdmal.cloudental.EaoManager;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.util.exception.DentistExistsException;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.InvalidDentistCredentialsException;

@Named
@Stateless
@LocalBean
@WebService
public class DentistBean {
	@EJB
	private EaoManager emgr;
	
    public DentistBean() {}
    public DentistBean(EaoManager mgr) { 
    	this.emgr = mgr;
    }
    
    public Dentist createDentist(String name, String surname, String username, String password) 
    																throws DentistExistsException,
    																InvalidDentistCredentialsException  {
    	Dentist d = getDentist(username); 
    	if (d != null) 
    		throw new DentistExistsException(username, "Already exists, wont create");
    	if (name.equals("") || surname.equals("") || password.equals(""))
    		throw new InvalidDentistCredentialsException(username, "Name, surname and password need to be filled \n");
    	
    	d = new Dentist();
		d.setName(name);
		d.setSurname(surname);
		d.setUsername(username);
		d.setPassword(password);
		
		emgr.persist(d);
		return d;
    }

    public void updateDentist(Dentist d) throws DentistNotFoundException {
    	if (emgr.findOrFail(Dentist.class, d.getId()) == null) {
    		throw new DentistNotFoundException(d.getUsername());
    	}
    	emgr.update(d);
    }
    
    public void deleteDentist(Dentist d) throws DentistNotFoundException {
    	if (emgr.findOrFail(Dentist.class, d.getId()) == null) {
    		throw new DentistNotFoundException(d.getUsername());
    	}

    	emgr.delete(d);
    }

    public void deleteDentist(String username) throws DentistNotFoundException {
    	Dentist d = getDentist(username); 
    	if (d == null) {
    		throw new DentistNotFoundException(username);
    	}
    	emgr.delete(d);
    }

    public void deleteDentists() {
    	Vector<Dentist> dents = getDentists();
    	for (Dentist dentist : dents) {
			try {
				deleteDentist(dentist);
			} catch (DentistNotFoundException e) {
				//should never happen
				e.printStackTrace();
			}
		}
    }
    
    public long countDentists() {
    	Query q = emgr.getEM().createQuery("select count(d) from Dentist d");
        return emgr.executeSingleLongQuery(q);
    }

    public Dentist getDentist(String username) {
    	Query q = emgr.getEM().createQuery("select d from Dentist d where d.username = :username")
    			.setParameter("username", username);
        return (Dentist) emgr.executeSingleObjectQuery(q);
    }

    @SuppressWarnings("unchecked")
	public Vector<Dentist> getDentists() {
    	Query q = emgr.getEM().createQuery("select d from Dentist d");
        return (Vector<Dentist>) emgr.executeMultipleObjectQuery(q);
    }
    
    public void close() {
    	emgr.closeEM();
    }

}
