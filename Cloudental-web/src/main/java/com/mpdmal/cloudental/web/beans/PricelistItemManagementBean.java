package com.mpdmal.cloudental.web.beans;

import java.util.Vector;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.beans.DentistBean;
import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
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
		Dentist d = user.getCurrentUser();
		System.out.println(""+d.getName()+d.getId());
		
		Vector<PricelistItem>  ans  = new Vector<PricelistItem>();
		for (PricelistItem item : d.getPriceList()) {
			ans.add(item);
		}
		return  ans;
	}

	public String createPricelistItem(){
		System.out.println("create: "+getTitle());
		try {
			dentistService.createPricelistItem(user.getCurrentUser().getId(), getTitle(),getDescription(), getPrice().doubleValue() );
		} catch (InvalidPostitAlertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DentistNotFoundException e) {
			// TODO Auto-generated catch block
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
