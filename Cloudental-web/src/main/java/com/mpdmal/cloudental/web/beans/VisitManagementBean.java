package com.mpdmal.cloudental.web.beans;

import java.util.Vector;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.Visit;
import com.mpdmal.cloudental.util.exception.ActivityNotFoundException;
import com.mpdmal.cloudental.util.exception.VisitNotFoundException;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class VisitManagementBean extends Visit {

	private static final long serialVersionUID = 1L;

	@EJB
	DentistServices dentistService;
	@EJB
	PatientServices patientServices;
	
	UserHolder user = (UserHolder)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userHolder");
	
	private Activity  selectedActivity;
	private Visit selectedVisit;
	private Patient selectedPatient;
	
	
	public Patient getSelectedPatient() {
		return selectedPatient;
	}
	public void setSelectedPatient(Patient selectedPatient) {
		this.selectedPatient = selectedPatient;
	}
	
	public Visit getSelectedVisit() {
		return selectedVisit;
	}
	public void setSelectedVisit(Visit selectedVisit) {
		this.selectedVisit = selectedVisit;
	}
	public Activity getSelectedActivity() {
		return selectedActivity;
	}
	public void setSelectedActivity(Activity selectedActivity) {
		this.selectedActivity = selectedActivity;
	}
	public void loadActivityListOfPatient(){
		FacesContext context = FacesContext.getCurrentInstance();
		ActivityHolderBean activityHolderBean = (ActivityHolderBean)context.getApplication() .evaluateExpressionGet(context, "#{activityHolderBean}", ActivityHolderBean.class);
		activityHolderBean.loadActivityListOfPatient(selectedPatient);
	}


	public Vector<Visit> loadActivityVisits() {
		Vector<Visit> result=null;
		try {
			result= patientServices.getActivityVisits(selectedActivity.getId());
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Service error", e.getMessage()));
		}
		return result;
	}
	
	public Vector<Visit> loadPatientVisits() {
		Vector<Visit> result=null;
		try {
			if(selectedPatient!=null)
				result= patientServices.getPatientVisits(selectedPatient.getId());
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Service error", e.getMessage()));
		}
		return result;
	}
	
	public String createVisit() {
		System.out.println("call sercvice createVisit: ");
		try {
			
			patientServices.createVisit(selectedActivity.getId(), getComments(), getTitle(), getVisitdate(), getEnddate(),getDeposit().doubleValue(),getColor() );
		} catch (CloudentException e) {
			e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Service error", e.getMessage()));
		}
		return null;
	}

	
	public String updateSelectedPatient(){
		System.out.println("updateSelectedPatient: ");
		return null;
	}
	
	public String onPatientSelected(){
		loadActivityListOfPatient();
		return null;
	}
	
	public void rowEditListener(org.primefaces.event.RowEditEvent ev){
		System.out.println("VisitManagementBean rowEditListener()");
        try {
        	Visit visit = (Visit)ev.getObject();
            if(visit == null) {
            } else {
                System.out.println("call service update visit" + visit.getId());
                patientServices.updateVisit(visit);
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Service error", e.getMessage()));
        }        
    }

}
