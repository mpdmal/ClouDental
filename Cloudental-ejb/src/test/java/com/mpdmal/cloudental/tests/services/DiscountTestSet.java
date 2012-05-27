package com.mpdmal.cloudental.tests.services;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Vector;

import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.tests.base.ArquillianCloudentTestBase;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.DiscountNotFoundException;
import com.mpdmal.cloudental.util.exception.ValidationException;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class DiscountTestSet extends ArquillianCloudentTestBase {
	@Test 
	@InSequence (1)
	public void create() throws CloudentException {
		Dentist d = dbean.createDentist("Dim", "Az", "Arilou", "arilou");
		assertEquals(1, dbean.countDentists());
		assertEquals(0, dsvcbean.countDiscounts());
		
		//create
		for (int i = 0; i < 10; i++) {
			dsvcbean.createDiscount(d.getId(), "discount title"+i, "some descr ...", 10.0+i);
		}
		assertEquals(10, dsvcbean.countDiscounts());
		
		//create identical
		dsvcbean.createDiscount(d.getId(), "discount title", "some descr ...", 100.0);		
		dsvcbean.createDiscount(d.getId(), "discount title", "some descr ...", 100.0);
		
		//failed create ..invalid dentist
		try {
			dsvcbean.createDiscount(-100, "title", "descr ...", 100.0);
		} catch (DentistNotFoundException e) {
			assertEquals("Dentist id not found:-100\n", e.getMessage());
		}
		
		//failed create ..constraints violated
		try {
			dsvcbean.createDiscount(d.getId(), null, "descr ...", 99.0);
		} catch (ValidationException e) {
			assertEquals("Property->TITLE on Entity->DISCOUNT may not be null\n", e.getMessage());
		}
		
		//valid create , no descr , neg. value
		dsvcbean.createDiscount(d.getId(), "neg discount title", null, -100.0);
		assertEquals(13, dsvcbean.countDiscounts());
	}
	
	@Test
	@InSequence (2)
	public void getAndCount () throws DentistNotFoundException {
		Dentist d = dbean.findDentistByUsername("Arilou");
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
		Collection<Discount> dscs = dsvcbean.getDiscounts(dbean.findDentistByUsername("Arilou").getId());
		//update discounts
		for (Discount discount : dscs) {
			dsvcbean.updateDiscount(discount.getId(), "altered!!!", " altered title");
		}
		
		dscs = (Collection<Discount>)dsvcbean.getDiscounts(dbean.findDentistByUsername("Arilou").getId());
		for (Discount discount : dscs) {
			assertEquals("altered!!!", discount.getDescription());
			assertEquals(" altered title", discount.getTitle());
		}
	}
	
	@Test
	@InSequence (4)
	public void delete() throws CloudentException {
		assertEquals(13, dsvcbean.countDiscounts());

		Dentist dentist = dbean.findDentistByUsername("Arilou");
		Vector<Discount> dscs = (Vector<Discount>)dsvcbean.getDiscounts(dentist.getId());
		//delete by id
		for (int i = 0; i < 5; i++) {
			dsvcbean.deleteDiscount(dscs.elementAt(i).getId());
		}
		
		//failed delete ...invalid id
		try {
			dsvcbean.deleteDiscount(-1);
		} catch (DiscountNotFoundException e) {
			assertEquals("Discount not found:-1\n", e.getMessage());
		}
		
		assertEquals(8, dsvcbean.countDiscounts());
		try {
			dsvcbean.deleteDiscounts(dentist.getId());
		} catch (DentistNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(0, dsvcbean.countDiscounts());
		assertEquals(1, dbean.countDentists());
		
		//cascade delete by dentist
		for (int i = 0; i < 10; i++) 
			dsvcbean.createDiscount(dentist.getId(), "discount title"+i, "some descr ...", 10.0+i);
		assertEquals(10, dsvcbean.countDiscounts());

		dbean.deleteDentistByUsername ("Arilou");		
		assertEquals(0, dsvcbean.countDiscounts());
	}
}
