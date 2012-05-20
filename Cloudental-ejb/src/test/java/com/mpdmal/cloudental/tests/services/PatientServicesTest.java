package com.mpdmal.cloudental.tests.services;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.junit.Test;

import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.tests.base.ArquillianCloudentTest;
import com.mpdmal.cloudental.util.exception.DentistExistsException;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.DiscountNotFoundException;
import com.mpdmal.cloudental.util.exception.InvalidDentistCredentialsException;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;
import com.mpdmal.cloudental.util.exception.PatientExistsException;
import com.mpdmal.cloudental.util.exception.PatientNotFoundException;
import com.mpdmal.cloudental.util.exception.PricelistItemNotFoundException;


public class PatientServicesTest extends ArquillianCloudentTest{
	
	@Test
	public void createActivity() throws DentistExistsException,
												InvalidDentistCredentialsException,
												DentistNotFoundException,
												PatientExistsException, 
												PatientNotFoundException,
												InvalidPostitAlertException, 
												DiscountNotFoundException, 
												PricelistItemNotFoundException, NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		
		assertEquals(0, dbean.countDentists());
		//utx.begin();
		Dentist d = dbean.createDentist("dim", "az", "arilou", "coersum");
		//utx.commit();
		Patient p = dsvcbean.createPatient(d.getId(), "ko", "pat");
		Discount dc = dsvcbean.createDiscount(d.getId(), "discount 1", "sddf", .30);
		PricelistItem item = dsvcbean.createPricelistItem(d.getId(), "item 1", "asdasd", 23.12);
		
		Activity ac = psvcbean.createActivity(p.getId(), "activity 1", new Date(), null, item.getId(), dc.getId());
		dbean.deleteDentists();
	}
	
}