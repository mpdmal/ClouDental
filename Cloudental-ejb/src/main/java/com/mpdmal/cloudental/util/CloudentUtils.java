package com.mpdmal.cloudental.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;
import javax.validation.ConstraintViolationException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.FileResolver;

import org.hibernate.validator.engine.ConstraintViolationImpl;
import org.slf4j.LoggerFactory;


import ch.qos.logback.classic.Logger;

import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.util.exception.InvalidAddressTypeException;
import com.mpdmal.cloudental.util.exception.InvalidContactInfoTypeException;
import com.mpdmal.cloudental.util.exception.InvalidMedEntryAlertException;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;
import com.mpdmal.cloudental.util.exception.InvalidTitleFormatTypeException;
import com.mpdmal.cloudental.util.exception.ValidationException;

public class CloudentUtils {
	public static final int DEFAULT_USER_ID = -1;
	public static final int DEFAULT_DISCOUNT_ID = -1;
	public static final int DEFAULT_PRICEABLE_ID = -1;
    private static final String DBSTRING = "jdbc:postgresql://localhost:5432/CloudentDB";
    private static final String DBUSER = "aza";
    private static final String DBPWD = "aza";
    private static final String RESOURCES_RELATIVEDIR = "cloudental/jasper/images/";
    private static final String PATIENTS_REPORT_JASPER = "cloudental/jasper/patient_report.jasper";
    private static final String PATIENTS_REPORT_PDF = "reporting/patient_report_$1.pdf";
	
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
	
	//VISIT EVENT TITLE FORMAT TYPE
	public static enum EventTitleFormatType {
		FULL (1, "Name and Surname"),
		NAME (2, "Name only"),
		SURNAME (3, "Surname only"),
		SHORT (4, "Surname and initial");
		
		
		private final String desc;
		private final int value;
		private EventTitleFormatType(int type, String desc) {
			value = type;
			this.desc = desc;
		}
		public int getValue() {		return value;	}
		public String getDescription() { return desc;}
	}

	public static boolean isTitleFormatTypeValid(int type) throws InvalidTitleFormatTypeException {
		for (EventTitleFormatType tp : CloudentUtils.EventTitleFormatType.values())  
			if (type == tp.getValue()) 
		    	return true;
		return false;
	}

	public static String findTitleFormatTypeDescr(int type) {
		for (EventTitleFormatType tp : EventTitleFormatType.values()) {
			if (tp.getValue() == type)
				return tp.getDescription();
		}
		return "";
	}
	
	public static String createEventTitle(int type, Patient p) throws InvalidTitleFormatTypeException {
		if (!CloudentUtils.isTitleFormatTypeValid(type))
			throw new InvalidTitleFormatTypeException(type);
		
		if (type == CloudentUtils.EventTitleFormatType.FULL.getValue()) {
			return p.getSurname()+" "+p.getName();
		} else
		if (type == CloudentUtils.EventTitleFormatType.NAME.getValue()) {
			return p.getName();
		} else
		if (type == CloudentUtils.EventTitleFormatType.SHORT.getValue()) {
			return p.getSurname()+" "+p.getName().substring(0,1);
		}
		return p.getSurname();
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
	
	public static void logMethodInfo(Method m, Object[] prms) {
    	StringBuilder sb = new StringBuilder("bean service fired:"+m.getName());
    	if (prms == null) {
    		sb.append("  [no args]");
    		CloudentUtils.logServicecall(sb.toString());
    		return;
    	}
    	
    	sb.append("  [ ");
    	for (Object o : prms) {
    		if (o!=null)
			  sb.append(o.toString()).append(", ");
		}
    	sb.delete(sb.length()-2, sb.length());
    	sb.append("]");
    	CloudentUtils.logServicecall(sb.toString());
    }

    public static void logContextData(Map<String, Object> data) {
    	Set<String> keys = data.keySet();
    	StringBuilder sb = new StringBuilder();
    	sb.append("Context Data ------>");
    	for (String key : keys) {
			Object val = data.get(key);
			sb.append(key).append(":").append(val.toString()).append("");
		}
    	sb.append("<------ End Context Data ");
		CloudentUtils.logServicecall(sb.toString());
    }

	public static void printReport(int dentistid) {
		try {
			FileResolver fileResolver = new FileResolver() {
				@Override
				public File resolveFile(String fileName) {
					return new File(RESOURCES_RELATIVEDIR+fileName);
				}
			};
			try {
				System.out.println(new File (".").getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("REPORT_FILE_RESOLVER", fileResolver);
			parameters.put("DENTISTID", new Integer(dentistid));
			
			JasperPrint jprint = JasperFillManager.fillReport(
					new FileInputStream(PATIENTS_REPORT_JASPER),
					parameters, getSystemConnection());
			String outname = PATIENTS_REPORT_PDF;
			outname = outname.replace("$", ""+dentistid);
			JasperExportManager.exportReportToPdfFile(jprint, outname);
			System.out.println("CREATED REPORT "+outname);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
	}	
	private static Connection getSystemConnection() {
		//try connection to postgres
		try {
			Class.forName("org.postgresql.Driver");
			System.out.println("Connection to postgres established :"+DBSTRING);
			return DriverManager.getConnection(DBSTRING,DBUSER,DBPWD);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}