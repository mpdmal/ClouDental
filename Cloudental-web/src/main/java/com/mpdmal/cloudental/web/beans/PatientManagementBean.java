package com.mpdmal.cloudental.web.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.util.exception.PatientNotFoundException;

public class PatientManagementBean extends Patient {

	UserHolder user = (UserHolder)FacesContext.getCurrentInstance().
			getExternalContext().getSessionMap().get("userHolder");


	private static final long serialVersionUID = 1L;

	@Inject
	DentistServices dentistService;
	@Inject
	PatientServices patientServices;
	
	Patient selectedPatient;
	
	public UserHolder getUser() {
		return user;
	}

	public void setUser(UserHolder user) {
		this.user = user;
	}

	public Vector<Patient> loadPatientList() {
		Vector<Patient> result =null;
		if(user!=null){
			System.out.println("PatientManagementBean call service getPatientList");	
			result  = (Vector<Patient>) dentistService.getPatientlist(user.getCurrentUser().getId());
		}
		
		return  result;
	}

	public String createPatient() {
		System.out.println("create: "+getSurname());
		try {
			dentistService.createPatient(user.getCurrentUser().getId() , getName(), getSurname() );
			updatePatientList();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR, e.getMessage(),"" ));
			e.printStackTrace();
		} 
		return null;
	}
	
	public String deletePatient(){
		System.out.println("deletePatient: "+getSelectedPatient().getName());
		try {
			dentistService.deletePatient(getSelectedPatient().getId() );
			updatePatientList();
						
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
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRORE", e.toString()));
        }        
    }

    public Patient getSelectedPatient() {  
        return selectedPatient;  
    }  
    public void setSelectedPatient(Patient patient) {  
        this.selectedPatient = patient;  
    } 
    
    public String viewActivities(){
    	System.out.println("viewActivities");
		FacesContext context = FacesContext.getCurrentInstance();
		ActivityManagementBean activityManagementBean= (ActivityManagementBean)context.getApplication() .evaluateExpressionGet(context, "#{activityManagementBean}", ActivityManagementBean.class);
		activityManagementBean.setSelectedPatient(getSelectedPatient());
//		activityManagementBean.updateActivityList();
    	return "viewActivities";
    }
	
    private void updatePatientList(){
    	FacesContext context = FacesContext.getCurrentInstance();
		PatientHolder patientHolder = (PatientHolder)context.getApplication() .evaluateExpressionGet(context, "#{patientHolder}", PatientHolder.class);
		patientHolder.setPatientsList(loadPatientList());
    }
    
    public void handleSelect(SelectEvent event) {  
    	//System.out.println("PatientManagementBean handleSelect: "+event.getObject().toString());
        //FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected:" + event.getObject().toString(), null);  
        //FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
	








}
