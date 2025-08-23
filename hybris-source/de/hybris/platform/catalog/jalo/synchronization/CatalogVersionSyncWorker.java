package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.catalog.RecoverableSynchronizationPersistenceException;
import de.hybris.platform.catalog.jalo.ItemSyncTimestamp;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.persistence.hjmp.HJMPUtils;
import de.hybris.platform.util.Config;
import org.apache.log4j.Logger;

public class CatalogVersionSyncWorker implements Runnable
{
    private static final Logger LOG = Logger.getLogger(CatalogVersionSyncWorker.class.getName());
    private final String name;
    private final CatalogVersionSyncMaster master;
    private final int number;
    private CatalogVersionSyncCopyContext cvscc;
    private SyncSchedule current;
    private final Runnable meRunningCurrentScheduleLocked;


    public CatalogVersionSyncWorker(CatalogVersionSyncMaster master, String name, int number)
    {
        this.meRunningCurrentScheduleLocked = (Runnable)new Object(this);
        this.master = master;
        this.number = number;
        this.name = name;
    }


    public final int getWorkerNumber()
    {
        return this.number;
    }


    public String toString()
    {
        return this.name;
    }


    protected CatalogVersionSyncCopyContext getCopyContext()
    {
        if(this.cvscc == null)
        {
            this.cvscc = getMaster().getJob().createCopyContext(getMaster().getCronJob(), this);
        }
        return this.cvscc;
    }


    public SyncSchedule getCurrent()
    {
        return this.current;
    }


    public void setCurrent(SyncSchedule current)
    {
        this.current = current;
    }


    public CatalogVersionSyncMaster getMaster()
    {
        return this.master;
    }


    public void run()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("sync worker " + this + " started");
        }
        try
        {
            setCurrent(getMaster().fetchNext(this));
            for(; getCurrent() != null; setCurrent(getMaster().fetchNext(this)))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("sync worker " + this + " fetched " + getCurrent());
                }
                this.master.runExclusiveIfNecessary(getCurrent(), this.meRunningCurrentScheduleLocked);
            }
        }
        catch(InterruptedException interruptedException)
        {
        }
        catch(Exception e)
        {
            LOG.error("error in worker " + this.name + " : " + e.getMessage());
        }
        finally
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("sync worker " + this + " finished");
            }
            getMaster().notifyWorkerDied(this);
        }
    }


    protected void doSynchronization()
    {
        CatalogVersionSyncCopyContext cvscc = getCopyContext();
        SyncSchedule scheduledItem = getCurrent();
        Item src = cvscc.toItem(scheduledItem.getSrcPK());
        Item tgt = cvscc.toItem(scheduledItem.getTgtPK());
        ItemSyncTimestamp syncTS = null;
        try
        {
            syncTS = (ItemSyncTimestamp)cvscc.toItem(scheduledItem.getTimestampPK());
        }
        catch(ClassCastException e)
        {
            PK spk = scheduledItem.getTimestampPK();
            System.err.println("illegal sync timestamp PK '" + spk + " tc:" + spk.getTypeCode() + " item:" + cvscc.toItem(spk));
        }
        Exception error = null;
        try
        {
            if(src == null && tgt != null)
            {
                removeItem(tgt, scheduledItem);
            }
            else if(src == null)
            {
                LOG.info("target has been already removed " + scheduledItem + " - skipping it");
            }
            else
            {
                copyItem(cvscc, src, tgt, syncTS, scheduledItem);
            }
        }
        catch(Exception e)
        {
            error = e;
        }
        finally
        {
            setCurrent(null);
            if(error != null)
            {
                getMaster().notifyError(this, scheduledItem, error);
            }
            else
            {
                getMaster().notifyFinished(this, scheduledItem);
            }
        }
    }


    protected void copyItem(CatalogVersionSyncCopyContext cvscc, Item src, Item tgt, ItemSyncTimestamp syncTS, SyncSchedule schedule)
    {
        try
        {
            enableAndConfigureOptimisticLocking();
            cvscc.copy(src, tgt, syncTS);
            this.master.incrementProcessedItemsCount(schedule.getWeight());
        }
        finally
        {
            HJMPUtils.clearOptimisticLockingSetting();
        }
    }


    private void enableAndConfigureOptimisticLocking()
    {
        HJMPUtils.enableOptimisticLocking();
        HJMPUtils.setBlacklistedTypesForOptimisticLocking(Config.getString("synchronization.hjmp.optimistic.locking.disabled.types", ""));
    }


    protected void removeItem(Item removeItem, SyncSchedule schedule)
    {
        try
        {
            CatalogVersionSyncCopyContext cvscc = getCopyContext();
            enableAndConfigureOptimisticLocking();
            cvscc.delete(removeItem);
            this.master.incrementProcessedItemsCount(schedule.getWeight());
            if(cvscc.isDebugEnabled())
            {
                cvscc.debug("removed " + removeItem.getClass().getName() + " " + removeItem.getPK() + " HJMPOptLOCK = " +
                                HJMPUtils.getVersionForPk(removeItem.getPK()));
            }
        }
        catch(RecoverableSynchronizationPersistenceException e)
        {
            handleRecovery(removeItem, schedule, e);
        }
        catch(Exception e)
        {
            if(CatalogSyncUtils.shouldRetryAfter(e))
            {
                this.cvscc.writeDumpRecord(null, removeItem.getPK(), null, null, null, true);
            }
            else
            {
                throw e;
            }
        }
        finally
        {
            HJMPUtils.clearOptimisticLockingSetting();
        }
    }


    private void handleRecovery(Item itemToRemove, SyncSchedule schedule, RecoverableSynchronizationPersistenceException exception)
    {
        this.cvscc.writeDumpRecord(null, itemToRemove.getPK(), null, null, null, false);
        if(this.cvscc.isInfoEnabled())
        {
            this.cvscc.info("Could not remove item " + itemToRemove.getPK() + " HJMPOptLock = " +
                            HJMPUtils.getVersionForPk(itemToRemove
                                            .getPK()) + " due to " + exception.getLocalizedMessage() + "(" + exception
                            .getClass() + ") - will try again in next turn");
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void processRemovalError(Item itemToRemove, Exception exception)
    {
        throw new UnsupportedOperationException();
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected boolean isItemAlreadyRemovedError(Item itemToRemove, Exception exception)
    {
        throw new UnsupportedOperationException();
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected boolean isRemovalNotPossibleError(Item itemToRemove, Exception exception)
    {
        return exception instanceof de.hybris.platform.jalo.ConsistencyCheckException;
    }
}
