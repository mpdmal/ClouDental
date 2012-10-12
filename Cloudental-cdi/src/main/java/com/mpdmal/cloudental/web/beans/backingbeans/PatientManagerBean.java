package com.mpdmal.cloudental.web.beans.backingbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.mpdmal.cloudental.entities.Address;
import com.mpdmal.cloudental.entities.AddressPK;
import com.mpdmal.cloudental.entities.Contactinfo;
import com.mpdmal.cloudental.entities.ContactinfoPK;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.PatientNotFoundException;
import com.mpdmal.cloudental.util.exception.base.CloudentException;
import com.mpdmal.cloudental.web.controllers.Office;
import com.mpdmal.cloudental.web.util.CloudentWebUtils;

public class PatientManagerBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//MODEL
	private Office _office;
	private Vector<Patient> _patientList;
	private Patient _selectedPatient, _createPatient;
	private Address _createAdrs;
	private Contactinfo _createCInfo;
	
	public PatientManagerBean(Office office) {
		this._office = office;
		_createPatient = new Patient();
		
		_createAdrs = new Address();
		_createAdrs.setCountry("Greece");
		AddressPK id1 = new AddressPK();
		
		_createCInfo = new Contactinfo();
		ContactinfoPK id2 = new ContactinfoPK();
		
		try {
			id1.setAdrstype(CloudentUtils.AddressType.HOME.getValue());
			id2.setInfotype(CloudentUtils.ContactInfoType.EMAIL.getValue());
		} catch (CloudentException e) {
			e.printStackTrace();
		}
		_createAdrs.setId(id1);
		_createCInfo.setId(id2);
	}

	//GETTERS/SETTERS
	public Patient getCreatePatient() {		return _createPatient;	}
	public Address getCreateAddress() {		return _createAdrs; }
	public Contactinfo getCreateContactInfo() {		return _createCInfo; }
	public Patient getSelectedPatient() {	return _selectedPatient; }
	public Vector<Patient> getPatientList() {	return _patientList;	}
	
	public void setSelectedPatient(Patient patient) {	
		System.out.println("set selected:"+patient.getSurname());
		this._selectedPatient = patient;
		//createPatientTreeStructure();
	}
	public void setCreatePatient(Patient patient) {	this._createPatient = patient;	}
	public void setCreateAddress(Address adrs) {	this._createAdrs = adrs;	}
	public void setCreateContactInfo(Contactinfo _createCInfo) {	this._createCInfo = _createCInfo;	}
	//INTERFACE
	public void populatePatients (int dentistid) {
		_patientList = (Vector<Patient>) _office.getDentistServices().getPatientlist(dentistid);
		if (_patientList.size() > 0)
			_selectedPatient = _patientList.elementAt(0);
		else
			_selectedPatient = new Patient();
		//createPatientTreeStructure();
	}

	public String createContactInfo () {
		System.out.println("!create cInfo!");
		if (_createCInfo == null) {
			CloudentUtils.logWarning("cannot create null contact info");
			return null;
		}
		try {
			_createCInfo.getId().setId(_selectedPatient.getId());
			_office.getPatientServices().createContactinfo(_createCInfo);
			populatePatients(_office.getOwnerID());
		} catch (CloudentException e) {
			CloudentWebUtils.showJSFErrorMessage(e.getMessage());
			e.printStackTrace();
		} 
		return null;
 	}
	
	public String createAddress() {
		System.out.println("!create address!");
		if (_createAdrs == null) {
			CloudentUtils.logWarning("cannot create null address");
			return null;
		}

		try {
			_createAdrs.getId().setId(_selectedPatient.getId());
			_office.getPatientServices().createAddress(_createAdrs);
			populatePatients(_office.getOwnerID());
		} catch (CloudentException e) {
			CloudentWebUtils.showJSFErrorMessage(e.getMessage());
			e.printStackTrace();
		} 
		return null;
	}

	public String createPatient() {
		System.out.println("!create patient!");
		if (_createPatient == null) {
			CloudentUtils.logWarning("cannot create null patient");
			return null;
		}

		try {
			_office.getDentistServices().createPatient(_office.getOwnerID(), getCreatePatient().getName(), getCreatePatient().getSurname());
			populatePatients(_office.getOwnerID());
		} catch (Exception e) {
			CloudentWebUtils.showJSFErrorMessage(e.getMessage());
			e.printStackTrace();
		} 
		return null;
	}
	
	public void deletePatient() {
		System.out.println("!delete patient!");
		if (_selectedPatient == null) {
			CloudentUtils.logWarning("cannot delete null patient, make a selection first");
			return;
		}
		
		try {
			_office.getDentistServices().deletePatient(_selectedPatient.getId());
			populatePatients(_office.getOwnerID());
			_office.getScheduleControler().populateScheduler(_office.getOwnerID());
		} catch (PatientNotFoundException e) {
			CloudentWebUtils.showJSFErrorMessage(e.getMessage());
			e.printStackTrace();
		}
	}
	public List<Patient> completePatient(String query) {  
    	Vector<Patient>  patients  = (Vector<Patient>) _office.getPatientManagment().getPatientList();
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
	/*
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
	*/
}
