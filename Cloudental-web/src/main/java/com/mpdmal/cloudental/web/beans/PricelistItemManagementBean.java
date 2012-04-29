package com.mpdmal.cloudental.web.beans;

import java.util.Vector;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.swing.text.DefaultEditorKit.CutAction;

import com.mpdmal.cloudental.beans.DentistBean;
import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;

public class PricelistItemManagementBean extends PricelistItem {
	
	UserHolder user = (UserHolder)FacesContext.getCurrentInstance().
			getExternalContext().getSessionMap().get("userHolder");
	
	private static final long serialVersionUID = 1L;
	@EJB
	DentistServices dentistService;
	@EJB
	PatientServices patientServices;

	@EJB
	DentistBean dentistBean;
	
	public Vector<PricelistItem> getPricelist() {
		Vector<PricelistItem>  ans  = dentistService.getPricelistItems(user.getCurrentUser().getUsername());
		return  ans;
	}

	public String createPricelistItem(){
		System.out.println("create: "+getTitle());
		try {
			dentistService.createPricelistItem(user.getCurrentUser().getUsername(), getTitle(),getDescription(), getPrice().doubleValue() );
		} catch (InvalidPostitAlertException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}

	public String updatePricelistItem(){
		System.out.println("update: "+getTitle());
		return null;
	}

	public void setCurrentPricelistItem(PricelistItem currentItem){
		System.out.println("setCurrentPricelistItem: "+currentItem.getTitle());
		setId(currentItem.getId());
		setTitle(currentItem.getTitle());
		setPrice(currentItem.getPrice());
		setDescription(currentItem.getDescription());
	}



}
