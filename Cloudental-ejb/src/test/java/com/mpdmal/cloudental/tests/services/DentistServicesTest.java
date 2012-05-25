package com.mpdmal.cloudental.tests.services;

import org.junit.Test;

import com.mpdmal.cloudental.tests.base.ArquillianCloudentTest;
import static org.junit.Assert.assertEquals;

public class DentistServicesTest extends ArquillianCloudentTest {
	@Test
	public void dbTest() {
		//clean up the db before the rest of the tests :P
		dbean.deleteDentists();
		assertEquals(0, dbean.countDentists());
		assertEquals(0, dsvcbean.countDiscounts());
		assertEquals(0, dsvcbean.countPricelistItems());
		
	}

}
