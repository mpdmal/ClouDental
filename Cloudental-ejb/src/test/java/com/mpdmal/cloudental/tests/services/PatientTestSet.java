package com.mpdmal.cloudental.tests.services;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Vector;

import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.tests.base.ArquillianCloudentTestBase;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.ValidationException;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class PatientTestSet extends ArquillianCloudentTestBase {
	@Test
	@InSequence (1)
	public void create() throws CloudentException {
		//create
		Dentist d = dbean.createDentist("qwe", "asd", "zxc", "vsd");
		for (int i = 0; i < 10; i++) {
			dsvcbean.createPatient(d.getId(), "PAtient_"+i, "Patient surname_"+i);	
		}
		//find by dentist
		Vector<Patient> pats= (Vector<Patient>)dbean.findDentist(d.getId()).getPatientList();
		assertEquals(10, pats.size());
		for (int i = 0; i < pats.size(); i++) {
			Patient p = pats.elementAt(i);
			assertEquals("PAtient_"+i, p.getName());
			assertEquals("Patient surname_"+i, p.getSurname());
		}
		
		//create identical
		dsvcbean.createPatient(d.getId(), "PAtient_10", "Patient surname_10");
		dsvcbean.createPatient(d.getId(), "PAtient_10", "Patient surname_10");
		pats= (Vector<Patient>)dbean.findDentist(d.getId()).getPatientList();
		assertEquals(12, pats.size());
		
		//failed create ..constraints violated
		try {
			dsvcbean.createPatient(d.getId(), null,null);
		} catch (ValidationException e) {
			String msg = e.getMessage();
			
			assertEquals(true, msg.contains("Property->NAME on Entity->PATIENT may not be null"));
			assertEquals(true, msg.contains("Property->NAME on Entity->PATIENT may not be empty"));
			assertEquals(true, msg.contains("Property->SURNAME on Entity->PATIENT may not be null"));
			assertEquals(true, msg.contains("Property->SURNAME on Entity->PATIENT may not be empty"));
		}

		//failed create ..dentist does not exist
		try {
			dsvcbean.createPatient(-1, "xcv","asd");
		} catch (DentistNotFoundException e) {
			assertEquals("Dentist id not found:-1\n", e.getMessage());
		}
		pats= (Vector<Patient>)dbean.findDentist(d.getId()).getPatientList();
		assertEquals(12, pats.size());
	}

	@Test
	@InSequence (2)
	public void getAndCount () throws DentistNotFoundException {
		Dentist d = dbean.findDentistByUsername("zxc");
		//get by dentist id
		Vector<Patient> pats = (Vector<Patient>)dsvcbean.getPatientlist(d.getId());
		assertEquals(12, pats.size());
		//get from dentist object
		assertEquals(12, d.getPatientList().size());
		
		for (int i = 0; i < pats.size()-1; i++) { // last is identical
			Patient p = pats.elementAt(i);
			assertEquals("PAtient_"+i, p.getName());
			assertEquals("Patient surname_"+i, p.getSurname());
		}
		
		assertEquals(12, dsvcbean.countDentistPatients(d.getId()));
		//count patients
		assertEquals(12, dsvcbean.countPatients());
	}
	
	@Test 
	@InSequence (3)
	public void update() throws CloudentException {
		Dentist d = dbean.findDentistByUsername("zxc");
		Collection<Patient> pats = dsvcbean.getPatientlist(d.getId());
		//update patients
		for (Patient p : pats) {
			dsvcbean.updatePatient(p.getId(), "altered name", "altered surname", "altered!!!");
		}
		
		//through service
		pats = (Collection<Patient>) dsvcbean.getPatientlist(d.getId());
		for (Patient p : pats) {
			assertEquals("altered!!!", p.getComments());
			assertEquals("altered surname", p.getSurname());
			assertEquals("altered name", p.getName());
		}
		
		//through dentist
		d = dbean.findDentistByUsername("zxc");
		for (Patient p : d.getPatientList()) {
			assertEquals("altered!!!", p.getComments());
			assertEquals("altered surname", p.getSurname());
			assertEquals("altered name", p.getName());
		}
		
		assertEquals(12, dsvcbean.countDentistPatients(d.getId()));
		assertEquals(12, dsvcbean.countPatients());
	}
	
	@Test 
	@InSequence (4)
	public void delete() throws CloudentException {
		Dentist d = dbean.findDentistByUsername("zxc");
		Vector<Patient> patients = (Vector<Patient>) d.getPatientList();
		assertEquals(12, patients.size());
		dsvcbean.deletePatient(patients.elementAt(0).getId());
		dsvcbean.deletePatient(patients.elementAt(2).getId());
		dsvcbean.deletePatient(patients.elementAt(3).getId());
		dsvcbean.deletePatient(patients.elementAt(5).getId());

		assertEquals(8, dsvcbean.countPatients());

		dsvcbean.deletePatientList(d.getId());
		assertEquals(0, dsvcbean.countDentistPatients(d.getId()));
		dbean.deleteDentists();
	}
}
