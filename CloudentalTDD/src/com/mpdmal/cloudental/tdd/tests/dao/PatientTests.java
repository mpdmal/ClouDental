package com.mpdmal.cloudental.tdd.tests.dao;

import java.util.Date;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.tdd.base.CDentAbstractDaoTest;
import com.mpdmal.cloudental.util.exception.PatientAlreadyExistsException;

public class PatientTests extends CDentAbstractDaoTest {
	@Override
	public void initTestEnv() {
		// TODO Auto-generated method stub
		Dentist _aDentist = new Dentist();
		_aDentist.setUsername("Arilou");
		_aDentist.setPassword("admin");
		_aDentist.setName("Dim");
		_aDentist.setSurname("Aza");
		_dentistdao.updateCreate(_aDentist, false);
	}
	
	@Test
	public void testPatients() {
		Dentist d = _dentistdao.getDentist("Arilou");
		
		Patient p = new Patient();
		p.setComments("");
		p.setCreated(new Date());
		p.setName("Ko");
		p.setSurname("pat");
		p.setDentist(d);

		Patient p2 = new Patient();
		p2.setComments("");
		p2.setCreated(new Date());
		p2.setName("Ge");
		p2.setSurname("Mit");
		p2.setDentist(d);

		Patient p3 = new Patient();
		p3.setComments("");
		p3.setCreated(new Date());
		p3.setName("Ge1");
		p3.setSurname("Mit");
		p3.setDentist(d);

		try {
			d.addPatient(p);
			d.addPatient(p2);
			d.addPatient(p3);
		} catch (PatientAlreadyExistsException e) {
			e.printStackTrace();
		}
		//create
		_dentistdao.updateCreate(d, false);
		
		assertEquals(3, _patientdao.countPatients());
		
		//single delete
		_patientdao.delete(p2);
		assertEquals(2, _patientdao.countPatients());
		
	}
	@Override
	public void closeTestEnv() {
		//cascade delete
		Dentist d = _dentistdao.getDentist("Arilou");
		_dentistdao.delete(d);
		assertEquals(0, _dentistdao.countDentists());
		assertEquals(0, _patientdao.countPatients());

//		//cascade delete
//		d.addPatient(p);
//		d.addPatient(p2);
//
//		d = _dentistdao.getDentist("Arilou");
//		_dentistdao.delete(d);
//
//		
//		//get/delete
//		p = _patientdao.getPatients(d.getUsername()).elementAt(0);
//		_patientdao.delete(p);
//
//		_dentistdao.delete(d);
//		
//		assertEquals(0, _dentistdao.countDentists());
//		assertEquals(0, _patientdao.countPatients());
	}
}
