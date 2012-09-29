package com.mpdmal.cloudental.beans;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.jws.WebService;
import javax.persistence.Query;

import com.mpdmal.cloudental.beans.base.AbstractEaoService;
import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Address;
import com.mpdmal.cloudental.entities.Contactinfo;
import com.mpdmal.cloudental.entities.ContactinfoPK;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.Medicalhistory;
import com.mpdmal.cloudental.entities.Medicalhistoryentry;
import com.mpdmal.cloudental.entities.MedicalhistoryentryPK;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.Patienthistory;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.entities.Visit;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.ActivityNotFoundException;
import com.mpdmal.cloudental.util.exception.DiscountNotFoundException;
import com.mpdmal.cloudental.util.exception.InvalidMedEntryAlertException;
import com.mpdmal.cloudental.util.exception.PatientNotFoundException;
import com.mpdmal.cloudental.util.exception.PricelistItemNotFoundException;
import com.mpdmal.cloudental.util.exception.ValidationException;
import com.mpdmal.cloudental.util.exception.VisitNotFoundException;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

@Named
@Stateless
@LocalBean
@WebService
public class PatientServices extends AbstractEaoService {
	//ACTIVITIES
    public Activity createActivity (int patientid, String description, Date start, Date end, int plitemid, int discountid, BigDecimal price) 
																throws CloudentException {
		Patient p = findPatient(patientid);
		Discount d = findDiscount(discountid); 
		PricelistItem plitem = findPricable(plitemid);
		
		Patienthistory ph = p.getDentalHistory();
		Activity ac = new Activity();
		
		ac.setDescription(description);
		ac.setStartdate(start);
		ac.setOpen((end == null) ? true : false);
		ac.setEnddate(end);
		ac.setPriceable(plitem);
		ac.setDiscount(d);
		ac.setPatienthistory(ph);
		ac.setPrice(price);
		ph.addActivity(ac);
		
		emgr.persist(ac);
		return ac;
    }

    public long countActivities() {
    	Query q = emgr.getEM().createQuery("select count(ac) from Activity ac");
        return emgr.executeSingleLongQuery(q);
    }

    public long countPatientActivities(int patientid) {
    	Query q = emgr.getEM().
    			createQuery("select count(ac) from Activity ac where ac.patienthistory.patient.id =:patientid").
    			setParameter("patientid", patientid);
        return emgr.executeSingleLongQuery(q);
    }

	public void deleteActivity (int activityid) throws ActivityNotFoundException {
		Activity act = findActivity(activityid);
		act.getPatienthistory().removeActivity(act);
		emgr.delete(act);
	}
	
	public void deletePatientActivities (int patientid) throws PatientNotFoundException, ActivityNotFoundException {
		Patient p = findPatient(patientid);
		
		Vector<Activity> acts = (Vector<Activity>) p.getDentalHistory().getActivities();
		while (acts.size() > 0) {
			deleteActivity(acts.elementAt(0).getId());
		}
	}
	
    public void updateActivity (Activity act) throws 
								ActivityNotFoundException, 
								DiscountNotFoundException, 
								PricelistItemNotFoundException {
		
		//validate activity
		findDiscount(act.getDiscount().getId());
		findPricable(act.getPriceable().getId());
		if (act.getEnddate() != null && (act.getEnddate().getTime() <= act.getStartdate().getTime())) {
		CloudentUtils.logWarning("Activity enddate cannot precede start date, wont update:"+act.getId());
		return;
		}
		
		Activity ac = findActivity(act.getId());
		ac.setDiscount(act.getDiscount());
		ac.setDescription(act.getDescription());
		ac.setOpen(act.isOpen());
		ac.setPrice(act.getPrice());
		ac.setPriceable(act.getPriceable());
		emgr.update(ac);
	}
   
    @SuppressWarnings("unchecked")
	public Vector<Activity> getPatientActivities (int patientid) throws PatientNotFoundException {
		Patient p = findPatient(patientid);

    	Query q = emgr.getEM().
    			createQuery("select ac from Activity ac where ac.patienthistory.patient.id =:patientid").
    			setParameter("patientid", p.getId());
        return (Vector<Activity>) emgr.executeMultipleObjectQuery(q);
    }

    //OTHER SERVICES
    public Medicalhistoryentry createMedicalEntry(int patientID, String comment, int alert) throws 
    															PatientNotFoundException,
    															InvalidMedEntryAlertException {
    	Patient p = findPatient(patientID);
    	Medicalhistory hstr = p.getMedicalhistory();
    	
    	MedicalhistoryentryPK id = new MedicalhistoryentryPK();
    	id.setAdded(new Date());
    	id.setId(hstr.getId());
    	Medicalhistoryentry entry = new Medicalhistoryentry();
    	entry.setAlert(alert);
    	entry.setComments(comment);
    	entry.setId(id);
    	
    	hstr.addMedicalEntry(entry);
    	emgr.update(hstr);
    	return entry;
    }
    
    public void deleteMedicalEntry(Medicalhistoryentry entry) throws PatientNotFoundException {
    	Patient p = findPatient(entry.getId().getId());
    	p.getMedicalhistory().deleteMedicalEntry(entry);
    	emgr.delete(entry);
    }

    public Contactinfo createContactinfo(int patientID, String info, int type) throws 
    																CloudentException {
    	Patient p = findPatient(patientID);
    	ContactinfoPK id = new ContactinfoPK(); 
		id.setId(p.getId());
		id.setInfotype(type);

		Contactinfo cnt = new Contactinfo();
		cnt.setInfo(info);
		cnt.setId(id);
		cnt.setPatient(p);
		p.addContactInfo(cnt);
		
		emgr.persist(cnt);
		return cnt;
    }

    public Address createAddress(int patientID, Address adr) throws Exception {
      	Patient p = findPatient(patientID);
      	//Address object enforces valid id.addressType , no need to check...
    	adr.getId().setId(patientID);
    	adr.setPatient(p);
    	p.addAddress(adr);
    	emgr.persist(adr);
    	return adr;
    }

}
