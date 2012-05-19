package com.mpdmal.cloudental.beans;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.jws.WebService;
import javax.persistence.Query;

import com.mpdmal.cloudental.EaoManager;
import com.mpdmal.cloudental.beans.base.AbstractEaoService;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.Medicalhistory;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.Patienthistory;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;
import com.mpdmal.cloudental.util.exception.PatientExistsException;
import com.mpdmal.cloudental.util.exception.PatientNotFoundException;
import com.mpdmal.cloudental.util.exception.PricelistItemNotFoundException;

@Named
@Stateless
@LocalBean
@WebService
public class DentistServices extends AbstractEaoService {
    public DentistServices() {}
    public DentistServices(EaoManager mgr) { 
    	this.emgr = mgr;
    }

    public void close() {
    	emgr.closeEM();
    }

    //DENTIST

    //DISCOUNTS
    public long countDiscounts() {
    	Query q = emgr.getEM().createQuery("select count(d) from Discount d");
        return emgr.executeSingleLongQuery(q);
    }

    @SuppressWarnings("unchecked")
	public Collection<Discount> getDiscounts(int dentistid) {
    	Query q = emgr.getEM().
    			createQuery("select d from Discount d where d.dentist.id =:dentistid").
    			setParameter("dentistid", dentistid);
        return (Collection<Discount>) emgr.executeMultipleObjectQuery(q);
    }

	public Discount createDiscount(int dentistid, String title, String description, double value) 
											throws InvalidPostitAlertException, DentistNotFoundException {
		Dentist dentist = findDentist(dentistid);
		Discount d = new Discount();
		d.setDescription(description);
		d.setTitle(title);
		d.setDiscount(BigDecimal.valueOf(value));
		d.setDentist(dentist);
		dentist.addDiscount(d);

		emgr.persist(d);
		return d;
	}

	public void deleteDiscount(int id) {
		Discount d= emgr.findOrFail(Discount.class, id);
		if (d== null) {
			//TODO
			return ;
		}
		d.getDentist().removeDiscount(d);
		emgr.delete(d);
	}

    public void updateDiscount(int id, String description, String title) {
		Discount d= emgr.findOrFail(Discount.class, id);
		if (d == null) {
			//TODO
			return ;
		}
		for (Discount ds : d.getDentist().getDiscounts()) {
			if (ds.getId() == d.getId()) {
				ds.setDescription(description);
				ds.setTitle(title);
				break;
			}
		}
		emgr.update(d);
    }

    //PRICABLES

    public long countPricelistItems() {
    	Query q = emgr.getEM().createQuery("select count(pi) from PricelistItem pi");
        return emgr.executeSingleLongQuery(q);
    }

    @SuppressWarnings("unchecked")
	public Collection<PricelistItem> getPricelist(int dentistid) {
    	Query q = emgr.getEM().
    			createQuery("select pi from PricelistItem pi where pi.dentist.id =:dentistid").
    			setParameter("dentistid", dentistid);
        return (Collection<PricelistItem>) emgr.executeMultipleObjectQuery(q);
    }

	public PricelistItem createPricelistItem(int dentistid, String title, String description, double value) 
														throws InvalidPostitAlertException, DentistNotFoundException {
		Dentist dentist = findDentist(dentistid);
		PricelistItem item = new PricelistItem();
		item.setDescription(description);
		item.setTitle(title);
		item.setPrice(BigDecimal.valueOf(value));
		item.setDentist(dentist);
		dentist.addPricelistItem(item);

		emgr.persist(item);
		return item;
	}
	
	public void deletePricelistItem(int id) throws PricelistItemNotFoundException {
		PricelistItem item = findPricable(id);
		item.getDentist().removePricelistItem(item);
		emgr.delete(item);
	}

    public void updatePricelistItem(int id, String description, String title) 
    											throws PricelistItemNotFoundException {
		PricelistItem item = findPricable(id);
		item.getDentist().removePricelistItem(item);
		item.setDescription(description);
		item.setTitle(title);
		item.getDentist().addPricelistItem(item);
		emgr.update(item);
    }

    //POST-IT
    
    //PATIENT
 
    @SuppressWarnings("unchecked")
	public Collection<Patient> getPatientlist(int dentistid) {
    	Query q = emgr.getEM().
    			createQuery("select p from Patient p where p.dentist.id =:dentistid").
    			setParameter("dentistid", dentistid);
        return (Collection<Patient>) emgr.executeMultipleObjectQuery(q);
    }

	public Patient createPatient(int dentistid, String name, String surname) 
													throws DentistNotFoundException,
													PatientExistsException {
		Dentist dentist = findDentist(dentistid);

		//patient
		Patient p = new Patient();
		p.setCreated(new Date());
		p.setName(name);
		p.setSurname(surname);

		//auto generate medical history
		Medicalhistory medhistory = new Medicalhistory();
		medhistory.setComments("Auto Generated");
		medhistory.setPatient(p);

		//auto generate med history
		Patienthistory dentalhistory = new Patienthistory();
		dentalhistory.setComments("auto generated");
		dentalhistory.setStartdate(new Date());
		dentalhistory.setPatient(p);

		p.setDentist(dentist);
		p.setMedicalhistory(medhistory);
		p.setDentalhistory(dentalhistory);
		dentist.addPatient(p);
		
		emgr.update(dentist);
		return p;
    }

	public void deletePatient (int patientid) throws PatientNotFoundException {
		Patient p = findPatient(patientid);
		p.getDentist().removePatient(p);
		emgr.delete(p);
	}
	
	public void deletePatientList (int dentistid) throws DentistNotFoundException, PatientNotFoundException {
		Dentist dentist = findDentist(dentistid);
		Vector<Patient> ptns = (Vector<Patient>) dentist.getPatientList();
		while (ptns.size() > 0) {
			deletePatient(ptns.elementAt(0).getId());
		}
	}
}
