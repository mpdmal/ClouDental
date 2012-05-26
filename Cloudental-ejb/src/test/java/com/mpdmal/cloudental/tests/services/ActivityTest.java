package com.mpdmal.cloudental.tests.services;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.tests.base.ArquillianCloudentTest;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

public class ActivityTest extends ArquillianCloudentTest{
	
	@Test
	public void create() throws CloudentException {
		
		assertEquals(0, dbean.countDentists());
		//utx.begin();
		Dentist d = dbean.createDentist("dim", "az", "arilou", "coersum");
		//utx.commit();
		Patient p = dsvcbean.createPatient(d.getId(), "ko", "pat");
		Discount dc = dsvcbean.createDiscount(d.getId(), "discount 1", "sddf", .30);
		PricelistItem item = dsvcbean.createPricelistItem(d.getId(), "item 1", "asdasd", 23.12);
		
//		psvcbean.createActivity(p.getId(), "activity 1", new Date(), null, item.getId(), dc.getId());
		dbean.deleteDentists();
	}
	
}
