package com.mpdmal.cloudental.web.converter;


import java.util.List;
import java.util.Vector;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.mpdmal.cloudental.entities.Patient;

@FacesConverter (value="patientConverter", forClass=PatientConverter.class)
public class PatientConverter implements Converter {  
    public static List<Patient> patientDB;  
  
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {  
        if (submittedValue.length() <= 0)  
            return null;  

        try {
        	int id = Integer.parseInt(submittedValue); 
            if(patientDB==null)
              	patientDB =null; 
            for (Patient p : patientDB)   
            	if (p.getId() == id)   
            		return p;  
 
            } catch(NumberFormatException exception) {  
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid patient"));  
            }  
  
        return null;  
    }  
  
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {  
        if (value == null || value.equals(""))   
            return "";  

        return String.valueOf(((Patient)value).getUIfriendlyForm());  
    } 
}  
