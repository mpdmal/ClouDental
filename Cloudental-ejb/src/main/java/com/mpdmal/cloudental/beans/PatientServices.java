package com.mpdmal.cloudental.beans;

import java.math.BigDecimal;
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
		
		emgr.update(act);
	}

   
    @SuppressWarnings("unchecked")
	public Vector<Activity> getPatientActivities (int patientid) throws PatientNotFoundException {
		Patient p = findPatient(patientid);

    	Query q = emgr.getEM().
    			createQuery("select ac from Activity ac where ac.patienthistory.patient.id =:patientid").
    			setParameter("patientid", p.getId());
        return (Vector<Activity>) emgr.executeMultipleObjectQuery(q);
    }

    //SERVICES
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
    
  /*
    public Vector<Activity> getActivitiesByDate (int patientID, Date from , Date to) throws PatientNotFoundException {
    	Patient p = _pdao.getPatient(patientID);
    	if (p == null) 
    		throw new PatientNotFoundException(patientID, "Cannot get Activities:");
    	return _acvdao.getActivities(p.getId(), from , to);
    }

    public void deleteActivitiesByDate (int patientID, Date from , Date to) throws PatientNotFoundException {
    	Patient p = _pdao.getPatient(patientID);
    	if (p == null) 
    		throw new PatientNotFoundException (patientID, "Cannot delete Activities:");
    	
    	for (Activity acv : _acvdao.getActivities(p.getId(),from , to)) {
			_acvdao.delete(acv);
		}
    }
*/

    //VISITS
    public Visit createVisit (int activityID, String description,
    							String title, Date start, Date end, double deposit,
    							int color ) throws CloudentException {
    	Activity act = findActivity(activityID);
    	validateVisit(act, start, end);
    	
		Visit v = new Visit();
		v.setComments(description);
		v.setVisitdate(start);
		v.setEnddate(end);
		v.setColor(color);
		v.setTitle(title);
		v.setDeposit(BigDecimal.valueOf(deposit));
		v.setActivity(act);
		act.addVisit(v);
		
		emgr.persist(v);
		return v;
    }
    
    @SuppressWarnings("unchecked")
	public Vector<Visit> getActivityVisits (int activityid) throws ActivityNotFoundException {
    	Query q = emgr.getEM().
    			createQuery("select v from Visit v where v.activity.id =:activityid").
    			setParameter("activityid", activityid);
        return (Vector<Visit>) emgr.executeMultipleObjectQuery(q);
    }

    @SuppressWarnings("unchecked")
	public Vector<Visit> getPatientVisits (int patientid) throws ActivityNotFoundException {
    	Query q = emgr.getEM().
    			createQuery("select v from Visit v where v.activity.patienthistory.patient.id =:patientid").
    			setParameter("patientid", patientid);
        return (Vector<Visit>) emgr.executeMultipleObjectQuery(q);
    }

    public long countVisits() {
    	Query q = emgr.getEM().createQuery("select count(v) from Visit v");
        return emgr.executeSingleLongQuery(q);
    }

    public long countActivityVisits(int activityid) throws ActivityNotFoundException {
    	Query q = emgr.getEM().
    			createQuery("select count(v) from Visit v where v.activity.id =:activityid").
    			setParameter("activityid", activityid);
        return emgr.executeSingleLongQuery(q);
    }

    public long countPatientVisits(int patientid) {
    	Query q = emgr.getEM().
    			createQuery("select count(v) from Visit v where v.activity.patienthistory.patient.id =:patientid").
    			setParameter("patientid", patientid);
        return emgr.executeSingleLongQuery(q);
    }


/*
    public void deleteVisits (int activityid) throws PatientNotFoundException {
    	Activity acvt = _acvdao.getActivity(activityid);
    	if (acvt == null) 
    		throw new PatientNotFoundException (activityid, "Cannot delete Visits");
    	
    	for (Visit v : acvt.getVisits()) {
			_vdao.delete(v);
		}
    }*/    
    
    
    //PRIVATE
    private void validateVisit(Activity act, Date start, Date end) throws ValidationException, ActivityNotFoundException {
    	long acstart = act.getStartdate().getTime();
    	long acend = act.getEnddate().getTime();
    	long vtstart  = start.getTime();
    	long vtend = end.getTime();

    	//visit dates should make sense ..
    	if (vtend <= vtstart)
    		throw new ValidationException("Visit END date must come after the start date");
    	
    	//visit dates should be within activity dates
    	if (acstart > vtstart || acend < vtend) 
    		throw new ValidationException("Visit START and END date must be within the respective Activity dates");
    	

    	//if first visit we are good
    	if (act.getVisits().size() <= 0)
    		return;

    	//visits cannot overlap one another,try and find a spot
    	//among PATIENT visits NOT just ACTIVITY visits
    	boolean invalid = false;
    	for (Visit vt : getPatientVisits(act.getPatienthistory().getPatient().getId())) {
			if (vtend < vt.getVisitdate().getTime() || vtstart > vt.getEnddate().getTime())
				continue;
			invalid = true;
		}
    	//overlaps some existing visit 
    	if (invalid)
    		throw new ValidationException("Visit dates cannot overlap one another");
    }
}