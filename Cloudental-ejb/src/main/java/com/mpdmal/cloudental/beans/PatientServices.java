package com.mpdmal.cloudental.beans;

import java.util.Date;
import java.util.Vector;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.jws.WebService;
import javax.persistence.Query;

import com.mpdmal.cloudental.EaoManager;
import com.mpdmal.cloudental.beans.base.AbstractEaoService;
import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.Patienthistory;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.util.exception.DiscountNotFoundException;
import com.mpdmal.cloudental.util.exception.PatientNotFoundException;
import com.mpdmal.cloudental.util.exception.PricelistItemNotFoundException;

@Named
@Stateless
@LocalBean
@WebService
public class PatientServices extends AbstractEaoService {
	@EJB
	private EaoManager emgr;
	
    public PatientServices() {}
    public PatientServices(EaoManager mgr) { 
    	this.emgr = mgr;
    }

    public void close() {
    	emgr.closeEM();
    }

    //ACTIVITIES
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
		ph.addActivity(ac);
		
		emgr.update(ph);
		return ac;
    }

    @SuppressWarnings("unchecked")
	public Vector<Activity> getActivities (int patientid) throws PatientNotFoundException {
		Patient p = findPatient(patientid);

    	Query q = emgr.getEM().
    			createQuery("select av from Activity av where av.patienthistory.patient.id =:patientid").
    			setParameter("patientid", patientid);
        return (Vector<Activity>) emgr.executeMultipleObjectQuery(q);
    }

    /*
    //for OOC testing
    public void setVisitDao (VisitDAO dao) { _vdao = dao; }
    public void setPatientDao (PatientDAO dao) { _pdao = dao;}
    public void setMedhistentryDao(MedicalhistoryentryDAO medentrydao) {_medentrydao = medentrydao; }
    public void setActivityDao(ActivityDAO dao) { _acvdao = dao;}
    public void setDentistDao(DentistDAO dao) {_ddao = dao; }

    //SERVICES
	public Vector<Patient> getPatients (String dentistid) {
		Dentist dentist = _ddao.getDentist(dentistid);
		if (dentist == null) {
			CloudentUtils.logError("Dentist does not exist:"+dentistid);
			return null;
		}
		return _pdao.getPatients(dentistid);
	}

    public Medicalhistoryentry createMedicalHistoryEntry(int patientID, String comment, int alert) throws 
    															PatientNotFoundException,
    															InvalidMedEntryAlertException {
    	Patient p = _pdao.getPatient(patientID);
    	if (p == null) 
    		throw new PatientNotFoundException(patientID, "No such Patient/MedHistory cannot add entry");

    	MedicalhistoryentryPK id = new MedicalhistoryentryPK();
    	id.setAdded(new Date());
    	id.setId(p.getId());
    	Medicalhistoryentry entry = new Medicalhistoryentry();
    	entry.setAlert(alert);
    	entry.setComments(comment);
    	entry.setId(id);
    	_medentrydao.updateCreate(entry, false);
    	return entry;
    }
    public Contactinfo createContactinfo(int patientID, String info, int type) throws 
    											PatientNotFoundException, 
    											InvalidContactInfoTypeException {
    	Patient p = _pdao.getPatient(patientID);
    	if (p == null) 
    		throw new PatientNotFoundException(patientID, "Cannot add Contact info "+type+"|"+info);
    	
    	ContactinfoPK id = new ContactinfoPK(); 
		id.setId(p.getId());
		id.setInfotype(type);

		Contactinfo cnt = new Contactinfo();
		cnt.setInfo(info);
		cnt.setId(id);
		p.addContactInfo(cnt);
		_pdao.updateCreate(cnt, true);
		return cnt;
    }

    public Address createAddress(int patientID, Address adr) throws Exception {
    	Patient p = _pdao.getPatient(patientID);
    	if (p == null) 
    		throw new PatientNotFoundException(patientID, "Cannot add Address");

    	if (adr.getId()== null) {
    		CloudentUtils.logError("cannot add Address with no id ..."+adr.getXML());
    		throw new Exception ("cannot add Address with no id for patient:"+patientID);
    	}
    	//Address object enforces valid id.addressType , no need to check...
    	adr.getId().setId(patientID);
    	adr.setPatient(p);
    	p.addAddress(adr);
    	_pdao.updateCreate(p, true);
    	return adr;
    }
    
        //ACTIVITIES

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
