package com.mpdmal.cloudental.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.util.exception.base.CloudentException;


public class CloudentalXML {
	public static Dentist parseDentist(InputStream is) {
		Dentist d = new Dentist();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			DentistHandler dh = new DentistHandler();
			saxParser.parse(is, dh);
			d = dh.getDentist();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return d;
	}
	
	public static boolean isException(String xml) {
		if (xml.indexOf(CloudentException.EXCEPTION_NODE) == 0)
			return true;
		return false;
	}
	
	public static boolean isException(InputStream is) {
		StringBuffer xml = new StringBuffer();
		byte[] b = new byte[CloudentException.EXCEPTION_NODE.length()];
		int n;
		try {
			n = is.read(b);
			if (n > 0) xml.append(new String(b, 0 , n));
		} catch (IOException e) {
			return false;
		}
		if (xml.indexOf(CloudentException.EXCEPTION_NODE) == 0)
			return true;
		return false;
	}

}	

class DentistHandler extends DefaultHandler {
	private Dentist _dentist = null;
	private boolean _idnode = true, _namenode = true,
			_unamenode = true, _snamenode = true,
			_pwdnode = true;
	
	public Dentist getDentist () {	return _dentist;	}
	
	@Override
	public void startDocument() throws SAXException {
		_dentist = new Dentist();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equals(Dentist.DENTIST_IDNODE))  
			_idnode = true;
		else
		if (qName.equals(Dentist.DENTIST_NAMEENDNODE)) 
			_namenode = true;
		else
		if (qName.equals(Dentist.DENTIST_SURNAMENODE)) 
			_snamenode = true;
		else
		if (qName.equals(Dentist.DENTIST_USERNAMENODE)) 
			_unamenode = true;
		else
		if (qName.equals(Dentist.DENTIST_PASSWORDNODE)) 
			_pwdnode = true;
	}
	
	public void characters(char ch[], int start, int length) throws SAXException {
		if (_idnode) {
			_dentist.setId(Integer.parseInt(new String(ch,start,length)));
			_idnode = false;
		}
 
		if (_namenode) {
			_dentist.setName(new String(ch,start,length));
			_namenode = false;
		}
 
		if (_pwdnode) {
			_dentist.setPassword(new String(ch,start,length));
			_pwdnode = false;
		}
 
		if (_unamenode) {
			_dentist.setUsername(new String(ch,start,length));
			_unamenode = false;
		}

		if (_snamenode) {
			_dentist.setSurname(new String(ch,start,length));
			_snamenode = false;
		}
	}	
}