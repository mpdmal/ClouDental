package com.mpdmal.cloudental.tdd.tests.dao;

import java.util.Date;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.tdd.base.CDentAbstractDaoTest;
import com.mpdmal.cloudental.util.exception.PatientExistsException;

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
		
		Dentist _aDentist1 = new Dentist();
		_aDentist1.setUsername("Mika");
		_aDentist1.setPassword("admin");
		_aDentist1.setName("Asd");
		_aDentist1.setSurname("Qwe");
		_dentistdao.updateCreate(_aDentist1, false);
	}
	
	@Test
	public void testPatients() {
		Dentist d1 = _dentistdao.getDentist("Arilou");
		Dentist d2 = _dentistdao.getDentist("Mika");
		
		Patient p = new Patient();
		p.setComments("");
		p.setCreated(new Date());
		p.setName("Ko");
		p.setSurname("pat");
		p.setDentist(d1);

		Patient p2 = new Patient();
		p2.setComments("");
		p2.setCreated(new Date());
		p2.setName("Ge");
		p2.setSurname("Mit");
		p2.setDentist(d2);

		Patient p3 = new Patient();
		p3.setComments("");
		p3.setCreated(new Date());
		p3.setName("Ge1");
		p3.setSurname("Mit");
		p3.setDentist(d2);

		try {
			d1.addPatient(p);
			d2.addPatient(p2);
			d2.addPatient(p3);
		} catch (PatientExistsException e) {
			e.printStackTrace();
		}
		//create
		_dentistdao.updateCreate(d1, false);
		_dentistdao.updateCreate(d2, false);
		
		assertEquals(3, _patientdao.countPatients());
		assertEquals(2, _dentistdao.countDentists());
		
		assertEquals(2, _patientdao.countPatients("Mika"));
		assertEquals(1, _patientdao.countPatients("Arilou"));
		//single delete
		_patientdao.delete(p2);
		assertEquals(2, _patientdao.countPatients());
		assertEquals(2, _dentistdao.countDentists());
		
		_dentistdao.delete(_dentistdao.getDentist("Mika"));
		assertEquals(1, _dentistdao.countDentists());
		assertEquals(1, _patientdao.countPatients());
		_dentistdao.delete(_dentistdao.getDentist("Arilou"));
		assertEquals(0, _dentistdao.countDentists());
		assertEquals(0, _patientdao.countPatients());
	}
}
