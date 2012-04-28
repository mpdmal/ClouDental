package com.mpdmal.cloudental.tdd.tests.dao;

import java.util.Date;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.tdd.base.CDentAbstractDaoTest;

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

		d.addPatient(p);
		d.addPatient(p2);
		//create
		_patientdao.updateCreate(p, false);
		_patientdao.updateCreate(p2, false);
		assertEquals(2, _patientdao.countPatients());
		_dentistdao.delete(p2);
		assertEquals(1, _patientdao.countPatients());
	}
	@Override
	public void closeTestEnv() {
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
