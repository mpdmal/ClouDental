package com.mpdmal.cloudental.web.beans.backingbeans;

import java.io.Serializable;
import java.util.Vector;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.web.controllers.Office;

public class PatientManagerBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//MODEL
	DentistServices dentistService;
	PatientServices patientServices;
	Office office;
	Vector<Patient> patientList;
	Patient selectedPatient, createPatient;
	
	
	public PatientManagerBean(Office office, DentistServices dsvc, PatientServices psvc) {
		dentistService = dsvc;
		patientServices = psvc;
		this.office = office;
		createPatient = new Patient();
	}

	//GETTERS/SETTERS
	public Vector<Patient> getPatientList() {	return patientList;	}
	public Patient getSelectedPatient() {	return selectedPatient;	}
	public Patient getCreatePatient() {	return createPatient;	}

	public void setSelectedPatient(Patient patient) {	this.selectedPatient = patient;	}
	public void setCreatePatient(Patient patient) {	this.createPatient = patient;	}

	//INTERFACE
	public void populatePatients (int dentistid) {
		patientList = (Vector<Patient>) dentistService.getPatientlist(dentistid);
	}
	
	public String createPatient() {
		try {
			dentistService.createPatient(office.getOwnerID(), getCreatePatient().getName(), getCreatePatient().getSurname());
			populatePatients(office.getOwnerID());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR, e.getMessage(),"" ));
			e.printStackTrace();
		} 
		return null;
	}
}

