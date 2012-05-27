package com.mpdmal.cloudental.tests.services;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Vector;

import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.tests.base.ArquillianCloudentTestBase;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.PricelistItemNotFoundException;
import com.mpdmal.cloudental.util.exception.ValidationException;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class PricelistTestSet extends ArquillianCloudentTestBase {
	@Test 
	@InSequence (1)
	public void create() throws CloudentException {
		Dentist d = dbean.createDentist("Dim", "Az", "Arilou", "arilou");
		assertEquals(1, dbean.countDentists());
		assertEquals(0, dsvcbean.countPricelistItems());
		
		//create
		for (int i = 0; i < 10; i++) {
			dsvcbean.createPricelistItem(d.getId(), "pricelist item title"+i, "some descr ...", 10.0+i);
		}
		assertEquals(10, dsvcbean.countPricelistItems());
		
		//create identical
		dsvcbean.createPricelistItem(d.getId(), "pricelist item title", "some descr ...", 100.0);		
		dsvcbean.createPricelistItem(d.getId(), "pricelist item title", "some descr ...", 100.0);
		
		//failed create ..invalid dentist
		try {
			dsvcbean.createPricelistItem(-100, "title", "descr ...", 100.0);
		} catch (DentistNotFoundException e) {
			assertEquals("Dentist id not found:-100\n", e.getMessage());
		}
		
		//failed create ..constraints violated
		try {
			dsvcbean.createPricelistItem(d.getId(), null, "descr ...", 99.0);
		} catch (ValidationException e) {
			assertEquals("Property->TITLE on Entity->PRICELISTITEM may not be null\n", e.getMessage());
		}
		
		//valid create , no descr , neg. value
		dsvcbean.createPricelistItem(d.getId(), "neg pricelist item title", null, -100.0);
		assertEquals(13, dsvcbean.countPricelistItems());
	}
	
	@Test
	@InSequence (2)
	public void getAndCount () throws DentistNotFoundException {
		Dentist d = dbean.findDentistByUsername("Arilou");
		//get by dentist id
		Collection<PricelistItem> dscs = dsvcbean.getPricelist(d.getId());
		assertEquals(13, dscs.size());
		//get from dentist object
		assertEquals(13, d.getPriceList().size());
		
		for (PricelistItem item : dscs) {
			if (item.getPrice().doubleValue() == -100.0) {
				assertEquals("neg pricelist item title", item.getTitle());
				assertEquals("" , item.getDescription());
				continue;
			}
			assertEquals(d.getId(), item.getDentist().getId());
			assertEquals("some descr ...", item.getDescription());
		}
		
		//count pricelist items
		assertEquals(13, dsvcbean.countPricelistItems());
	}
	
	@Test 
	@InSequence (3)
	public void update() throws CloudentException {
		Collection<PricelistItem> dscs = dsvcbean.getPricelist(dbean.findDentistByUsername("Arilou").getId());
		//update pricelist items
		for (PricelistItem item : dscs) {
			dsvcbean.updatePricelistItem(item.getId(), "altered!!!", " altered title");
		}
		
		dscs = (Collection<PricelistItem>)dsvcbean.getPricelist(dbean.findDentistByUsername("Arilou").getId());
		for (PricelistItem item : dscs) {
			assertEquals("altered!!!", item.getDescription());
			assertEquals(" altered title", item.getTitle());
		}
	}

	@Test
	@InSequence (4)
	public void delete() throws CloudentException {
		assertEquals(13, dsvcbean.countPricelistItems());

		Dentist dentist = dbean.findDentistByUsername("Arilou");
		Vector<PricelistItem> dscs = (Vector<PricelistItem>)dsvcbean.getPricelist(dentist.getId());
		//delete by id
		for (int i = 0; i < 5; i++) {
			dsvcbean.deletePricelistItem(dscs.elementAt(i).getId());
		}
		
		//failed delete ...invalid id
		try {
			dsvcbean.deletePricelistItem(-1);
		} catch (PricelistItemNotFoundException e) {
			assertEquals("Pricelist Item id not found:-1\n", e.getMessage());
		}
		
		assertEquals(8, dsvcbean.countPricelistItems());
		try {
			dsvcbean.deletePricelist(dentist.getId());
		} catch (DentistNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(0, dsvcbean.countPricelistItems());
		assertEquals(1, dbean.countDentists());
		
		//cascade delete by dentist
		for (int i = 0; i < 10; i++) 
			dsvcbean.createPricelistItem(dentist.getId(), "pricelist item title"+i, "some descr ...", 10.0+i);
		assertEquals(10, dsvcbean.countPricelistItems());

		dbean.deleteDentistByUsername("Arilou");		
		assertEquals(0, dsvcbean.countPricelistItems());
	}
}
