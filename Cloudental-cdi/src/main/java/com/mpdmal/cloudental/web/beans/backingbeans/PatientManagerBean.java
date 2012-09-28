package com.mpdmal.cloudental.web.beans.backingbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.PatientNotFoundException;
import com.mpdmal.cloudental.web.controllers.Office;
import com.mpdmal.cloudental.web.util.CloudentWebUtils;

public class PatientManagerBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//MODEL
	Office office;
	Vector<Patient> patientList;
	Patient selectedPatient, createPatient;
	
	TreeNode root, selectedPatientNode;
	
	public PatientManagerBean(Office office) {
		this.office = office;
		createPatient = new Patient();
	}

	//GETTERS/SETTERS
	public Patient getCreatePatient() {		return createPatient;	}
	public TreeNode getPatientListRoot() {	return root;	}
	public Patient getSelectedPatient() {	return selectedPatient; }
	public Vector<Patient> getPatientList() {	return patientList;	}
	public TreeNode getSelectedPatientNode() {	return selectedPatientNode;	}
	
	public void setSelectedPatientNode(TreeNode nd ) {	this.selectedPatientNode = nd;	}
	public void setSelectedPatient(Patient patient) {	
		System.out.println("set selected:"+patient.getSurname());
		this.selectedPatient = patient;
		//createPatientTreeStructure();
	}
	public void setCreatePatient(Patient patient) {	this.createPatient = patient;	}
  
	//INTERFACE
	public void populatePatients (int dentistid) {
		System.out.println("patient list for "+dentistid);
		patientList = (Vector<Patient>) office.getDentistServices().getPatientlist(dentistid);
		//createPatientTreeStructure();
	}
	
	public String createPatient() {
		System.out.println("!create patient!");
		if (createPatient == null) {
			CloudentUtils.logWarning("cannot create null patient");
			return null;
		}

		try {
			office.getDentistServices().createPatient(office.getOwnerID(), getCreatePatient().getName(), getCreatePatient().getSurname());
			populatePatients(office.getOwnerID());
		} catch (Exception e) {
			CloudentWebUtils.showJSFErrorMessage(e.getMessage());
			e.printStackTrace();
		} 
		return null;
	}
	
	public void deletePatient() {
		System.out.println("!delete patient!");
		if (selectedPatient == null) {
			CloudentUtils.logWarning("cannot delete null patient, make a selection first");
			return;
		}
		
		try {
			office.getDentistServices().deletePatient(selectedPatient.getId());
			populatePatients(office.getOwnerID());
		} catch (PatientNotFoundException e) {
			CloudentWebUtils.showJSFErrorMessage(e.getMessage());
			e.printStackTrace();
		}
	}
	public List<Patient> completePatient(String query) {  
    	System.out.println("completePatient: ");
    	Vector<Patient>  patients  = (Vector<Patient>) office.getPatientManagment().getPatientList();
        List<Patient> suggestions = new ArrayList<Patient>();  
          
        for(Patient p : patients) {  
        	query = query.toLowerCase(); // case insensitive 
            if(p.getName().toLowerCase().startsWith(query) ||
           		p.getSurname().toLowerCase().startsWith(query) )  
                	suggestions.add(p);  
        }  
          
        return suggestions;  
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
