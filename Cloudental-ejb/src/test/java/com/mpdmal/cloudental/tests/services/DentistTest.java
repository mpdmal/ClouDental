package com.mpdmal.cloudental.tests.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Vector;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.tests.base.ArquillianCloudentTest;
import com.mpdmal.cloudental.util.exception.DentistExistsException;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.ValidationException;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

@RunWith(Arquillian.class)
public class DentistTest extends ArquillianCloudentTest{
	@Test
	@InSequence (1)
	public void create() throws CloudentException {
		assertEquals(0, dbean.countDentists());
		//create
		for (int i = 0; i < 10; i++) {
			dbean.createDentist("Name_"+i, "surname", ""+i, "pwd"+i);
		}
		Vector<Dentist> dents = dbean.getDentists();
		assertEquals(10, dents.size());
		for (int i = 0; i < dents.size(); i++) {
			Dentist dn = dents.elementAt(i);
			assertEquals("surname", dn.getSurname());			
			assertEquals("pwd"+i, dn.getPassword());
			assertEquals(""+i, dn.getUsername());
			assertEquals("Name_"+i, dn.getName()); 
		}
		
		//create identical
		dbean.createDentist("Name_10", "surname", "10", "pwd10");
		try {
			dbean.createDentist("Name_10", "surname", "10", "pwd10");
		} catch (DentistExistsException e) {
			assertEquals("Dentist already Exists:10\n", e.getMessage());
		}
		assertEquals(11, dbean.getDentists().size());
		//failed create ..constraints violated
		//also testing cloudental ValidationException
		try {
			dbean.createDentist(null, null,null,null);
		} catch (ValidationException e) {
			assertEquals(true, e.getMessage().contains("Property->NAME on Entity->DENTIST may not be null"));
			assertEquals(true, e.getMessage().contains("Property->NAME on Entity->DENTIST may not be empty"));
			assertEquals(true, e.getMessage().contains("Property->SURNAME on Entity->DENTIST may not be null"));
			assertEquals(true, e.getMessage().contains("Property->SURNAME on Entity->DENTIST may not be empty"));
			assertEquals(true, e.getMessage().contains("Property->USERNAME on Entity->DENTIST may not be null"));
			assertEquals(true, e.getMessage().contains("Property->USERNAME on Entity->DENTIST may not be empty"));
			assertEquals(true, e.getMessage().contains("Property->PASSWORD on Entity->DENTIST may not be null"));
			assertEquals(true, e.getMessage().contains("Property->PASSWORD on Entity->DENTIST may not be empty"));
		}
		assertEquals(11, dbean.getDentists().size());
	}
	
	
	@Test
	@InSequence (2)
	public void getAndCount () throws DentistNotFoundException {
		//get by username
		Dentist d = dbean.findDentistByUsername("1");
		assertEquals("Name_1", d.getName());
		assertEquals("pwd1", d.getPassword());
		
		//failed get, wrong username 
		try {
			d = dbean.findDentistByUsername("Arilou");	
		} catch (DentistNotFoundException e) {
			assertEquals(true, e.getMessage().contains("Dentist not found:Arilou"));
		}
		
		//get from dentist object
		assertEquals(11, dbean.countDentists());
		
		Vector<Dentist> dents = dbean.getDentists();
		for (int i = 0; i < dents.size(); i++) {
			Dentist dn = dents.elementAt(i);
			assertEquals("surname", dn.getSurname());			
			assertEquals("pwd"+i, dn.getPassword());
			assertEquals(""+i, dn.getUsername());
			assertEquals("Name_"+i, dn.getName()); 
		}

		//count discounts
		assertEquals(11, dbean.countDentists());
	}
	@Test
	@InSequence (3)
	public void update() throws CloudentException {
		Vector<Dentist> dentists = dbean.getDentists();
		
		Dentist d = dentists.elementAt(0);
		d.setUsername("Altered");
		dbean.updateDentist(d);
		
		d = dbean.findDentistByUsername("Altered");
		assertNotNull(d);
	}

	@Test
	@InSequence (4)
	public void delete() throws CloudentException {
		
		Vector<Dentist> dentists = dbean.getDentists();
		assertEquals(11, dentists.size());
		Dentist d = dentists.elementAt(3);
		dbean.deleteDentist(d.getId());
		assertEquals(10, dbean.countDentists());
		
		dentists = dbean.getDentists();
		assertEquals(10, dentists.size());
		for (Dentist dentist : dentists) {
			dbean.deleteDentist(dentist.getId());
		}
		assertEquals(0, dbean.countDentists());
		
		for (int i = 0; i < 11; i++) {
			dbean.createDentist("Name_"+i, "seff", ""+i, "pwd"+i);
		}
		assertEquals(11, dbean.countDentists());
		dbean.deleteDentistByUsername(""+4);
		assertEquals(10, dbean.countDentists());
		dbean.deleteDentists();
		assertEquals(0, dbean.countDentists());
	}
}
