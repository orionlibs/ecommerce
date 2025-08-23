package de.hybris.platform.catalog.jalo.synchronization;

import org.apache.log4j.Logger;

public abstract class AbstractWorkerLockHolder implements WorkerItemLockHolder
{
    private static final Logger LOG = Logger.getLogger(AbstractWorkerLockHolder.class.getName());


    public synchronized void release(long pk, int workerNumber)
    {
        doRelease(workerNumber, pk);
        notifyAll();
    }


    public synchronized boolean lock(long pk, int workerNumber, long timeout)
    {
        boolean gotTimeout = (timeout > 0L);
        long waitUntil = gotTimeout ? (System.currentTimeMillis() + timeout * 1000L) : 0L;
        while(!doLock(pk, workerNumber))
        {
            if(timeout == 0L)
            {
                return false;
            }
            waitForLock(gotTimeout, waitUntil, workerNumber);
            if(gotTimeout && System.currentTimeMillis() > waitUntil)
            {
                return false;
            }
        }
        return true;
    }


    protected abstract void doRelease(int paramInt, long paramLong);


    protected abstract boolean doLock(long paramLong, int paramInt);


    protected void waitForLock(boolean gotTimeout, long waitUntil, int workerNumber)
    {
        try
        {
            if(gotTimeout)
            {
                long msToWait = Math.max(1L, waitUntil - System.currentTimeMillis());
                wait(msToWait);
            }
            else
            {
                wait();
            }
        }
        catch(InterruptedException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("worker " + workerNumber + " interrupted!");
            }
        }
    }
}
