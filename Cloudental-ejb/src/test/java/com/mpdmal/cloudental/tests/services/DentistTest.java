package com.mpdmal.cloudental.tests.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Vector;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.tests.base.ArquillianCloudentTest;
import com.mpdmal.cloudental.util.exception.DentistExistsException;
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
		assertEquals(10, dbean.getDentists().size());
		
		//create identical
		dbean.createDentist("A", "surname", "asd", "pwd");
		try {
			dbean.createDentist("A", "surname", "asd", "pwd");
		} catch (DentistExistsException e) {
			assertEquals("Dentist already Exists:asd\nAlready exists, wont create", e.getMessage());
		}
		
		//failed create ..constraints violated
		try {
			dbean.createDentist(null, "surname", "asd1", "pwd");
		} catch (ValidationException e) {
			assertEquals("Property->NAME on Entity->DENTIST may not be null\n", e.getMessage());
		}
		
		try {
			dbean.createDentist("A", null, "as2d", "pwd");
		} catch (ValidationException e) {
			assertEquals("Property->SURNAME on Entity->DENTIST may not be null\n", e.getMessage());
		}

		try {
			dbean.createDentist("A", "surname", null, "pwd");
		} catch (ValidationException e) {
			assertEquals("Property->USERNAME on Entity->DENTIST may not be null\n", e.getMessage());
		}

		try {
			dbean.createDentist("A", "surname", "asd3", null);
		} catch (ValidationException e) {
			assertEquals("Property->PASSWORD on Entity->DENTIST may not be null\n", e.getMessage());
		}
	}
	
	
	@Test
	@InSequence (2)
	public void getAndCount () {
		//get by username
		Dentist d = dbean.getDentist("1");
		
		//failed get, wrong username 
		d = dbean.getDentist("Arilou");
		
		System.out.println(d.getXML());
		//get by dentist id
		Collection<Discount> dscs = dsvcbean.getDiscounts(d.getId());
		assertEquals(13, dscs.size());
		//get from dentist object
		assertEquals(13, d.getDiscounts().size());
		
		for (Discount discount : dscs) {
			if (discount.getDiscount().doubleValue() == -100.0) {
				assertEquals("neg discount title", discount.getTitle());
				assertEquals("" , discount.getDescription());
				continue;
			}
			assertEquals(d.getId(), discount.getDentist().getId());
			assertEquals("some descr ...", discount.getDescription());
		}
		
		//count discounts
		assertEquals(13, dsvcbean.countDiscounts());
	}
	@Test
	@InSequence (3)
	public void update() throws CloudentException {
		Vector<Dentist> dentists = dbean.getDentists();
		
		Dentist d = dentists.elementAt(0);
		d.setUsername("Altered");
		dbean.updateDentist(d);
		
		d = dbean.getDentist("Altered");
		assertNotNull(d);
	}

	@Test
	@InSequence (4)
	public void delete() throws CloudentException {
		
		Vector<Dentist> dentists = dbean.getDentists();
		assertEquals(11, dentists.size());
		Dentist d = dentists.elementAt(3);
		dbean.deleteDentist(d);
		assertEquals(10, dbean.countDentists());
		
		dentists = dbean.getDentists();
		assertEquals(10, dentists.size());
		for (Dentist dentist : dentists) {
			dbean.deleteDentist(dentist);
		}
		assertEquals(0, dbean.countDentists());
		
		for (int i = 0; i < 11; i++) {
			dbean.createDentist("Name_"+i, "seff", ""+i, "pwd"+i);
		}
		assertEquals(11, dbean.countDentists());
		dbean.deleteDentist(""+4);
		assertEquals(10, dbean.countDentists());
		dbean.deleteDentists();
		assertEquals(0, dbean.countDentists());
	}
}
