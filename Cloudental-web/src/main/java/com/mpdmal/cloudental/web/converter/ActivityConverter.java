package com.mpdmal.cloudental.web.converter;


import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Activity;

  
public class ActivityConverter implements Converter {  
  
	@EJB
	PatientServices patientServices;  
  
   
  
    public Activity getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {  
    	System.out.println("Activity getAsObject");
    	Activity activity  = null;
    	
        if (submittedValue.trim().equals("")) {  
            return null;  
        } else {  
            try {
            	System.out.println("submittedValue:" +submittedValue );
                int id = Integer.parseInt(submittedValue); 
//                activity = patientServices.getActivitiesById(id);
                activity = new Activity();
                activity.setId(id);
                activity.setDescription("a2");
                
  
            } catch(NumberFormatException exception) {  
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid patient"));  
            }  
        }  
  
        return activity;  
    }  
  
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {  
    	
        if (value == null || value.equals("")) {  
            return "";  
        } else {  
        	String id = ( (Activity) value).getId().toString();
        	System.out.println("ActivityConverter getAsString "+id);
        	
            return String.valueOf(  id );  
        }  
    }  
}  