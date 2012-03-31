package com.mpdmal.cloudental.web.converter;


import java.util.ArrayList;  
import java.util.List;  
import javax.faces.application.FacesMessage;  
  
import javax.faces.component.UIComponent;  
import javax.faces.context.FacesContext;  
import javax.faces.convert.Converter;  
import javax.faces.convert.ConverterException;  
  
import com.mpdmal.cloudental.entities.Patient;

  
public class PatientConverter implements Converter {  
  
    public static List<Patient> patientDB;  
  
    static {
    	
    	patientDB = new ArrayList<Patient>();
    	Patient p1 =new Patient();
    	p1.setName("p1");
    	p1.setSurname("p1 sur");
    	p1.setId(new Integer(1));
    	
    	Patient p2 =new Patient();
    	p2.setName("p2");
    	p2.setSurname("p2 sur");
    	p2.setId(new Integer(1));
  
    	patientDB.add(p1);  
    	patientDB.add(p2);  
    	  
       
    }  
  
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {  
    	System.out.println("PatientConverter getAsObject");
        if (submittedValue.trim().equals("")) {  
            return null;  
        } else {  
            try {
            	System.out.println("submittedValue" +submittedValue );
                int id = Integer.parseInt(submittedValue); 
                
  
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
        	System.out.println("PatientConverter getAsString "+id);
        	
            return String.valueOf(  id );  
        }  
    }  
}  