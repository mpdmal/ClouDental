package com.mpdmal.cloudental.tdd.tests.dao;

import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.mpdmal.cloudental.entities.Medicalhistoryentry;
import com.mpdmal.cloudental.entities.MedicalhistoryentryPK;
import com.mpdmal.cloudental.tdd.base.CDentAbstractDaoTest;
import com.mpdmal.cloudental.util.exception.InvalidMedEntryAlertException;

public class MedHistoryEntryTests extends CDentAbstractDaoTest {

	@Test
	public void testEntries() throws InvalidMedEntryAlertException {
		assertEquals(0, _medhentrydao.countMedicalhistoryentrys());
		
		MedicalhistoryentryPK id = new MedicalhistoryentryPK();
		id.setAdded(new Date());
		id.setId(71);

		Medicalhistoryentry entry = new Medicalhistoryentry();
		entry.setAlert(1);
		entry.setComments("asdasd");
		entry.setId(id);
		_medhentrydao.updateCreate(entry, false); //create
		entry.setComments("altered");
		_medhentrydao.updateCreate(entry, true); //update
		_medhentrydao.delete(entry); //delete
	}
}
