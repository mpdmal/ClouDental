package com.mpdmal.cloudental.tdd.tests.dao;

import java.util.Vector;

import org.junit.Test;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.tdd.base.CDentAbstractDaoTest;

import static org.junit.Assert.assertEquals;

public class DentistTests extends CDentAbstractDaoTest {
	Dentist _aDentist, _aDentist2;
	@Override
	public void initTestEnv() {
	}

	@Test
	public void testDentists() {
		assertEquals(null, _dentistdao.getDentist("ASDAD"));
		assertEquals(0, _dentistdao.countDentists());
		_aDentist = new Dentist();
		_aDentist.setUsername("Arilou");
		_aDentist.setPassword("admin");
		_aDentist.setName("Dim");
		_aDentist.setSurname("Aza");
		_dentistdao.updateCreate(_aDentist, false);
		
		_aDentist2 = new Dentist();
		_aDentist2.setUsername("Mika");
		_aDentist2.setPassword("nimda");
		_aDentist2.setName("Nat");
		_aDentist2.setSurname("Bourne");
		_dentistdao.updateCreate(_aDentist2, false);


		//verify env, test count
		assertEquals(2, _dentistdao.countDentists());
		assertEquals("Arilou", _aDentist.getUsername());
		assertEquals("Mika", _aDentist2.getUsername());
		
		//test update
		_aDentist.setName("Azza");
		_dentistdao.updateCreate(_aDentist, true);
		_aDentist2.setSurname("Borne");
		_dentistdao.updateCreate(_aDentist2, true);
		
		//test get
		_aDentist = _dentistdao.getDentist("Arilou");
		_aDentist2 = _dentistdao.getDentist("Mika");
		assertEquals(null, _dentistdao.getDentist("Noone!"));
		assertEquals("Azza", _aDentist.getName());
		assertEquals("Borne", _aDentist2.getSurname());

		//test delete
		Vector<Dentist> dentists = _dentistdao.getDentists();
		for (Dentist dentist : dentists) {
			_dentistdao.delete(dentist);
		}
		assertEquals(0, _dentistdao.countDentists());
	}
}
