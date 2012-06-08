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
import com.mpdmal.cloudental.web.beans.PatientHolder;
import com.mpdmal.cloudental.web.beans.PatientManagementBean;

@FacesConverter (value="patientConverter", forClass=PatientConverter.class)
public class PatientConverter implements Converter {  
  
    public static List<Patient> patientDB;  
  

  
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {  
    	//System.out.println("PatientConverter getAsObject");
        if (submittedValue.trim().equals("")) {  
            return null;  
        } else {  
            try {
            	System.out.println("submittedValue" +submittedValue );
                int id = Integer.parseInt(submittedValue); 
                
                if(patientDB==null)
                	patientDB = getPatientList();
                for (Patient p : patientDB) {  
                    if (p.getId() == id) {  
                        return p;  
                    }  
                }  
  
            } catch(NumberFormatException exception) {  
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid patient"));  
            }  
        }  
  
        return null;  
    }  
  
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {  
    	
        if (value == null || value.equals("")) {  
            return "";  
        } else {  
        	String id = ( (Patient) value).getId().toString();
        	//System.out.println("PatientConverter getAsString "+id);
        	
            return String.valueOf(  id );  
        }  
    } 
    
    public Vector<Patient> getPatientList() {
		FacesContext context=FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		PatientHolder p = (PatientHolder)app.evaluateExpressionGet(context, "#{patientHolder}", PatientHolder.class);
		return  p.getPatientsList();
	}
}  
