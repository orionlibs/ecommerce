package de.hybris.platform.testframework.runlistener;

import de.hybris.platform.testframework.Transactional;
import de.hybris.platform.tx.Transaction;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class TransactionRunListener extends RunListener
{
    private static final Logger log = Logger.getLogger(TransactionRunListener.class.getName());
    private volatile Transaction curTrans;


    public void testStarted(Description description) throws Exception
    {
        Transactional anno = (Transactional)description.getAnnotation(Transactional.class);
        if(anno != null)
        {
            transactionalTestStarted(description, anno);
        }
        else
        {
            nonTransactionalTestStarted(description);
        }
    }


    public void testFinished(Description description) throws Exception
    {
        if(this.curTrans != null)
        {
            transactionalTestFinished(description, (Transactional)description.getAnnotation(Transactional.class));
        }
        else
        {
            nonTransactionalTestFinished(description);
        }
    }


    private void nonTransactionalTestFinished(Description description)
    {
        assertNoTransactionRunning(description, false);
    }


    private void transactionalTestFinished(Description description, Transactional anno)
    {
        try
        {
            if(this.curTrans.isRunning())
            {
                rollbackTestTx(description, this.curTrans, anno);
            }
            else
            {
                log.error("Can not rollback transaction after test " + description
                                .getDisplayName() + " - it is not running anymore", new Exception("Illegal Test Tx Outcome of " + description
                                .getDisplayName()));
            }
        }
        finally
        {
            this.curTrans = null;
        }
    }


    private void nonTransactionalTestStarted(Description description)
    {
        assertNoTransactionRunning(description, true);
        if(log.isDebugEnabled())
        {
            log.debug("Transactional support is not enabled for test " + description.getDisplayName());
        }
    }


    private void transactionalTestStarted(Description description, Transactional anno)
    {
        assertNoTransactionRunning(description, true);
        this.curTrans = startTestTransaction(description, anno);
        if(log.isDebugEnabled())
        {
            log.debug("Transactional support enabled for test " + description.getDisplayName() + " with " + (
                            anno.enableDelayedStore() ? "enabled" : "disabled") + " delayed store");
        }
    }


    private Transaction startTestTransaction(Description description, Transactional anno)
    {
        try
        {
            Transaction tx = Transaction.current();
            tx.begin();
            tx.enableDelayedStore(anno.enableDelayedStore());
            return tx;
        }
        catch(Exception e)
        {
            log.error("Error starting test transaction before test " + description.getDisplayName(), e);
            return null;
        }
    }


    private void rollbackTestTx(Description description, Transaction tx, Transactional anno)
    {
        boolean commit = anno.noRollback();
        try
        {
            int counter = 0;
            do
            {
                if(commit)
                {
                    tx.commit();
                }
                else
                {
                    tx.rollback();
                }
                counter++;
            }
            while(tx.isRunning());
            if(counter > 1)
            {
                log.error("Found transaction open after performing test " + description
                                .getDisplayName() + " - required " + counter + " attempts to finish transaction", new Exception("Illegal Test Tx Outcome: " + description
                                .getDisplayName()));
            }
        }
        catch(Exception e)
        {
            log.error("Error committing transaction after test " + description.getDisplayName(), e);
        }
    }


    private void assertNoTransactionRunning(Description description, boolean before)
    {
        String when = before ? "before" : "after";
        Transaction tx = Transaction.current();
        if(tx.isRunning())
        {
            String msg = "Found running transaction " + tx + " " + when + "starting " + description.getDisplayName() + " - trying to rollback..";
            if(before)
            {
                log.error(msg);
            }
            else
            {
                log.error(msg, new Exception("Illegal Test Tx Outcome: " + description.getDisplayName()));
            }
            rollbackLeftoverTx(tx);
        }
    }


    private void rollbackLeftoverTx(Transaction tx)
    {
        try
        {
            do
            {
                tx.rollback();
            }
            while(tx.isRunning());
        }
        catch(Exception e)
        {
            log.error("Error rolling back leftover transaction " + tx, e);
        }
    }
}
