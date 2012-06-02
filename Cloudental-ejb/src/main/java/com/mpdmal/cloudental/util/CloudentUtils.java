package com.mpdmal.cloudental.util;

import java.util.Iterator;

import javax.persistence.Query;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.engine.ConstraintViolationImpl;
import org.slf4j.LoggerFactory;


import ch.qos.logback.classic.Logger;

import com.mpdmal.cloudental.util.exception.InvalidAddressTypeException;
import com.mpdmal.cloudental.util.exception.InvalidContactInfoTypeException;
import com.mpdmal.cloudental.util.exception.InvalidMedEntryAlertException;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;
import com.mpdmal.cloudental.util.exception.ValidationException;

public class CloudentUtils {
	//ENUMS
	//POST-IT ALERTS
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(CloudentUtils.class);
	private static final Logger servicelogger = (Logger) LoggerFactory.getLogger("com.mpdmal");
	
	public static enum PostitAlertType {
		NOTE (1, "Note"),
		TODO (2, "To do"),
		ALARM (3, "Alarm");
		
		private final String desc;
		private final int value;
		private PostitAlertType(int type, String desc) {
			value = type;
			this.desc = desc;
		}
		public int getValue() {		return value;	}
		public String getDescription() { return desc;}
	}

	public static boolean isPostitAlertValid(int type) throws InvalidPostitAlertException {
		for (PostitAlertType tp : CloudentUtils.PostitAlertType.values())  
			if (type == tp.getValue()) 
		    	return true;
		return false;
	}

	public static String findPostitAlertDescr(int type) {
		for (PostitAlertType tp : PostitAlertType.values()) {
			if (tp.getValue() == type)
				return tp.getDescription();
		}
		return "";
	}
	
	// MEDICAL HISTORY ENTRY ALERTS
	public static enum MedEntryAlertType {
		NOALERT (0, "Normal"),
		LOW (1, "Low"),
		MEDIUM (2, "Medium"),
		HIGH (3, "High");
		
		private final String desc;
		private final int value;
		private MedEntryAlertType(int type, String desc) {
			value = type;
			this.desc = desc;
		}
		public int getValue() {		return value;	}
		public String getDescription() { return desc;}
	}
	public static boolean isMedEntryAlertValid(int type) throws InvalidMedEntryAlertException {
		for (MedEntryAlertType tp : CloudentUtils.MedEntryAlertType.values())  
			if (type == tp.getValue()) 
		    	return true;
		return false;
	}

	public static String findMedEntryAlertDescr(int type) {
		for (MedEntryAlertType tp : MedEntryAlertType.values()) {
			if (tp.getValue() == type)
				return tp.getDescription();
		}
		return "";
	}
	// CONTACT INFO TYPE
	public static enum ContactInfoType {
		EMAIL (0, "E-mail"),
		FAX (1, "Fax"),
		HOME (2, "Home Number "),
		OFFICE (3, "Office Number "),
		MOBILE (4, "Mobile Phone ");
		
		private final String desc;
		private final int value;
		private ContactInfoType(int type, String desc) {
			value = type;
			this.desc = desc;
		}
		public int getValue() {		return value;	}
		public String getDescription() { return desc;}
	}
	public static boolean isContactInfoTypeValid(int type) throws InvalidContactInfoTypeException {
		for (ContactInfoType tp : CloudentUtils.ContactInfoType.values())  
			if (type == tp.getValue()) 
		    	return true;
		return false;
	}

	public static String findContactInfoTypeDescr(int type) {
		for (ContactInfoType tp : ContactInfoType.values()) {
			if (tp.getValue() == type)
				return tp.getDescription();
		}
		return "";
	}

	// ADDRESS TYPE
	public static enum AddressType {
		HOME (0, "Home Address"),
		OFFICE (1, "Office Address"),
		BILLING (2, "Billing Address");
		
		private final String desc;
		private final int value;
		private AddressType(int type, String desc) {
			value = type;
			this.desc = desc;
		}
		public int getValue() {		return value;	}
		public String getDescription() { return desc;}
	}
	public static boolean isAddressTypeValid(int type) throws InvalidAddressTypeException {
		for (AddressType tp : CloudentUtils.AddressType.values())  
			if (type == tp.getValue()) 
		    	return true;
		return false;
	}

	public static String findAddressTypeDescr(int type) {
		for (AddressType tp : AddressType.values()) {
			if (tp.getValue() == type)
				return tp.getDescription();
		}
		return "";
	}

	//LOGING
	private static final int LOG_TYPE_MSG = 1; 
	private static final int LOG_TYPE_WARNING = 2;
	private static final int LOG_TYPE_ERROR = 3;
	private static final int LOG_TYPE_SVCLOG  = 4;
	
	public static void logQueryString(Query q) {
		System.out.println(q.toString());
	}
	
	public static void logServicecall(String entry) {
		log(entry, LOG_TYPE_SVCLOG);
	}
	
	public static void logMessage(String entry) {
		log(entry, LOG_TYPE_MSG);
	}

	public static void logError(String entry) {
		log(entry, LOG_TYPE_ERROR);
	}

	public static void logWarning(String entry) {
		log(entry, LOG_TYPE_WARNING);
	}

	public static void log(String entry, int type) {
		switch (type) {
			case LOG_TYPE_WARNING:
				logger.warn(entry);
				break;
			case LOG_TYPE_ERROR:
				logger.error(entry);
				break;
			case LOG_TYPE_MSG:
				logger.info(entry);
				break;
			case LOG_TYPE_SVCLOG:
				servicelogger.debug(entry);
				break;							
			default:
				break;
		}
	}
	
	public static ValidationException createValidationException (ConstraintViolationException e) {
		String msg = "";
		Iterator<?> it = e.getConstraintViolations().iterator();
		while (it.hasNext()) {
			ConstraintViolationImpl<?> impl = (ConstraintViolationImpl<?>)it.next();
			String name = impl.getRootBean().getClass().toString();
			name = name.substring(name.lastIndexOf(".")+1, name.length());
			msg = msg.concat("Property->"+impl.getPropertyPath().toString().toUpperCase());
			msg = msg.concat(" on Entity->"+name.toUpperCase());
			msg = msg.concat(" "+impl.getMessage()+"\n"); 
		}
		CloudentUtils.logError(msg);
		return new ValidationException(msg,e);
	}
}