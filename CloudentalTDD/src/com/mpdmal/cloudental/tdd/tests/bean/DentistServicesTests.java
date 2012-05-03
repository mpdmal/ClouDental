package com.mpdmal.cloudental.tdd.tests.bean;

import java.util.Collection;

import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mpdmal.cloudental.EaoManager;
import com.mpdmal.cloudental.beans.DentistBean;
import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.util.exception.DentistExistsException;
import com.mpdmal.cloudental.util.exception.InvalidDentistCredentialsException;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;

import static org.junit.Assert.assertEquals;

public class DentistServicesTests {
	EaoManager egr ;

	@Before
	public void pre() {
		egr = new EaoManager(Persistence.createEntityManagerFactory("CloudentalTDD").createEntityManager());
	}
	
	@After
	public void post() {
		egr.clostEM();
	}
	@Test
	public void createPricelistItem() throws DentistExistsException, 
												InvalidDentistCredentialsException, 
												InvalidPostitAlertException {
		DentistBean dbean = new DentistBean(egr);
		DentistServices dsvcbean = new DentistServices(egr);
		assertEquals(0, dsvcbean.countPricelistItems());
		Dentist d = dbean.createDentist("qwe", "asd", "zxc", "vsd");
		for (int i = 0; i < 10; i++) {
			dsvcbean.createPricelistItem(d.getId(), "pc"+i, "wef"+i, i*10.1);			
		}
		Collection<PricelistItem> pricelist = dsvcbean.getPricelist(d.getId());
		assertEquals(10, pricelist.size());
	}
	
	@Test
	public void updatePricelistItems() {
		DentistBean dbean = new DentistBean(egr);
		DentistServices dsvcbean = new DentistServices(egr);

		Dentist d = dbean.getDentist("zxc");
		Collection<PricelistItem> pricelist = dsvcbean.getPricelist(d.getId());
		assertEquals(10, pricelist.size());

		PricelistItem item = pricelist.iterator().next();
		double price = item.getPrice().doubleValue();
		pricelist = dsvcbean.updatePricelistItem(item.getId(), "altered!", "rty");
		
		for (PricelistItem pricelistItem : pricelist) {
			if (pricelistItem.getPrice().doubleValue() == price)
				assertEquals(pricelistItem.getDescription(), "altered!");
		}
	}
	
	@Test
	public void deletePricelistItems() {
		DentistBean dbean = new DentistBean(egr);
		DentistServices dsvcbean = new DentistServices(egr);

		assertEquals(1, dbean.countDentists());
		Dentist d = dbean.getDentist("zxc");
		Collection<PricelistItem> pricelist = dsvcbean.getPricelist(d.getId());
		assertEquals(10, pricelist.size());

		pricelist =  dsvcbean.deletePricelistItem(pricelist.iterator().next().getId());
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
}