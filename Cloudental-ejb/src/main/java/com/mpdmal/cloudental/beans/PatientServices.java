package com.mpdmal.cloudental.beans;

import java.util.Date;
import java.util.Vector;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.jws.WebService;
import javax.persistence.Query;

import com.mpdmal.cloudental.EaoManager;
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
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.ActivityNotFoundException;
import com.mpdmal.cloudental.util.exception.DiscountNotFoundException;
import com.mpdmal.cloudental.util.exception.InvalidContactInfoTypeException;
import com.mpdmal.cloudental.util.exception.InvalidMedEntryAlertException;
import com.mpdmal.cloudental.util.exception.PatientNotFoundException;
import com.mpdmal.cloudental.util.exception.PricelistItemNotFoundException;

@Named
@Stateless
@LocalBean
@WebService
public class PatientServices extends AbstractEaoService {
    public PatientServices() {}
    public PatientServices(EaoManager mgr)  { 
    	this.emgr = mgr;
    }

    public void close() {
    	emgr.closeEM();
    }

    //ACTIVITIES
    public Activity updateActivity (Activity act) throws 
    										ActivityNotFoundException, 
    										DiscountNotFoundException, 
    										PricelistItemNotFoundException {

    	//validate activity
    	findDiscount(act.getDiscount().getId());
    	findPricable(act.getPriceable().getId());
    	if (act.getEnddate() != null && (act.getEnddate().getTime() <= act.getStartdate().getTime())) {
    		CloudentUtils.logWarning("Activity enddate cannot precede start date, wont update:"+act.getId());
    		return act;
    	}
    	
    	emgr.update(act);
		return act;
    }
    
    public Activity createActivity (int patientid, String description, Date start, Date end, int plitemid, int discountid) 
																throws PatientNotFoundException, 
																DiscountNotFoundException, 
																PricelistItemNotFoundException {
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
		ph.addActivity(ac);
		
		emgr.persist(ac);
		return ac;
    }

    @SuppressWarnings("unchecked")
	public Vector<Activity> getActivities (int patientid) throws PatientNotFoundException {
		Patient p = findPatient(patientid);

    	Query q = emgr.getEM().
    			createQuery("select av from Activity av where av.patienthistory.patient.id =:patientid").
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
    											PatientNotFoundException, 
    											InvalidContactInfoTypeException {
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
    
        //ACTIVITIES
/*
    public Vector<Activity> getActivitiesByDate (int patientID, Date from , Date to) throws PatientNotFoundException {
    	Patient p = _pdao.getPatient(patientID);
    	if (p == null) 
    		throw new PatientNotFoundException(patientID, "Cannot get Activities:");
    	return _acvdao.getActivities(p.getId(), from , to);
    }

    public void deleteActivities (int patientID) throws PatientNotFoundException {
    	Patient p = _pdao.getPatient(patientID);
    	if (p == null) 
    		throw new PatientNotFoundException (patientID, "Cannot delete Activity:");
    	
    	for (Activity acv : _acvdao.getActivities(p.getId())) {
			_acvdao.delete(acv);
		}
    }    
    
    public void deleteActivitiesByDate (int patientID, Date from , Date to) throws PatientNotFoundException {
    	Patient p = _pdao.getPatient(patientID);
    	if (p == null) 
    		throw new PatientNotFoundException (patientID, "Cannot delete Activities:");
    	
    	for (Activity acv : _acvdao.getActivities(p.getId(),from , to)) {
			_acvdao.delete(acv);
		}
    }

    //VISITS
    public Visit createVisit (int activityID, String description,
    							String title, Date start, Date end, double deposit,
    							int color ) throws ActivityNotFoundException {
    	Activity acvt = _acvdao.getActivity(activityID);
    	if (acvt == null)
    		throw new ActivityNotFoundException(activityID, "Cannot create Activity:");
    	
		Visit v = new Visit();
		v.setComments(description);
		v.setVisitdate(start);
		v.setEnddate(end);
		v.setColor(color);
		v.setTitle(title);
		v.setDeposit(BigDecimal.valueOf(deposit));
		v.setActivity(acvt);
		acvt.addVisit(v);
		
		_vdao.updateCreate(v, false);
		return v;
    }

    public void deleteVisits (int activityid) throws PatientNotFoundException {
    	Activity acvt = _acvdao.getActivity(activityid);
    	if (acvt == null) 
    		throw new PatientNotFoundException (activityid, "Cannot delete Visits");
    	
    	for (Visit v : acvt.getVisits()) {
			_vdao.delete(v);
		}
    }    
*/
}
