package com.mpdmal.cloudental.web.beans;

import java.util.Vector;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.PricelistItem;

public class ActivityManagementBean extends Activity{

	private static final long serialVersionUID = 1L;

	UserHolder user = (UserHolder)FacesContext.getCurrentInstance().
			getExternalContext().getSessionMap().get("userHolder");
	
	@EJB
	DentistServices dentistService;
	@EJB
	PatientServices patientServices;
	
	private PricelistItem selectedPricelistItem;
	private Discount selectedDiscount;
	private Patient selectedPatient;
	
	

	public Vector<Activity> getActivityListOfPatient() throws Exception {
		Vector<Activity>  ans  = patientServices.getActivities(selectedPatient.getId());
		return  ans;
	}

	public String createActivity() throws Exception{
		System.out.println("Patient ID:"+ selectedPatient.getId() );
		System.out.println("create activity: "+ getDescription());
		System.out.println("selectedPricelistItem.getId()" +selectedPricelistItem.getId());
		System.out.println("Start Date" +getStartdate().toString() );
		//patientServices.createActivity(selectedPatient.getId(), getDescription(), getStartdate(), getEnddate(), selectedPricelistItem.getId(), selectedDiscount.getId());
		return null;
	}

	public String updateActivity(){
		System.out.println("update activity: "+getId());
//		dentistService.update(this);
		return null;
	}

	public void setCurrentActivity(ActivityManagementBean  a){
//		System.out.println("setCurrentActivity: "+a.getActivityID());
		
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
	

	

}
