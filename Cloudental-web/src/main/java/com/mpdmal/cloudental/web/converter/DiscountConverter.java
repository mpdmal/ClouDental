package com.mpdmal.cloudental.web.converter;



import java.util.Vector;

import javax.ejb.EJB;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.web.beans.DiscountManagementBean;


public class DiscountConverter implements Converter {
	
	@EJB
	DentistServices dentistService;
	
	private Vector<Discount> discountVector; 


	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {  
		System.out.println("DiscountConverter getAsObject");
		
		
		if (submittedValue.trim().equals("")) {  
			return null;  
		} else {  
			try {
				System.out.println("submittedValue: " +submittedValue );
				int id = Integer.parseInt(submittedValue); 

				discountVector = getDiscounts();
				for (Discount p : discountVector) {  
					if (p.getId() == id) {
						System.out.println("Returned object: "+p.getXML());
						return p;  
					}  
				}  

			} catch(NumberFormatException exception) {  
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Discount"));  
			}  
		}  

		return null;  
	}  

	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {  
		//System.out.println("DiscountConverter getAsString");
		if (value == null || value.equals("")) {  
			return "";  
		} else {  
			String id = ( (Discount) value).getId().toString();
			//System.out.println("Discount getAsString "+id);

			return String.valueOf(  id );  
		}  
	}
	public Vector<Discount> getDiscounts() {
		FacesContext context=FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		DiscountManagementBean p = (DiscountManagementBean)app.evaluateExpressionGet(context, "#{discountManagementBean}", DiscountManagementBean.class);
		return  p.getDiscounts();
	}
}  