package com.mpdmal.cloudental.web.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.beans.DentistBean;
import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.PatientExistsException;

public class PatientManagementBean extends Patient {

	UserHolder user = (UserHolder)FacesContext.getCurrentInstance().
			getExternalContext().getSessionMap().get("userHolder");


	private static final long serialVersionUID = 1L;

	@EJB
	DentistServices dentistService;
	@EJB
	PatientServices patientServices;

	@EJB
	DentistBean dentistBean;

	public Vector<Patient> getPatientList() {

		Vector<Patient>  ans  = (Vector<Patient>) dentistService.getPatientlist(user.getCurrentUser().getId());
		return  ans;
	}

	public String createPatient(){

		try {
			dentistService.createPatient(user.getCurrentUser().getId() , getName(), getSurname());
		} catch (PatientExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DentistNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("create: "+getSurname());
		return null;
	}

	public String updatePatient(){
		System.out.println("update: "+getSurname());
//		dentistService.update(this);
		return null;
	}

	public void setCurrentPatient(Patient p){
		System.out.println("setCurrentPatient: "+p.getSurname());
		setName(p.getName());
		setSurname(p.getSurname());
		setComments(p.getComments());
	}
	
	public List<Patient> completePatient(String query) {  
    	System.out.println("completePatient: ");
    	Vector<Patient>  patients  = (Vector<Patient>) dentistService.getPatientlist(user.getCurrentUser().getId());
        List<Patient> suggestions = new ArrayList<Patient>();  
          
        for(Patient p : patients) {  
        	String name =  p.getName();
        	String surname = p.getSurname();
        	name = name.toLowerCase();
        	surname =surname.toLowerCase();
        	query = query.toLowerCase(); 
        	
            if(name.startsWith(query) || surname.startsWith(query) )  
                suggestions.add(p);  
        }  
          
        return suggestions;  
    }  








}
