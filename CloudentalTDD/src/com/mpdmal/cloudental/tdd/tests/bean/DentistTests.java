package com.mpdmal.cloudental.tdd.tests.bean;

import java.util.Vector;

import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mpdmal.cloudental.EaoManager;
import com.mpdmal.cloudental.beans.DentistBean;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.util.exception.DentistExistsException;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.InvalidDentistCredentialsException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DentistTests {
	EaoManager egr ;

	@Before
	public void pre() {
		egr = new EaoManager(Persistence.createEntityManagerFactory("CloudentalTDD").createEntityManager());
	}
	
	@Test
	public void testCreate() throws DentistExistsException, InvalidDentistCredentialsException {
		DentistBean db = new DentistBean(egr);
		assertEquals(0, db.countDentists());
		for (int i = 0; i < 10; i++) {
			db.createDentist("Name_"+i, "surname", ""+i, "pwd"+i);
		}
		assertEquals(10, db.getDentists().size());
	}

	@Test
	public void testUpdate() throws DentistNotFoundException {
		DentistBean db = new DentistBean(egr);
		Vector<Dentist> dentists = db.getDentists();
		
		Dentist d = dentists.elementAt(0);
		d.setUsername("Altered");
		db.updateDentist(d);
		
		d = db.getDentist("Altered");
		assertNotNull(d);
	}

	@Test
	public void testDelete() throws DentistNotFoundException, DentistExistsException, InvalidDentistCredentialsException {
		DentistBean db = new DentistBean(egr);
		
		Vector<Dentist> dentists = db.getDentists();
		assertEquals(10, dentists.size());
		Dentist d = dentists.elementAt(3);
		db.deleteDentist(d);
		assertEquals(9, db.countDentists());
		
		dentists = db.getDentists();
		assertEquals(9, dentists.size());
		for (Dentist dentist : dentists) {
			db.deleteDentist(dentist);
		}
		assertEquals(0, db.countDentists());
		
		for (int i = 0; i < 10; i++) {
			db.createDentist("Name_"+i, "seff", ""+i, "pwd"+i);
		}
		assertEquals(10, db.countDentists());
		db.deleteDentist(""+4);
		assertEquals(9, db.countDentists());
		db.deleteDentists();
		assertEquals(0, db.countDentists());
	}

		
	@After
	public void post() {
		egr.clostEM();
	}
}
