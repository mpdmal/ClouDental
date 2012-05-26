package com.mpdmal.cloudental.web.beans;

import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.util.exception.PricelistItemNotFoundException;

public class PricelistItemManagementBean extends PricelistItem {
	
	UserHolder user = (UserHolder)FacesContext.getCurrentInstance().
			getExternalContext().getSessionMap().get("userHolder");
	
	private static final long serialVersionUID = 1L;
	@EJB
	DentistServices dentistService;
	@EJB
	PatientServices patientServices;
	
	private PricelistItem selectedItem;

	public PricelistItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(PricelistItem selectedItem) {
		this.selectedItem = selectedItem;
	}

	public String createPricelistItem(){
		System.out.println("create PriceListItem: "+getTitle());
		try {
			dentistService.createPricelistItem(user.getCurrentUser().getId(), getTitle(),getDescription(), getPrice().doubleValue() );
			updatePricelist();
		} catch (Exception e) {
//			FacesMessage(FacesMessage.Severity severity, String summary, String detail) 
			FacesMessage message = new  FacesMessage(FacesMessage.SEVERITY_ERROR, "Service unavailable", e.getMessage());
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, message);
			e.printStackTrace();
		} 
		
		return null;
	}


	public String deletePricelistItem(){
		System.out.println("delete PricelistItem: "+getSelectedItem().getTitle());
		try {
			dentistService.deletePricelistItem(getSelectedItem().getId() );
			updatePricelist();
			
		} catch (PricelistItemNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void rowEditListener(org.primefaces.event.RowEditEvent ev){
		System.out.println("PricelistItemManagementBean rowEditListener()");
        try {
        	PricelistItem pricelistItem = (PricelistItem)ev.getObject();
            //CarJpaController ctrlCar = new CarJpaController();
            
            if(pricelistItem == null) {
                System.out.println("PricelistItem is null...");
            } else {
                System.out.println("PricelistItem is not null with id = " + pricelistItem.getId());
                System.out.println( pricelistItem.getTitle());
                System.out.println( pricelistItem.getDescription());
                
            }
            if(pricelistItem.getTitle().equalsIgnoreCase("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No streetname", null));
            } else {
            	
                dentistService.updatePricelistItem(pricelistItem.getId(),pricelistItem.getDescription() ,pricelistItem.getTitle());
                
            }
            
        } catch (Exception e) {
            System.out.println("rowEditListener ERROR = " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRORE", e.toString()));
        }        
    }

	public Collection<PricelistItem> loadPricelist() {
		Collection<PricelistItem> result = null;
		if(user!=null){
			System.out.println("call  service getPricelist()");
			result= dentistService.getPricelist(user.getCurrentUser().getId());
		}
		return result;
	}
	
    private void updatePricelist(){
    	FacesContext context = FacesContext.getCurrentInstance();
    	PricelistHolder pricelistHolder = (PricelistHolder)context.getApplication() .evaluateExpressionGet(context, "#{pricelistHolder}", PricelistHolder.class);
    	pricelistHolder .setPricelist(loadPricelist());
		
    }

}
