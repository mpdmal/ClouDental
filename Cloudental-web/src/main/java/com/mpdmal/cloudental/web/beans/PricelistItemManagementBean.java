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
	
	private Collection<PricelistItem> pricelistCol;
	
	private PricelistItem item ;
	
	public Collection<PricelistItem> getPricelist() {
		Dentist d = user.getCurrentUser();
		System.out.println("getPricelist()");
		if(pricelistCol==null){
			System.out.println("call  service for:"+d.getName()+d.getId());
			pricelistCol = dentistService.getPricelist(d.getId());
		}
		
		return pricelistCol;
		
	}

	public String createPricelistItem(){
		System.out.println("create PriceListItem: "+getTitle());
		try {
			dentistService.createPricelistItem(user.getCurrentUser().getId(), getTitle(),getDescription(), getPrice().doubleValue() );
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return null;
	}

	public String updatePricelistItem(){
		System.out.println("update PricelistItem: "+getTitle());
//		dentistService.updatePricelistItem();
		return null;
	}
	public String deletePricelistItem(){
		System.out.println("delete PricelistItem: "+getSelectedItem().getTitle());
		try {
			dentistService.deletePricelistItem(getSelectedItem().getId() );
			pricelistCol = dentistService.getPricelist(user.getCurrentUser().getId());
		} catch (PricelistItemNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public void setCurrentPricelistItem(PricelistItem currentItem){
		System.out.println("setCurrentPricelistItem: "+currentItem.getTitle());
		setId(currentItem.getId());
		setTitle(currentItem.getTitle());
		setPrice(currentItem.getPrice());
		setDescription(currentItem.getDescription());
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

    public PricelistItem getSelectedItem() {  
    	if(item!=null)
    		System.out.println("getSelectedItem: "+item.getTitle());
    	else
    		System.out.println("getSelectedItem: is null");
    		
        return item;  
    }  
    public void setSelectedItem(PricelistItem item) {  
    	System.out.println("setSelectedItem: "+item.getTitle());
        this.item = item;  
    }  

}
