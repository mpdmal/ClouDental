package com.mpdmal.cloudental.web.beans.backingbeans;

import java.io.Serializable;
import java.util.Vector;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.web.controllers.Office;

public class PatientManagerBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//MODEL
	DentistServices dentistService;
	PatientServices patientServices;
	Office office;
	Vector<Patient> patientList;
	Patient selectedPatient, createPatient;
	
	TreeNode root, selectedPatientNode;
	
	public PatientManagerBean(Office office, DentistServices dsvc, PatientServices psvc) {
		dentistService = dsvc;
		patientServices = psvc;
		this.office = office;
		createPatient = new Patient();
	}

	//GETTERS/SETTERS
	public TreeNode getPatientListRoot() {	return root;	}
	public Vector<Patient> getPatientList() {	return patientList;	}
	public Patient getSelectedPatient() {	System.out.println("!!");return selectedPatient;	}
	public Patient getCreatePatient() {	return createPatient;	}
	public TreeNode getSelectedPatientNode() {	return selectedPatientNode;	}
	
	public void setSelectedPatientNode(TreeNode nd ) {	this.selectedPatientNode = nd;	}
	public void setSelectedPatient(Patient patient) {	
		System.out.println("!@#!@#!@#!@#!@#!@#!@#!@#!@#!#!@#!@#");
		this.selectedPatient = patient;
		createPatientTreeStructure();
	}
	public void setCreatePatient(Patient patient) {	this.createPatient = patient;	}
  
	//INTERFACE
	public void populatePatients (int dentistid) {
		patientList = (Vector<Patient>) dentistService.getPatientlist(dentistid);
		createPatientTreeStructure();
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
	
	//PRIVATE 
	private void createPatientTreeStructure() {
		root = new DefaultTreeNode("Root", null);
		if (selectedPatient == null) {
			CloudentUtils.logWarning("CREATING TREE FOR NO PATIENT");
			new DefaultTreeNode("No Patient Selected ...", root);
			return;
		}
		CloudentUtils.logWarning("CREATING TREE FOR PATIENT :"+selectedPatient.getId());
		for (Activity activity  : selectedPatient.getDentalHistory().getActivities()) {
			new DefaultTreeNode(activity.getDescription(), root);
		}
	}
}
