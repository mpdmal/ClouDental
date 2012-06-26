package com.mpdmal.cloudental.web.beans;

import java.util.Vector;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Medicalhistoryentry;
import com.mpdmal.cloudental.entities.Patient;


public class MedicalHistoryManagementBean {
	private Patient currentPatient;



	@Inject
	PatientServices patientService;

	private Integer patientId;
	private String comment;
	private int alert;




	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getAlert() {
		return alert;
	}

	public void setAlert(int alert) {
		this.alert = alert;
	}



	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String createMedicalHistory(){
		try{

			setCurrentPatient(currentPatient);
//			patientService.createMedicalHistoryEntry(currentPatient.getId(), comment, alert);
		}catch(Exception  e){
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Medical History failed to be created", null));
		}
		return null;
	}

	public String updateMedicalHistory(){
		System.out.println("Update Medical Histroy: ");

		return null;
	}

	public void setCurrentPatient(Patient p){
		System.out.println("setCurrentPatient: "+p.getSurname());
		setPatientId(p.getId());
		currentPatient = p;
	}
	public Patient getCurrentPatient() {
		return currentPatient;
	}

	public Vector<Medicalhistoryentry> getMedicalHistoryList(){
		Vector<Medicalhistoryentry> v = new Vector<Medicalhistoryentry>(); 
		return v;

	}



}
