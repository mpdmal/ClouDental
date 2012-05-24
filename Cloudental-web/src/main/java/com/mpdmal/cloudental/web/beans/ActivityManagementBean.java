package com.mpdmal.cloudental.web.beans;

import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.util.exception.PatientNotFoundException;

public class ActivityManagementBean extends Activity{

	private static final long serialVersionUID = 1L;
	
	@EJB
	DentistServices dentistService;
	@EJB
	PatientServices patientServices;
	
	private PricelistItem selectedPricelistItem;
	private Discount selectedDiscount;
	private Patient selectedPatient;
	private Activity selectedActivity;

	public ActivityManagementBean() {
		System.out.println("ActivityManagementBean costructor:" +this.hashCode());
	}

	public String createActivity() throws Exception{
		patientServices.createActivity(selectedPatient.getId(), getDescription(), getStartdate(), getEnddate(), selectedPricelistItem.getId(), selectedDiscount.getId());
		updateActivityList();
		return null;
	}

	public PricelistItem getSelectedPricelistItem() {
		return selectedPricelistItem;
	}

	public void setSelectedPricelistItem(PricelistItem selectedPricelistItem) {
		this.selectedPricelistItem = selectedPricelistItem;
	}

	public Discount getSelectedDiscount() {
		return selectedDiscount;
	}

	public void setSelectedDiscount(Discount selectedDiscount) {
		this.selectedDiscount = selectedDiscount;
	}

	public Patient getSelectedPatient() {
		return selectedPatient;
	}

	public void setSelectedPatient(Patient selectedPatient) {
		this.selectedPatient = selectedPatient;
	}
	
	public Activity getSelectedActivity() {
		return selectedActivity;
	}

	public void setSelectedActivity(Activity selectedActivity) {
		this.selectedActivity = selectedActivity;
	}
	
	public String deleteActivity(){
		System.out.println("deleteActivity: "+getSelectedActivity().getId());
		try {
			//patientServices.deleteActivity(getSelectedActivity().getId());
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Service error",""));
		}
		return null;
	}

	public void rowEditListener(org.primefaces.event.RowEditEvent ev){
		System.out.println("ActivityManagementBean rowEditListener()");
        try {
        	Activity activity = (Activity)ev.getObject();
            if(activity == null) {
                System.out.println("activity to update is null...");
            } else {
                System.out.println("update activity" + activity.getId());
                patientServices.updateActivity(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Service error", e.getMessage()));
        }        
    }

	public String updateActivityList(){
			FacesContext context = FacesContext.getCurrentInstance();
			ActivityHolderBean activityHolderBean = (ActivityHolderBean)context.getApplication() .evaluateExpressionGet(context, "#{activityHolderBean}", ActivityHolderBean.class);
			activityHolderBean.setActivityListOfPatient(loadActivityList());
			return null;
	}
	
	public Vector<Activity> loadActivityList(){
		//Map<String,String> params =       FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		Vector<Activity> result =null;
		if(selectedPatient!=null){
			System.out.println("getActivities for patientID: "+selectedPatient.getId());
			try {
				result = patientServices.getActivities(selectedPatient.getId());
			} catch (PatientNotFoundException e) {
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Service error", e.getMessage()));
			}
		}
		return result;
	}
	
	@PostConstruct
	public void initialize() {
		//System.out.println("ActivityManagementBean PostConstruct");
	}

	
//Test TAG 2
	

}
