package com.mpdmal.cloudental.util;

import javax.transaction.TransactionManager;

public class CDentJTATransactionController extends org.eclipse.persistence.transaction.JTATransactionController {
	public static final String JNDI_TRANSACTION_MANAGER_NAME = "java:comp/TransactionManager";
	public CDentJTATransactionController() { super(); }

	@Override
	protected TransactionManager acquireTransactionManager() throws Exception {
		return (TransactionManager) jndiLookup(JNDI_TRANSACTION_MANAGER_NAME);
	}
}