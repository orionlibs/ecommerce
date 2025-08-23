package de.hybris.platform.servicelayer.tx;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

public class MockTransactionManager extends AbstractPlatformTransactionManager
{
    protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException
    {
    }


    protected void doCommit(DefaultTransactionStatus status) throws TransactionException
    {
    }


    protected Object doGetTransaction() throws TransactionException
    {
        return new Object();
    }


    protected void doRollback(DefaultTransactionStatus status) throws TransactionException
    {
    }
}
