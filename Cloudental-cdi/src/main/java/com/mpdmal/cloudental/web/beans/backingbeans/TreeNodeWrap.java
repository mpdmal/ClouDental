package com.mpdmal.cloudental.web.beans.backingbeans;

import java.text.DateFormat;
import java.util.Date;

import javax.swing.text.DateFormatter;

import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Patient;

public class TreeNodeWrap {
	Object obj = null;
	private final String NA_STRING = "";
	private final String EMPTY_STRING = "";
	
	public TreeNodeWrap(Object o) {
		obj = o;
	}
	
	public String getName () {
		if (!(obj instanceof Activity) && !(obj instanceof Patient))
			return NA_STRING;
		if (obj instanceof Patient) 
			return ((Patient) obj).getName();
		return ((Activity) obj).getDescription();
	}
	
	public String getStartDate() {
		if (!(obj instanceof Activity) && !(obj instanceof Patient))
			return NA_STRING;
		if (obj instanceof Patient) 
			return formatDate(((Patient) obj).getCreated());
		return formatDate(((Activity) obj).getStartdate());
	}

	public String getEndDate() {
		if (!(obj instanceof Activity) && !(obj instanceof Patient))
			return NA_STRING;
		if (obj instanceof Patient) 
			return EMPTY_STRING;
		return formatDate(((Activity) obj).getEnddate());
	}

	public String getDiscount() {
		if (obj instanceof Activity) {
			return ((Activity) obj).getDiscount().getTitle();
		}
		return NA_STRING;
	}
	public String getPriceable() {
		if (obj instanceof Activity) {
			return ((Activity) obj).getPriceable().getTitle();
		}
		return NA_STRING;
	}
	private String formatDate(Date d) {
		return DateFormat.getDateTimeInstance().format(d);
	}
}

