package com.mpdmal.cloudental.tdd.tests.dao;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.tdd.base.CDentAbstractDaoTest;

public class PricelistTests extends CDentAbstractDaoTest {
	@Before
	public void initTestEnv() {
		Dentist _aDentist = new Dentist();
		_aDentist.setUsername("Arilou");
		_aDentist.setPassword("admin");
		_aDentist.setName("Dim");
		_aDentist.setSurname("Aza");
		_dentistdao.updateCreate(_aDentist, false);
		
		Dentist _aDentist2 = new Dentist();
		_aDentist2.setUsername("Mika");
		_aDentist2.setPassword("admin");
		_aDentist2.setName("Dim");
		_aDentist2.setSurname("Aza");
		_dentistdao.updateCreate(_aDentist2, false);

		assertEquals(2, _dentistdao.countDentists());
	}
	
	@Test
	public void testPricelist() {
		Dentist d = _dentistdao.getDentist("Arilou");
		PricelistItem p = new PricelistItem();

		p.setDentist(d);
		p.setDescription("");
		p.setPrice(BigDecimal.valueOf(100.0));
		p.setTitle("");
		d.addPricelistItem(p);
		_pcdao.updateCreate(p, false);
		assertEquals(1, _pcdao.countPricelistItems(d.getUsername()));
		_pcdao.delete(p);
		assertEquals(0, _pcdao.countPricelistItems(d.getUsername()));
		
		d.addPricelistItem(p);
		_dentistdao.updateCreate(d, true);
		assertEquals(1, _pcdao.countPricelistItems(d.getUsername()));
	}
	
	@Override
	public void closeTestEnv() {
		Vector<Dentist> dentists = _dentistdao.getDentists();
		for (Dentist dentist : dentists) {
			_dentistdao.delete(dentist);	//cascade delete
		}
		assertEquals(0, _dentistdao.countDentists());
		assertEquals(0, _postitdao.countPostits());
	}

}
