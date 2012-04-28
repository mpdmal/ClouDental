package com.mpdmal.cloudental.tdd.tests.dao;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;

import org.junit.Test;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.Postit;
import com.mpdmal.cloudental.entities.PostitPK;
import com.mpdmal.cloudental.tdd.base.CDentAbstractDaoTest;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;
import com.mpdmal.cloudental.util.exception.PatientAlreadyExistsException;

public class CascadeTests extends CDentAbstractDaoTest {
	@Override
	public void initTestEnv() {
		assertEquals(0, _dentistdao.countDentists());
		assertEquals(0, _postitdao.countPostits());
		assertEquals(0, _patientdao.countPatients());
	}

	@Test
	public void testCascade() throws InvalidPostitAlertException {
		Dentist _aDentist = new Dentist(); //create dentist
		_aDentist.setUsername("Arilou");
		_aDentist.setPassword("admin");
		_aDentist.setName("Dim");
		_aDentist.setSurname("Aza");
		
		Patient p = new Patient(); //a patient
		p.setComments("coments");
		p.setDentist(_aDentist);
		p.setName("patient1");
		p.setSurname("patient 2 surname");
		p.setCreated(new Timestamp(System.currentTimeMillis()));
		HashSet<Patient> patients = new HashSet<Patient>();
		patients.add(p);
		try {
			_aDentist.setPatients(patients);
		} catch (PatientAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Postit ps = new Postit(); //some notes
		ps.setAlert(2);
		PostitPK id = new PostitPK();
		id.setId(_aDentist.getUsername());
		id.setPostdate(new Date());
		ps.setId(id);
		ps.setPost("post!");
		HashSet<Postit> posts = new HashSet<Postit>();
		posts.add(ps);
		_aDentist.setNotes(posts);

		_dentistdao.updateCreate(_aDentist, false);
		assertEquals(1, _dentistdao.countDentists());
		assertEquals(1, _postitdao.countPostits());
		assertEquals(1, _patientdao.countPatients());
		
		_dentistdao.delete(_dentistdao.getDentist("Arilou"));
		assertEquals(0, _dentistdao.countDentists());
		assertEquals(0, _postitdao.countPostits());
		assertEquals(0, _patientdao.countPatients());
	}
}
