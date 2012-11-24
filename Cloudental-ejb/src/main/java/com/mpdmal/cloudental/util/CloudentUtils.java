package com.mpdmal.cloudental.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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
import com.mpdmal.cloudental.util.exception.InvalidMedIntakeRouteException;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;
import com.mpdmal.cloudental.util.exception.InvalidTitleFormatTypeException;
import com.mpdmal.cloudental.util.exception.ValidationException;

public class CloudentUtils {
	public static final int DEFAULT_USER_ID = -1;
	public static final int DEFAULT_DISCOUNT_ID = -1;
	public static final int DEFAULT_PRICEABLE_ID = -1;
	
	public static final int REPORTTYPE_PATIENTS = 1;
	public static final int REPORTTYPE_PHARMACY = 2;
	
    private static final String DBSTRING = "jdbc:postgresql://localhost:5432/CloudentDB";
    private static final String DBUSER = "aza";
    private static final String DBPWD = "aza";
    private static final String RESOURCES_RELATIVEDIR = "cloudental/jasper/images/";
    private static final String PATIENTS_REPORT_JASPER = "cloudental/jasper/patient_report.jasper";
    private static final String PATIENTS_REPORT_PDF = "cloudental/reporting/patient_report_$.pdf";
    private static final String PRESCRIPTIONS_REPORT_JASPER = "cloudental/jasper/prescriptions_report.jasper";
    private static final String PRESCRIPTIONS_REPORT_PDF = "cloudental/reporting/prescriptions_report_$.pdf";

    public static final String CLOUDENT_ACCOUNT = "cloudental@gmail.com" ;
    public static final String CLOUDENT_PWD = "cloudental123!";
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
	public static enum PrescrRowTimeunit {
		HOURS (Calendar.HOUR_OF_DAY, "hour(s)", "hour(s)"),
		DAYS (Calendar.DAY_OF_MONTH, "day(s)", "day(s)"),
		WEEK (Calendar.WEEK_OF_MONTH, "week(s)", "week(s)"),
		MONTH (Calendar.MONTH, "month(s)", "month(s)");
		
		private final String ddesc, fdesc;
		private final int value;
		private PrescrRowTimeunit(int type, String fdesc, String ddesc) {
			value = type;
			this.fdesc = fdesc;
			this.ddesc = ddesc;
		}
		public int getValue() {		return value;	}
		public String getFreqUnitDescription() { return fdesc;}
		public String getDurUnitDescription() { return ddesc;}
	}

	public static boolean isPrescrRowTimeunitValid(int type) {
		for (PrescrRowTimeunit tp : CloudentUtils.PrescrRowTimeunit.values())  
			if (type == tp.getValue()) 
		    	return true;
		return false;
	}

