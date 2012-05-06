package com.mpdmal.cloudental.tdd.tests.bean;

import java.util.Date;

import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mpdmal.cloudental.EaoManager;
import com.mpdmal.cloudental.beans.DentistBean;
import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.util.exception.DentistExistsException;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.DiscountNotFoundException;
import com.mpdmal.cloudental.util.exception.InvalidDentistCredentialsException;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;
import com.mpdmal.cloudental.util.exception.PatientExistsException;
import com.mpdmal.cloudental.util.exception.PatientNotFoundException;
import com.mpdmal.cloudental.util.exception.PricelistItemNotFoundException;

import static org.junit.Assert.assertEquals;

public class PatientServicesTests {
	EaoManager egr ;

	@Before
	public void pre() {
		egr = new EaoManager(Persistence.createEntityManagerFactory("CloudentalTDD").createEntityManager());
	}
	
	@After
	public void post() {
		egr.closeEM();
	}
	@Test
	public void createActivity() throws DentistExistsException,
												InvalidDentistCredentialsException,
												DentistNotFoundException,
												PatientExistsException, 
												PatientNotFoundException,
												InvalidPostitAlertException, 
												DiscountNotFoundException, 
												PricelistItemNotFoundException {
		DentistBean dbean = new DentistBean(egr);
		DentistServices dsvcbean = new DentistServices(egr);
		PatientServices psvcbean = new PatientServices(egr);
		
		assertEquals(0, dbean.countDentists());
		Dentist d = dbean.createDentist("dim", "az", "arilou", "coersum");
		Patient p = dsvcbean.createPatient(d.getId(), "ko", "pat");
		Discount dc = dsvcbean.createDiscount(d.getId(), "discount 1", "sddf", .30);
		PricelistItem item = dsvcbean.createPricelistItem(d.getId(), "item 1", "asdasd", 23.12);
		
		Activity ac = psvcbean.createActivity(p.getId(), "activity 1", new Date(), null, item.getId(), dc.getId());
		dbean.deleteDentists();
	}
}