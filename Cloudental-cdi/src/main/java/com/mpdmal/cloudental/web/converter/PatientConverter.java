package com.mpdmal.cloudental.web.converter;


import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

@FacesConverter (value="patientConverter", forClass=PatientConverter.class)
public class PatientConverter implements Converter {  
    public static List<Patient> patientDB;  
  
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals(""))     
            return null;  

        try {
        	return Patient.boxPatient(submittedValue); 
        } catch(CloudentException exception) {  
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid patient"));  
        }  
    }  
  
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {  
        if (value == null || value.equals(""))   
            return "";  

        return ((Patient)value).unboxPatient();  
    } 
}  
