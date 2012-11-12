package com.mpdmal.cloudental.beans;

import java.io.Serializable;
import java.util.Vector;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.jws.WebService;
import javax.persistence.Query;

import com.mpdmal.cloudental.beans.base.AbstractEaoService;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.UserPreferences;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.DentistExistsException;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

@Named
@Stateless
@LocalBean
@WebService
public class DentistBean extends AbstractEaoService implements Serializable {
	private static final long serialVersionUID = 1L;

	public DentistBean() {}
    
    public Dentist createDentist(String name, String surname, String username, String password) 
    																throws  CloudentException {
		if (findDentistByUsername(username) != null)
			throw new DentistExistsException(username);
    	
    	Dentist d = new Dentist();
		d.setName(name);
		d.setSurname(surname);
		d.setUsername(username);
		d.setPassword(password);
		
		UserPreferences prefs = new UserPreferences();
		prefs.setDailyreports(true);
		prefs.setEmailcontent("");
		prefs.setEmailnotification(true);
		prefs.setEventTitleFormatType(CloudentUtils.EventTitleFormatType.SHORT.getValue());
		prefs.setDentist(d);
		
		emgr.persist(d);
		emgr.persist(prefs);
		return d;
    }

    public long countDentists() {
    	Query q = emgr.getEM().createQuery("select count(d) from Dentist d");
        return emgr.executeSingleLongQuery(q);
    }

    public void updateDentist(Dentist d) throws DentistNotFoundException {
    	if (emgr.findOrFail(Dentist.class, d.getId()) == null) {
    		throw new DentistNotFoundException(d.getUsername());
    	}
    	emgr.update(d);
    }
    
    public void deleteDentist(int id) throws DentistNotFoundException {
    	Dentist d = findDentist(id);
    	emgr.delete(d);
    }

    public void deleteDentistByUsername(String username) throws DentistNotFoundException {
    	Dentist d = findDentistByUsername(username); 
    	emgr.delete(d);
    }

    @SuppressWarnings("unchecked")
	public Vector<Dentist> getDentists() {
    	Query q = emgr.getEM().createQuery("select d from Dentist d");
        return (Vector<Dentist>) emgr.executeMultipleObjectQuery(q);
    }
    
    public void deleteDentists() {
    	Vector<Dentist> dents = getDentists();
    	for (Dentist dentist : dents) {
			try {
				deleteDentist(dentist.getId());
			} catch (DentistNotFoundException e) {
				//should never happen
				e.printStackTrace();
			}
		}
    }
}
