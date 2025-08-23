package de.hybris.platform.spring;

import de.hybris.platform.tx.Transaction;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

public class HybrisTransactionManager extends AbstractPlatformTransactionManager
{
    protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException
    {
        try
        {
            ((Transaction)transaction).begin();
        }
        catch(Exception e)
        {
            throw toSpringTxException(e);
        }
    }


    protected void doCommit(DefaultTransactionStatus status) throws TransactionException
    {
        try
        {
            ((Transaction)status.getTransaction()).commit();
        }
        catch(Exception e)
        {
            throw toSpringTxException(e);
        }
    }


    protected Object doGetTransaction() throws TransactionException
    {
        try
        {
            return Transaction.current();
        }
        catch(Exception e)
        {
            throw toSpringTxException(e);
        }
    }


    protected void doRollback(DefaultTransactionStatus status) throws TransactionException
    {
        try
        {
            ((Transaction)status.getTransaction()).rollback();
        }
        catch(Exception e)
        {
            throw toSpringTxException(e);
        }
    }


    protected boolean isExistingTransaction(Object transaction) throws TransactionException
    {
        try
        {
            return ((Transaction)transaction).isRunning();
        }
        catch(Exception e)
        {
            throw toSpringTxException(e);
        }
    }


    protected void doSetRollbackOnly(DefaultTransactionStatus status) throws TransactionException
    {
        try
        {
            ((Transaction)status.getTransaction()).setRollbackOnly();
        }
        catch(Exception e)
        {
            throw toSpringTxException(e);
        }
    }


    protected TransactionException toSpringTxException(Exception jaloException)
    {
        if(jaloException instanceof de.hybris.platform.tx.RollbackOnlyException)
        {
            return (TransactionException)new UnexpectedRollbackException(jaloException.getMessage(), jaloException);
        }
        if(jaloException instanceof de.hybris.platform.tx.IllegalTransactionStateException)
        {
            return (TransactionException)new IllegalTransactionStateException(jaloException.getMessage(), jaloException);
        }
        if(jaloException instanceof de.hybris.platform.tx.TransactionException)
        {
            return (TransactionException)new TransactionSystemException(jaloException.getMessage(), jaloException);
        }
        if(jaloException instanceof RuntimeException)
        {
            return (TransactionException)new TransactionSystemException(jaloException.getMessage(), jaloException);
        }
        return (TransactionException)new TransactionSystemException(jaloException.getMessage(), jaloException);
    }
}