	public static String findPrescrRowTimeunitDurDescr(int type) {
		for (PrescrRowTimeunit tp : PrescrRowTimeunit.values()) {
			if (tp.getValue() == type)
				return tp.getDurUnitDescription();
		}
		return "";
	}
	public static String findPrescrRowTimeunitFreqDescr(int type) {
		for (PrescrRowTimeunit tp : PrescrRowTimeunit.values()) {
			if (tp.getValue() == type)
				return tp.getFreqUnitDescription();
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
	
	//MEDICINE INTAKE ROUTES
	public static enum MedIntakeRoute {
		ORAL (0, "Oral"),
		SUBLINGUAL (1, "Sublingual"),
		RECTAL (2, "Rectal"),
		TRANSDERMAL (3, "Transdermal"),
		TRANSMUCOSAL (4, "Transmucosal");
		
		private final String desc;
		private final int value;
		private MedIntakeRoute(int type, String desc) {
			value = type;
			this.desc = desc;
		}
		public int getValue() {		return value;	}
		public String getDescription() { return desc;}
	}

	public static boolean isMedIntakeRouteValid(int type) throws InvalidMedIntakeRouteException {
		for (MedIntakeRoute rt : CloudentUtils.MedIntakeRoute.values())  
			if (type == rt.getValue()) 
		    	return true;
		return false;
	}

	public static String findMedIntakeRouteDescr(int type) {
		for (MedIntakeRoute rt : MedIntakeRoute.values()) {
			if (rt.getValue() == type)
				return rt.getDescription();
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

    public static String printPatientReport(int dentistid) throws FileNotFoundException, JRException {
		String infile, outfile = "";
		HashMap<String, Object> parameters = getDefaultReportParameters();
		parameters.put("DENTISTID", new Integer(dentistid));
		infile = PATIENTS_REPORT_JASPER;
		outfile = PATIENTS_REPORT_PDF;

		JasperPrint jprint = JasperFillManager.fillReport(
				new FileInputStream(infile),
				parameters, getSystemConnection());
		
		outfile = outfile.replace("$", ""+dentistid);
		JasperExportManager.exportReportToPdfFile(jprint, outfile);
		CloudentUtils.logMessage("Created PATIENTS REPORT: "+outfile);
		return outfile;
    }
    
	public static String printPrescriptionReport(int prescid, String headertext, String patientname) throws FileNotFoundException, JRException {
		String infile, outfile = "";
		HashMap<String, Object> parameters = getDefaultReportParameters();
		parameters.put("PRESCRIPTIONID", new Integer(prescid));
		parameters.put("HEADERTEXT", headertext);
		parameters.put("PATIENTNAME", patientname);
		infile = PRESCRIPTIONS_REPORT_JASPER;
		outfile = PRESCRIPTIONS_REPORT_PDF;
		
		JasperPrint jprint = JasperFillManager.fillReport(
				new FileInputStream(infile),
				parameters, getSystemConnection());
		
		outfile = outfile.replace("$", ""+prescid);
		JasperExportManager.exportReportToPdfFile(jprint, outfile);
		CloudentUtils.logMessage("Created PRESCRIPTION REPORT: "+outfile);
		return outfile;
	}	
	private static Connection getSystemConnection() {
		//try connection to postgres
		try {
			Class.forName("org.postgresql.Driver");
			return DriverManager.getConnection(DBSTRING,DBUSER,DBPWD);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void mailReport(String pdf, String email) throws MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		Session session = Session.getInstance(props);
		
		if (!verifyEmail(email)) {
			CloudentUtils.logError("\tinvalid email :"+email);
			throw new MessagingException("invalid email :"+email);
		}

		//create message
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress("cloudental@gmail.com"));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
		message.setSubject("Cloudental - on demand reporting");

		//create bodypart for attachment
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		DataSource ds = new FileDataSource(pdf);
		messageBodyPart.setDataHandler(new DataHandler(ds));
		messageBodyPart.setFileName(ds.getName());
		
		//create multipart and add parts
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(messageBodyPart);
		
		message.setContent(mp);
		message.setSentDate(new Date());
		
		Transport transport = session.getTransport("smtp");
		transport.connect("smtp.gmail.com", 587, CLOUDENT_ACCOUNT, CLOUDENT_PWD);
		transport.sendMessage(message, message.getAllRecipients());
		CloudentUtils.logMessage("\temailed :"+email+" a patient report");
	}
	
	private static boolean verifyEmail(String email) {
		if (email.length() <= 0) 
			return false;

		int atidx = email.indexOf("@");
		int dotidx = email.indexOf(".");
		if (atidx < 1 || dotidx < 3) 
			return false;
		
		return true;
	}
	
	private static HashMap<String, Object> getDefaultReportParameters() {
		FileResolver fileResolver = new FileResolver() {
			@Override
			public File resolveFile(String fileName) {
				return new File(RESOURCES_RELATIVEDIR+fileName);
			}
		};
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("REPORT_FILE_RESOLVER", fileResolver);
		return parameters;
	}
	
	public static void contactCloudental(String title, String msg) throws AddressException, MessagingException {
    	Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		Session session = Session.getInstance(props);
		//create message
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress("cloudental@gmail.com"));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("arilou_npl@hotmail.com"));
		//message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("kpatakas@gmail.com"));
		message.setSubject("CD contact:"+title);

		message.setContent(msg, "text/plain");
		message.setSentDate(new Date());
		
		Transport transport = session.getTransport("smtp");
		transport.connect("smtp.gmail.com", 587, CloudentUtils.CLOUDENT_ACCOUNT, CloudentUtils.CLOUDENT_PWD);
		transport.sendMessage(message, message.getAllRecipients());
		CloudentUtils.logMessage("\temailed Cloudental! (patco, dimaz)");
		
		//maybe also keep in DB at a later stage
	}
}