package com.mpdmal.cloudental.web.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.PatientExistsException;
import com.mpdmal.cloudental.util.exception.PatientNotFoundException;
import com.mpdmal.cloudental.util.exception.ValidationException;

public class PatientManagementBean extends Patient {

	UserHolder user = (UserHolder)FacesContext.getCurrentInstance().
			getExternalContext().getSessionMap().get("userHolder");


	private static final long serialVersionUID = 1L;

	@EJB
	DentistServices dentistService;
	@EJB
	PatientServices patientServices;
	
	Patient selectedPatient;
	
	private Vector<Patient> patientList; 
	
	public Vector<Patient> getPatientList() {
		
		if(patientList==null){
			System.out.println("call service getPatientList");	
			patientList  = (Vector<Patient>) dentistService.getPatientlist(user.getCurrentUser().getId());
		}
		return  patientList;
	}

	public String createPatient() throws ValidationException{
		System.out.println("create: "+getSurname());
		try {
			dentistService.createPatient(user.getCurrentUser().getId() , getName(), getSurname() );
		} catch (PatientExistsException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR, e.getMessage(),"" ));
			e.printStackTrace();
		} catch (DentistNotFoundException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR, e.getMessage(),"" ));
			e.printStackTrace();
		}
		return null;
	}
	
	public String deletePatient(){
		System.out.println("deletePatient: "+getSelectedItem().getName());
		try {
			dentistService.deletePatient(getSelectedItem().getId() );
			patientList  = (Vector<Patient>) dentistService.getPatientlist(user.getCurrentUser().getId());
			
		}catch (PatientNotFoundException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR, e.getMessage(),"" ));
			e.printStackTrace();
		}
		return null;
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
	

	public void setCurrentPatient(Patient p){
		System.out.println("setCurrentPatient: "+p.getSurname());
		setName(p.getName());
		setSurname(p.getSurname());
		setComments(p.getComments());
	}
	
	
	public void rowEditListener(org.primefaces.event.RowEditEvent ev){
		System.out.println("PatientManagementBean rowEditListener()");
        try {
        	Patient selectedRow = (Patient)ev.getObject();
            if(selectedRow.getName().equalsIgnoreCase("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid selection", null));
            } else {
            	System.out.println("update patient "+selectedRow.getName());
               // dentistService.updatePatient(selectedRow.getId(),selectedRow );
            }
        } catch (Exception e) {
            System.out.println("rowEditListener ERROR = " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRORE", e.toString()));
        }        
    }

    public Patient getSelectedItem() {  
    	if(selectedPatient!=null)
    		System.out.println("getSelectedItem: "+selectedPatient.getName());
    	else
    		System.out.println("getSelectedItem: is null");
    		
        return selectedPatient;  
    }  
    public void setSelectedItem(Patient patient) {  
    	System.out.println("setSelectedItem: "+patient.getName());
        this.selectedPatient = patient;  
    } 
    
    public String viewActivities(){
    	System.out.println("viewActivities");
		FacesContext context = FacesContext.getCurrentInstance();
		ActivityManagementBean activityManagementBean= (ActivityManagementBean)context.getApplication() .evaluateExpressionGet(context, "#{activityManagementBean}", ActivityManagementBean.class);
		activityManagementBean.setSelectedPatient(getSelectedItem());
//		activityManagementBean.updateActivityList();
    	return "viewActivities";
    }
	
	








}
