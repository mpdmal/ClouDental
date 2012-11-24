package com.mpdmal.cloudental.beans;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebService;
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

import com.mpdmal.cloudental.beans.base.AbstractEaoService;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.InvalidPasswordException;
import com.mpdmal.cloudental.util.exception.base.CloudentException;


@Named
@Stateless
@LocalBean
@WebService
public class LoginBean extends AbstractEaoService {
	private static final long serialVersionUID = 1L;
	@Inject
	DentistBean dentistEao;
	
    public LoginBean() {}

    public Dentist doLogin(String username, String password) 
    									throws DentistNotFoundException, 
    									InvalidPasswordException {
    	Dentist d = dentistEao.findDentistByUsername(username);
    	if (d == null)
    		throw new DentistNotFoundException(username);
    	
    	if (!d.getPassword().equals(password))
    		throw new InvalidPasswordException(" for user:"+username);
    	
    	CloudentUtils.logMessage("successfully logged in "+username);
    	return d;
    }
    
    public void contactCloudM (String title, String msg) throws CloudentException {
    	try {
			CloudentUtils.contactCloudental(title, msg);
		} catch (Exception e) {
			throw new CloudentException(e.getMessage());
		}
    }
}
