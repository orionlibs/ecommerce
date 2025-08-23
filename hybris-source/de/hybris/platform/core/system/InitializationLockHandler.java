package de.hybris.platform.core.system;

import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.Tenant;
import java.io.Serializable;
import java.util.concurrent.Callable;

public class InitializationLockHandler implements Serializable
{
    private static final long serialVersionUID = 5226558116518909824L;
    final InitializationLockDao initializationDao;


    public InitializationLockHandler(InitializationLockDao initializationDao)
    {
        this.initializationDao = initializationDao;
    }


    public InitializationLockInfo getLockInfo()
    {
        return this.initializationDao.readLockInfo();
    }


    public boolean isLocked()
    {
        InitializationLockInfo info = getLockInfo();
        return (info != null && info.isLocked());
    }


    public boolean performLocked(Tenant forTenant, Callable<Boolean> operation, String message) throws Exception
    {
        if(lock(forTenant, message))
        {
            try
            {
                operation.call();
                return true;
            }
            finally
            {
                unlock(forTenant);
            }
        }
        return false;
    }


    public boolean lock(Tenant forTenant, String message)
    {
        assertLockRowExists();
        return this.initializationDao.lockRow(forTenant, message);
    }


    public void unlock(Tenant forTenant)
    {
        InitializationLockInfo info = getLockInfo();
        if(info != null)
        {
            if(info.getClusterNodeId() != ((AbstractTenant)forTenant).getClusterID())
            {
                throw new IllegalStateException("lock is owned by different cluster node " + info.getClusterNodeId() + " than " + ((AbstractTenant)forTenant)
                                .getClusterID() + " from tenant " + forTenant);
            }
            this.initializationDao.releaseRow(forTenant);
        }
    }


    private void assertLockRowExists()
    {
        if(this.initializationDao.readLockInfo() == null)
        {
            synchronized(InitializationLockHandler.class)
            {
                if(!this.initializationDao.insertRow())
                {
                    this.initializationDao.createTable();
                    if(!this.initializationDao.insertRow())
                    {
                        throw new IllegalStateException("could not create row or table");
                    }
                }
            }
        }
    }
}
