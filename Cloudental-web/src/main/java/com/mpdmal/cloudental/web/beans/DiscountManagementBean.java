package com.mpdmal.cloudental.web.beans;

import java.util.Vector;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.beans.DentistBean;
import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;

public class DiscountManagementBean extends Discount {
	
	UserHolder user = (UserHolder)FacesContext.getCurrentInstance().
			getExternalContext().getSessionMap().get("userHolder");
	
	private static final long serialVersionUID = 1L;
	@EJB
	DentistServices dentistService;
	@EJB
	PatientServices patientServices;

	@EJB
	DentistBean dentistBean;
	
	public Vector<Discount> getDiscounts() {
		Dentist d = user.getCurrentUser();
		return (Vector<Discount>)dentistService.getDiscounts(d.getId());
	}

	public int createDiscount(){
		try {
			Discount d = dentistService.createDiscount(user.getCurrentUser().getId(), getTitle(),getDescription(), getDiscount().doubleValue() );
			return d.getId();
		} catch (InvalidPostitAlertException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public String updateDiscount(){
		System.out.println("update discount : "+getTitle());
		return null;
	}

	public void setCurrentDiscount(Discount d){
		System.out.println("setCurrentPricelistItem: "+d.getTitle());
		setId(d.getId());
		setTitle(d.getTitle());
		setDiscount(d.getDiscount());
		setDescription(d.getDescription());
	}


	public void delete(int id) {
		dentistService.deleteDiscount(id);
	}
}
