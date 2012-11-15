package com.mpdmal.cloudental.beans;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.jws.WebService;
import javax.mail.MessagingException;
import javax.persistence.Query;

import net.sf.jasperreports.engine.JRException;

import com.mpdmal.cloudental.beans.base.AbstractEaoService;
import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.Medicalhistory;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.Patienthistory;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.entities.UserPreferences;
import com.mpdmal.cloudental.entities.Visit;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.DiscountNotFoundException;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;
import com.mpdmal.cloudental.util.exception.PatientExistsException;
import com.mpdmal.cloudental.util.exception.PatientNotFoundException;
import com.mpdmal.cloudental.util.exception.PricelistItemNotFoundException;
import com.mpdmal.cloudental.util.exception.ValidationException;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

@Named
@Stateless
@LocalBean
@WebService
public class DentistServices extends AbstractEaoService {
	private static final long serialVersionUID = 1L;

	//UserPrefs
	public UserPreferences getUserPrefs(int userid) throws CloudentException {
		UserPreferences item = emgr.findOrFail(UserPreferences.class, userid);
    	if (item == null)
    		throw new CloudentException("Cannot get Prefs, userid: "+userid);
    	return item;
	}
	
	public void savePrefs (UserPreferences prefs) { emgr.update(prefs); }
	public void sendOnDemandReport (int userid, String email, int type) throws FileNotFoundException,
																				JRException,
																				MessagingException {
			String reportname = CloudentUtils.printReport(userid, type);
			CloudentUtils.mailReport(reportname, email);
	}
	
	//DISCOUNTS
	public Discount createDiscount(int dentistid, String title, String description, double value) 
											throws DentistNotFoundException, ValidationException {
		Dentist dentist = findDentist(dentistid);
		Discount d = new Discount();
		if (description == null)
			d.setDescription("");
		else
			d.setDescription(description);
		d.setTitle(title);
		d.setDiscount(BigDecimal.valueOf(value));
		d.setDentist(dentist);
		dentist.addDiscount(d);

		emgr.persist(d);
		return d;
	}

    public long countDiscounts() {
    	Query q = emgr.getEM().createQuery("select count(d) from Discount d");
        return emgr.executeSingleLongQuery(q);
    }
    
	public long countDentistDiscounts(int dentistid) {
    	Query q = emgr.getEM().
    			createQuery("select count(d) from Discount d where d.dentist.id =:dentistid").
    			setParameter("dentistid", dentistid);
        return emgr.executeSingleLongQuery(q);
    }

    public void updateDiscount(int id, String description, String title) throws DiscountNotFoundException {
    	Discount d = findDiscount(id);
		d.setDescription(description);
		d.setTitle(title);
		emgr.update(d);
    }

	public void deleteDiscount(int id) throws DiscountNotFoundException {
		Discount d = findDiscount(id);
		d.getDentist().removeDiscount(d);
		emgr.delete(d);
	}

	@SuppressWarnings("unchecked")
	public Collection<Discount> getDiscounts(int dentistid) {
    	Query q = emgr.getEM().
    			createQuery("select d from Discount d where d.dentist.id =:dentistid").
    			setParameter("dentistid", dentistid);
        return (Collection<Discount>) emgr.executeMultipleObjectQuery(q);
    }

	public void deleteDiscounts (int dentistid) throws DentistNotFoundException {
		Dentist d = findDentist(dentistid);
		for (Discount discount : d.getDiscounts()) {
			emgr.delete(discount);
		}
	}

    //PRICABLES
    public PricelistItem createPricelistItem(int dentistid, String title, String description, double value) 
														throws InvalidPostitAlertException, DentistNotFoundException, ValidationException {
		Dentist dentist = findDentist(dentistid);
		PricelistItem item = new PricelistItem();
		if (description == null)
			item.setDescription("");
		else
			item.setDescription(description);
		item.setTitle(title);
		item.setPrice(BigDecimal.valueOf(value));
		item.setDentist(dentist);
		dentist.addPricelistItem(item);

		emgr.persist(item);
		return item;
	}
    
    public long countPricelistItems() {
    	Query q = emgr.getEM().createQuery("select count(pi) from PricelistItem pi");
        return emgr.executeSingleLongQuery(q);
    }

