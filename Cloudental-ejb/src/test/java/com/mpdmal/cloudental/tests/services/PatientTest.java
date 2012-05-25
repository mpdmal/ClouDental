package com.mpdmal.cloudental.tests.services;

import static org.junit.Assert.assertEquals;

import java.util.Vector;

import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.tests.base.ArquillianCloudentTest;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class PatientTest extends ArquillianCloudentTest {
	@Test
	@InSequence (1)
	public void create() throws CloudentException {
		Dentist d = dbean.createDentist("qwe", "asd", "zxc", "vsd");
		for (int i = 0; i < 10; i++) {
			dsvcbean.createPatient(d.getId(), "PAtient", "PAtient surname");	
		}
	}

	@Test 
	@InSequence (2)
	public void delete() throws CloudentException {
		Dentist d = dbean.getDentist("zxc");
		Vector<Patient> patients = (Vector<Patient>) d.getPatientList();
		assertEquals(10, patients.size());
		dsvcbean.deletePatient(patients.elementAt(0).getId());
		dsvcbean.deletePatient(patients.elementAt(2).getId());
		dsvcbean.deletePatient(patients.elementAt(3).getId());
		dsvcbean.deletePatient(patients.elementAt(5).getId());

		patients = (Vector<Patient>) dbean.getDentist("zxc").getPatientList();
		assertEquals(6, patients.size());

		dsvcbean.deletePatientList(d.getId());
		patients = (Vector<Patient>) dbean.getDentist("zxc").getPatientList();
		assertEquals(0, patients.size());
		dbean.deleteDentists();
	}
}
