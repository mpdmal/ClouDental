package com.mpdmal.cloudental.web.converter;


import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.web.beans.ActivityHolderBean;

@FacesConverter (forClass=ActivityConverter.class, value="activityConverter")
public class ActivityConverter implements Converter {  
  
	public static List<Activity> activityListFromDB; 
  
   
  
	public Activity getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {  
		
		if (submittedValue.trim().equals("")) {  
			return null;  
		} else {  
			try {
				System.out.println("submittedValue" +submittedValue );
				int id = Integer.parseInt(submittedValue); 

				if(activityListFromDB==null)
					activityListFromDB = getActivityList();
				for (Activity a : activityListFromDB) {  
					if (a.getId() == id) {  
						return a;  
					}  
				}  

			} catch(NumberFormatException exception) {  
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid patient"));  
			}  
		}  
		return null;  
	}  
  
    private List<Activity> getActivityList() {
    	FacesContext context=FacesContext.getCurrentInstance();
    	ActivityHolderBean activityManagementBean = (ActivityHolderBean)context.getApplication() .evaluateExpressionGet(context, "#{activityHolderBean}", ActivityHolderBean.class);
		return activityManagementBean.getActivityListOfPatient();
	}

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {  
    	if (value == null || value.equals("")) {  
    		return "";  
    	} else {  
    		String id = ( (Activity) value).getId().toString();
    		return String.valueOf(  id );  
    	}  
    }  
}  