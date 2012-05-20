package com.mpdmal.cloudental.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mpdmal.cloudental.tests.services.DentistServicesTest;
import com.mpdmal.cloudental.tests.services.DentistTest;
import com.mpdmal.cloudental.tests.services.PatientServicesTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	DentistTest.class,
	DentistServicesTest.class,
	PatientServicesTest.class
})

public class TestSuite {
 
}