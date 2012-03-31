package com.mpdmal.cloudental.tdd.tests.dao;

import java.util.Vector;

import org.junit.Test;

import com.mpdmal.cloudental.entities.Contactinfo;
import com.mpdmal.cloudental.entities.ContactinfoPK;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.tdd.base.CDentAbstractDaoTest;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.InvalidContactInfoTypeException;

import static org.junit.Assert.assertEquals;

public class ContactinfoTests extends CDentAbstractDaoTest {
	@Test
	public void testContactInfo() throws InvalidContactInfoTypeException {
		Patient p = _patientdao.getPatient("Demo Dentist", "Ko", "Pat");
		assertEquals(0, _cinfodao.getContactinfo(p.getId()).size());
		
		ContactinfoPK id = new ContactinfoPK(); 
		id.setId(p.getId());
		id.setInfotype(CloudentUtils.ContactInfoType.EMAIL.getValue());

		Contactinfo cnt = new Contactinfo();
		cnt.setInfo("a@b.c");
		cnt.setId(id);
		p.addContactInfo(cnt);
		
		id = new ContactinfoPK(); 
		id.setId(p.getId());
		id.setInfotype(CloudentUtils.ContactInfoType.FAX.getValue());
		
		cnt = new Contactinfo();
		cnt.setInfo("123-123555");
		cnt.setId(id);
		p.addContactInfo(cnt);

		//create (through patient/ no dao op)
		_patientdao.updateCreate(p, true);
		
		//get
		Vector<Contactinfo> cinfo = _cinfodao.getContactinfo(p.getId()); 
		assertEquals(2, cinfo.size());
		
	}
}
