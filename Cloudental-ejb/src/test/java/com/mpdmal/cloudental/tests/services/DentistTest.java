package com.mpdmal.cloudental.tests.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Vector;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.tests.base.ArquillianCloudentTest;
import com.mpdmal.cloudental.tests.util.TestUtils;
import com.mpdmal.cloudental.util.exception.base.CloudentException;

@RunWith(Arquillian.class)
public class DentistTest extends ArquillianCloudentTest{
	
	@Test
	public void testCreate() throws CloudentException {
		assertEquals(0, dbean.countDentists());
		for (int i = 0; i < 10; i++) {
			dbean.createDentist("Name_"+i, "surname", ""+i, "pwd"+i);
			
		}
		Collection<Dentist> aaa = dbean.getDentists();
		for (Dentist dentist : aaa) {
			System.out.println(dentist.getId());
		}
		assertEquals(10, aaa.size());
	}
	
	@Test
	public void testUpdate() throws CloudentException {
		Vector<Dentist> dentists = dbean.getDentists();
		
		Dentist d = dentists.elementAt(0);
		d.setUsername("Altered");
		dbean.updateDentist(d);
		
		d = dbean.getDentist("Altered");
		assertNotNull(d);
	}

	@Test
	public void testDelete() throws CloudentException {
		
		Vector<Dentist> dentists = dbean.getDentists();
		assertEquals(10, dentists.size());
		Dentist d = dentists.elementAt(3);
		dbean.deleteDentist(d);
		assertEquals(9, dbean.countDentists());
		
		dentists = dbean.getDentists();
		assertEquals(9, dentists.size());
		for (Dentist dentist : dentists) {
			dbean.deleteDentist(dentist);
		}
		assertEquals(0, dbean.countDentists());
		
		for (int i = 0; i < 10; i++) {
			dbean.createDentist("Name_"+i, "seff", ""+i, "pwd"+i);
		}
		assertEquals(10, dbean.countDentists());
		dbean.deleteDentist(""+4);
		assertEquals(9, dbean.countDentists());
		dbean.deleteDentists();
		assertEquals(0, dbean.countDentists());
	}
}
