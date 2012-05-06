package com.mpdmal.cloudental.tdd;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.tdd.tests.bean.DentistServicesTests;
import com.mpdmal.cloudental.tdd.tests.bean.DentistTests;
import com.mpdmal.cloudental.tdd.tests.bean.PatientServicesTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	DentistTests.class,
	DentistServicesTests.class,
	PatientServicesTests.class
})

public class TestSuite {
 
}