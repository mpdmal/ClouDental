package com.mpdmal.cloudental.web.converter;

import java.util.Collection;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.web.beans.PricelistHolder;

@FacesConverter (forClass=PricelistItemConverter.class, value="pricelistItemConverter")
public class PricelistItemConverter implements Converter {
	
	@Inject
	DentistServices dentistService;
	
//	private Collection<PricelistItem> pricelistItemCollection; 


	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {  
		//System.out.println("PricelistItemConverter getAsObject");
		
		
		if (submittedValue.trim().equals("")) {  
			return null;  
		} else {  
			try {
				System.out.println("submittedValue" +submittedValue );
				int id = Integer.parseInt(submittedValue); 

				Collection<PricelistItem>  pricelist = getPricelist();
				for (PricelistItem p : pricelist ) {  
					if (p.getId() == id) {  
						return p;  
					}  
				}  

			} catch(NumberFormatException exception) {  
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid PricelistItem"));  
			}  
		}  

		return null;  
	}  

	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {  
		//System.out.println("PricelistItemConverter getAsString");
		if (value == null || value.equals("")) {  
			return "";  
		} else {  
			String id = ( (PricelistItem) value).getId().toString();
			//System.out.println("PricelistItem getAsString "+id);

			return String.valueOf(  id );  
		}  
	}
	public Collection<PricelistItem> getPricelist() {
		FacesContext context=FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		PricelistHolder p = (PricelistHolder)app.evaluateExpressionGet(context, "#{pricelistHolder}", PricelistHolder.class);
		return  p.getPricelist();
	}
}  