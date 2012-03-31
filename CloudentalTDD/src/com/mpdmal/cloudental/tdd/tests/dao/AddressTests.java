package com.mpdmal.cloudental.tdd.tests.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mpdmal.cloudental.entities.Address;
import com.mpdmal.cloudental.entities.AddressPK;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.tdd.base.CDentAbstractDaoTest;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.InvalidAddressTypeException;

public class AddressTests extends CDentAbstractDaoTest {
	@Test
	public void testFailTypeAddress() throws InvalidAddressTypeException {
		AddressPK id = new AddressPK();
		try {
			id.setAdrstype(11);
		} catch (InvalidAddressTypeException e) {
			assertEquals("Address type is invalid:11", e.getMessage());
		}
	}
	
	@Test
	public void testHomeAddress() throws InvalidAddressTypeException {
		Patient p = _patientdao.getPatient("Demo Dentist", "Ko", "Pat");
		assertEquals(0, _adrdao.getEveryAddress(p.getId()).size());

		AddressPK id = new AddressPK();
		id.setAdrstype(CloudentUtils.AddressType.HOME.getValue());
		id.setId(p.getId());
		Address adr = new Address();
		adr.setCountry("Greece");
		adr.setCity("Athens");
		adr.setNumber(11);
		adr.setPatient(p);
		adr.setPostalcode("123 32");
		adr.setStreet("G. Sisini St.");
		adr.setId(id);
		p.addAddress(adr);
		_patientdao.updateCreate(p, true);
		
		assertEquals(1, _adrdao.getEveryAddress(p.getId()).size());
	}
	
	@Test
	public void testOfficeAddress() throws InvalidAddressTypeException {
		Patient p = _patientdao.getPatient("Demo Dentist", "Ko", "Pat");
		AddressPK id = new AddressPK();
		id.setAdrstype(CloudentUtils.AddressType.OFFICE.getValue());
		id.setId(p.getId());
		Address adr = new Address();
		adr.setCountry("Greece");
		adr.setCity("Athens");
		adr.setNumber(33);
		adr.setPatient(p);
		adr.setPostalcode("222 22");
		adr.setStreet("Backend St.");
		adr.setId(id);
		p.addAddress(adr);
		_patientdao.updateCreate(p, true);
		
		assertEquals(2, _adrdao.getEveryAddress(p.getId()).size());
	}

	@Test
	public void testBillingAddress() throws InvalidAddressTypeException {
		Patient p = _patientdao.getPatient("Demo Dentist", "Ko", "Pat");

		AddressPK id = new AddressPK();
		id.setAdrstype(CloudentUtils.AddressType.BILLING.getValue());
		id.setId(p.getId());
		Address adr = new Address();
		adr.setCountry("Greece");
		adr.setCity("Athens");
		adr.setNumber(99);
		adr.setPatient(p);
		adr.setPostalcode("111 11");
		adr.setStreet("Elf street");
		adr.setId(id);
		p.addAddress(adr);
		_patientdao.updateCreate(p, true);
		
		assertEquals(3, _adrdao.getEveryAddress(p.getId()).size());
	}

}
