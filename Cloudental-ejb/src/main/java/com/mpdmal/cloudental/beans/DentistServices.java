package com.mpdmal.cloudental.beans;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.jws.WebService;

import com.mpdmal.cloudental.dao.DentistDAO;
import com.mpdmal.cloudental.dao.PatientDAO;
import com.mpdmal.cloudental.dao.PostitDAO;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Medicalhistory;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.Patienthistory;
import com.mpdmal.cloudental.entities.Postit;
import com.mpdmal.cloudental.entities.PostitPK;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;

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
	
	public DentistServices() {}
	
	public void setDentistDao (DentistDAO dao) { _dentistdao = dao;}//for testing	
	public void setPatientDao (PatientDAO dao) { _pdao = dao;}//for testing
	public void setPostitDao (PostitDAO dao) { _postitdao = dao;}//for testing
	
	public void createNote(String dentistid, String note, int type) throws InvalidPostitAlertException {
		Dentist dentist = _dentistdao.getDentist(dentistid);
		if (dentist == null) {
    		CloudentUtils.logWarning("Dentist does not exist:"+dentistid+", postit bined:"+note);
			return;
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
	}
	public void createPatient(String dentistid, String name, String surname) {
		Dentist dentist = _dentistdao.getDentist(dentistid);
		if (dentist == null) {
			CloudentUtils.logError("Dentist does not exist, cannot create patient:"+name);
			return;
		}
		if (_pdao.getPatient(dentistid,name, surname) != null) {
			CloudentUtils.logError("Patient already exists"+surname+"|"+name);
			return;
		}
		//patient
		Patient p = new Patient();
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
		
		_dentistdao.updateCreate(dentist, true);
    }

}
