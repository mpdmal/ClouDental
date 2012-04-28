package com.mpdmal.cloudental.beans;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.jws.WebService;

import com.mpdmal.cloudental.dao.DentistDAO;
import com.mpdmal.cloudental.dao.DiscountDAO;
import com.mpdmal.cloudental.dao.PatientDAO;
import com.mpdmal.cloudental.dao.PatientToothDAO;
import com.mpdmal.cloudental.dao.PostitDAO;
import com.mpdmal.cloudental.dao.PricelistDAO;
import com.mpdmal.cloudental.dao.TeethDAO;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.Medicalhistory;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.Patienthistory;
import com.mpdmal.cloudental.entities.Patienttooth;
import com.mpdmal.cloudental.entities.PatienttoothPK;
import com.mpdmal.cloudental.entities.Postit;
import com.mpdmal.cloudental.entities.PostitPK;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.entities.Tooth;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;
import com.mpdmal.cloudental.util.exception.PatientAlreadyExistsException;

@Named
@Stateless
@LocalBean
@WebService
public class DentistServices {
	@EJB
	private DentistDAO _dentistdao;
	@EJB
	private PostitDAO _postitdao;
	@EJB
	private PatientDAO _pdao;
	@EJB
	private DiscountDAO _discountdao;
	@EJB
	private PricelistDAO _pcdao;
	@EJB
	private TeethDAO _tdao;
	@EJB
	private PatientToothDAO _ptcdao;
	
	public DentistServices() {}
	
	public void setDentistDao (DentistDAO dao) { _dentistdao = dao;}//for testing	
	public void setPatientDao (PatientDAO dao) { _pdao = dao;}//for testing
	public void setPostitDao (PostitDAO dao) { _postitdao = dao;}//for testing
	public void setDiscountDao (DiscountDAO dao) { _discountdao= dao;}//for testing
	public void setPricelistDao (PricelistDAO dao) { _pcdao = dao;}//for testing
	public void setTeethDao (TeethDAO dao) { _tdao= dao;}//for testing
	public void setPatientTeethDao (PatientToothDAO dao) { _ptcdao = dao;}//for testing
	
	public long countNotes() {		return _postitdao.countPostits();	};
	public long countNotes(String dentistid) {		return _postitdao.countPostits(dentistid);	};
	public long countDiscounts (String dentistid) {		return _discountdao.countDiscounts(dentistid);	}
	public long countDiscounts () {		return _discountdao.countDiscounts();	}
	public long countPricelistItems () { return _pcdao.countPricelistItems(); }
	public long countPricelistItems (String dentistid) { return _pcdao.countPricelistItems(dentistid); }
	
	public Vector<PricelistItem> getPricelistItems() {
    	return _pcdao.getPricelistItems();
    }
    
	public Vector<PricelistItem> getPricelistItems(String username) {
    	return _pcdao.getPricelistItems(username);
    }

	public Vector<Discount> getDiscounts() {
    	return _discountdao.getDiscounts();
    }
    
	public Vector<Discount> getDiscounts(String username) {
		return _discountdao.getDiscounts(username);
    }

	public Postit createNote(String dentistid, String note, int type) throws InvalidPostitAlertException {
		Dentist dentist = _dentistdao.getDentist(dentistid);
		if (dentist == null) {
    		CloudentUtils.logWarning("Dentist does not exist:"+dentistid+", postit bined:"+note);
			return null;
		}
		PostitPK id = new PostitPK();
		id.setId(dentistid);
		id.setPostdate(new Date());

		Postit postit = new Postit();
		postit.setId(id);
		postit.setPost(note);
		postit.setAlert(type);
		
		postit.setDentist(dentist);
		dentist.addNote(postit);
		
		_postitdao.updateCreate(postit, false);
		return postit;
	}
	
	public Discount createDiscount(String dentistid, String title, String description, double value) throws InvalidPostitAlertException {
		Dentist dentist = _dentistdao.getDentist(dentistid);
		if (dentist == null) {
    		CloudentUtils.logWarning("Dentist does not exist:"+dentistid+", discount bined:"+title);
			return null;
		}
		Discount ds = new Discount();
		ds.setDescription(description);
		ds.setTitle(title);
		ds.setDiscount(BigDecimal.valueOf(value));
		ds.setDentist(dentist);
		dentist.addDiscount(ds);
		_discountdao.updateCreate(ds, false);
		return ds;
	}

	public PricelistItem createPricelistItem(String dentistid, String title, String description, double value) throws InvalidPostitAlertException {
		Dentist dentist = _dentistdao.getDentist(dentistid);
		if (dentist == null) {
    		CloudentUtils.logWarning("Dentist does not exist:"+dentistid+", pc item bined:"+title);
			return null;
		}
		PricelistItem item = new PricelistItem();
		item.setDescription(description);
		item.setTitle(title);
		item.setPrice(BigDecimal.valueOf(value));
		item.setDentist(dentist);
		dentist.addPricelistItem(item);
		_pcdao.updateCreate(item, false);
		return item;
	}

	public Patient createPatient(String dentistid, String name, String surname) throws PatientAlreadyExistsException {
		Dentist dentist = _dentistdao.getDentist(dentistid);
		if (dentist == null) {
			CloudentUtils.logError("Dentist does not exist, cannot create patient:"+name);
			return null;
		}
		Patient p = _pdao.getPatient(dentistid,name, surname); 
		if (p != null) {
			throw new PatientAlreadyExistsException(p.getId());
		}
		//patient
		p = new Patient();
		p.setCreated(new Timestamp(System.currentTimeMillis()));
		p.setName(name);
		p.setSurname(surname);

		//auto generate medical history
		Medicalhistory medhistory = new Medicalhistory();
		medhistory.setComments("Auto Generated");
		medhistory.setPatient(p);
		
		//auto generate med history
		Patienthistory dentalhistory = new Patienthistory();
		dentalhistory.setComments("auto generated");
		dentalhistory.setStartdate(new Timestamp(System.currentTimeMillis()));
		dentalhistory.setPatient(p);

		p.setDentist(dentist);
		p.setMedicalhistory(medhistory);
		p.setDentalhistory(dentalhistory);
		dentist.addPatient(p);

		_pdao.updateCreate(p, false);
//		_dentistdao.updateCreate(dentist, true);
		
		for (Tooth tooth : _tdao.getDefaultTeethSet()) {
			Patienttooth t = new Patienttooth();
			t.setComments("");
			t.setTooth(tooth);
			t.setPatient(p);
			p.addTooth(t);
			
			PatienttoothPK id = new PatienttoothPK();
			id.setPatientid(p.getId());
			id.setToothid(tooth.getPosition());
			
			t.setId(id);
		}

		return p;
    }

}
