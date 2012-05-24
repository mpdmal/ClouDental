package com.mpdmal.cloudental.tests.services;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Vector;

import org.junit.Test;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.tests.base.ArquillianCloudentTest;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.DiscountNotFoundException;
import com.mpdmal.cloudental.util.exception.ValidationException;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class DentistServicesTest extends ArquillianCloudentTest{
	@Test 
	public void createDiscount() throws CloudentException {
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
	public void getDiscounts () {
		Dentist d = dbean.getDentist("Arilou");
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
	public void updateDiscount() {
		Collection<Discount> dscs = dsvcbean.getDiscounts(dbean.getDentist("Arilou").getId());
		//update discounts
		for (Discount discount : dscs) {
			dsvcbean.updateDiscount(discount.getId(), "altered!!!", " altered title");
		}
		
		dscs = (Collection<Discount>)dsvcbean.getDiscounts(dbean.getDentist("Arilou").getId());
		for (Discount discount : dscs) {
			assertEquals("altered!!!", discount.getDescription());
			assertEquals(" altered title", discount.getTitle());
		}
	}
	@Test
	public void deleteDiscount() throws DiscountNotFoundException, DentistNotFoundException, ValidationException {
		assertEquals(13, dsvcbean.countDiscounts());

		Dentist dentist = dbean.getDentist("Arilou");
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

		dbean.deleteDentist("Arilou");		
		assertEquals(0, dsvcbean.countDiscounts());
	}
	
	@Test
	public void createPricelistItem() throws CloudentException  {
		assertEquals(0, dsvcbean.countPricelistItems());
		Dentist d = dbean.createDentist("qwe", "asd", "zxc", "vsd");
		for (int i = 0; i < 10; i++) {
			dsvcbean.createPricelistItem(d.getId(), "pc"+i, "wef"+i, i*10.1);			
		}
		
		Collection<PricelistItem> pricelist = dsvcbean.getPricelist(d.getId());
		assertEquals(10, pricelist.size());
	}

	@Test
	public void updatePricelistItems() throws CloudentException {

		Dentist d = dbean.getDentist("zxc");
		Collection<PricelistItem> pricelist = dsvcbean.getPricelist(d.getId());
		assertEquals(10, pricelist.size());

		PricelistItem item = pricelist.iterator().next();
		double price = item.getPrice().doubleValue();
		dsvcbean.updatePricelistItem(item.getId(), "altered!", "rty");
		pricelist = dsvcbean.getPricelist(d.getId());
		for (PricelistItem pricelistItem : pricelist) {
			if (pricelistItem.getPrice().doubleValue() == price)
				assertEquals(pricelistItem.getDescription(), "altered!");
		}
	}

	@Test
	public void deletePricelistItems() throws CloudentException {

		assertEquals(1, dbean.countDentists());
		Dentist d = dbean.getDentist("zxc");
		Collection<PricelistItem> pricelist = dsvcbean.getPricelist(d.getId());
		assertEquals(10, pricelist.size());

		dsvcbean.deletePricelistItem(pricelist.iterator().next().getId());
		pricelist = dsvcbean.getPricelist(d.getId());
		assertEquals(9, pricelist.size());
		assertEquals(1, dbean.countDentists());

		for (PricelistItem pricelistItem : pricelist) {
			if (pricelistItem.getPrice().doubleValue() < 40)
				dsvcbean.deletePricelistItem(pricelistItem.getId());
		}
		assertEquals(6, dsvcbean.countPricelistItems());
		dbean.deleteDentists();
		assertEquals(0, dsvcbean.countPricelistItems());
	}

	@Test
	public void createPatient() throws CloudentException {
		Dentist d = dbean.createDentist("qwe", "asd", "zxc", "vsd");
		for (int i = 0; i < 10; i++) {
			dsvcbean.createPatient(d.getId(), "PAtient", "PAtient surname");	
		}
	}

	@Test 
	public void deletePatient() throws CloudentException {

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