    public long countDentistPricelistItems(int dentistid) {
    	Query q = emgr.getEM().
    			createQuery("select count(pi) from PricelistItem pi where pi.dentist.id =:dentistid").
    			setParameter("dentistid", dentistid);
        return emgr.executeSingleLongQuery(q);
    }

    public void updatePricelistItem(int id, String description, String title) 
    											throws PricelistItemNotFoundException {
		PricelistItem item = findPricable(id);
		item.setDescription(description);
		item.setTitle(title);
		emgr.update(item);
    }

    @SuppressWarnings("unchecked")
	public Collection<PricelistItem> getPricelist(int dentistid) {
    	Query q = emgr.getEM().
    			createQuery("select pi from PricelistItem pi where pi.dentist.id =:dentistid").
    			setParameter("dentistid", dentistid);
        return (Collection<PricelistItem>) emgr.executeMultipleObjectQuery(q);
    }

	public void deletePricelistItem(int id) throws PricelistItemNotFoundException {
		PricelistItem item = findPricable(id);
		item.getDentist().removePricelistItem(item);
		emgr.delete(item);
	}

	public void deletePricelist (int dentistid) throws DentistNotFoundException {
		Dentist d = findDentist(dentistid);
		for (PricelistItem item : d.getPriceList()) {
			emgr.delete(item);
		}
	}

    //POST-IT
    
    //PATIENT
	public Patient createPatient(int dentistid, String name, String surname) 
													throws DentistNotFoundException,
													PatientExistsException, ValidationException, 
													DiscountNotFoundException, PricelistItemNotFoundException {
		Dentist dentist = findDentist(dentistid);

		//patient
		long created = new Date().getTime(); 
		Patient p = new Patient();
		p.setCreated(new Date(created));
		p.setName(name);
		p.setSurname(surname);

		//auto generate medical history
		Medicalhistory medhistory = new Medicalhistory();
		medhistory.setComments("Auto Generated");
		medhistory.setPatient(p);

		//auto generate default activity 
		Activity ac = new Activity();
		ac.setDescription(Activity.DEFAULT_ACTIVITY_IDENTIFIER_DESCR);
		ac.setDiscount(findDiscount(CloudentUtils.DEFAULT_DISCOUNT_ID));
		ac.setEnddate(null);
		ac.setOpen(true);
		ac.setPrice(BigDecimal.ZERO);
		ac.setPriceable(findPricable(CloudentUtils.DEFAULT_PRICEABLE_ID));
		ac.setStartdate(new Date());

		//auto generate med history
		Patienthistory dentalhistory = new Patienthistory();
		dentalhistory.setComments("auto generated");
		dentalhistory.setStartdate(new Date());
		dentalhistory.setPatient(p);
		dentalhistory.addActivity(ac);
		
		p.setDentist(dentist);
		p.setMedicalhistory(medhistory);
		p.setDentalhistory(dentalhistory);
		dentist.addPatient(p);
		
		emgr.persist(p);
		return p;
    }

    public long countPatients() {
    	Query q = emgr.getEM().createQuery("select count(p) from Patient p");
        return emgr.executeSingleLongQuery(q);
    }

    public long countDentistPatients(int dentistid) {
    	Query q = emgr.getEM().
    			createQuery("select count(p) from Patient p where p.dentist.id =:dentistid").
    			setParameter("dentistid", dentistid);
        return emgr.executeSingleLongQuery(q);
    }
    
    public void updatePatient(int id, String name, String surname, String comments) throws PatientNotFoundException {
		Patient p = findPatient(id);
		p.setSurname(surname);
		p.setName(name);
		p.setComments(comments);
		emgr.update(p);
	}
    
    @SuppressWarnings("unchecked")
	public Collection<Patient> getPatientlist(int dentistid) {
    	Query q = emgr.getEM().
    			createQuery("select p from Patient p where p.dentist.id =:dentistid").
    			setParameter("dentistid", dentistid);
        return (Collection<Patient>) emgr.executeMultipleObjectQuery(q);
    }

	public void deletePatient (int patientid) throws PatientNotFoundException {
		System.out.println("@@");
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
	
    
    @SuppressWarnings("unchecked")
	public Vector<Visit> getDentistVisits (int dentistid)  {
    	Query q = emgr.getEM().
    			createQuery("select v from Visit v where v.activity.patienthistory.patient.dentist.id =:dentistid").
    			setParameter("dentistid", dentistid);
        return (Vector<Visit>) emgr.executeMultipleObjectQuery(q);
    }

}
