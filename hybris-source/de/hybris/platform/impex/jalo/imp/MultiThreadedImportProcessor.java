package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.core.Constants;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.threadpool.PoolableThread;
import java.util.Collection;
import java.util.Map;

public class MultiThreadedImportProcessor extends DefaultImportProcessor
{
    private volatile QueryParameters[] workerQueries;
    private volatile long[][] workerItems;


    public void setMaxThreads(int max)
    {
        this.workerItems = new long[max][];
        this.workerQueries = new QueryParameters[max];
    }


    protected ImpExWorker getWorker()
    {
        Thread thread = Thread.currentThread();
        if(thread instanceof PoolableThread)
        {
            return (ImpExWorker)((PoolableThread)thread).getRunnable();
        }
        return null;
    }


    protected void adjustSessionSettings()
    {
        super.adjustSessionSettings();
        if(((MultiThreadedImpExImportReader)getReader()).isInParallelMode())
        {
            SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
            ctx.setAttribute("all.attributes.use.ta", Boolean.FALSE);
            ctx.setAttribute(Constants.DISABLE_CYCLIC_CHECKS, Boolean.TRUE);
        }
    }


    protected SessionContext getCreationContext(ComposedType targetType, Map<StandardColumnDescriptor, Object> attributeValueMappings, ValueLine valueLine)
    {
        SessionContext ret = super.getCreationContext(targetType, attributeValueMappings, valueLine);
        if(((MultiThreadedImpExImportReader)getReader()).isInParallelMode())
        {
            ret.setAttribute("transaction_in_create_disabled", Boolean.TRUE);
        }
        return ret;
    }


    public Item processItemData(ValueLine valueLine) throws ImpExException
    {
        try
        {
            return super.processItemData(valueLine);
        }
        finally
        {
            releaseWorkerLocks(getWorker());
        }
    }


    protected void lockQuery(ImpExWorker worker, QueryParameters queryParameters)
    {
        if(((MultiThreadedImpExImportReader)getReader()).isInParallelMode())
        {
            if(this.workerQueries == null)
            {
                throw new IllegalArgumentException("max threads not yet set");
            }
            int number = worker.getNumber();
            synchronized(this.workerQueries)
            {
                while(!tryToLock(queryParameters, number))
                {
                    try
                    {
                        this.workerQueries.wait();
                    }
                    catch(InterruptedException interruptedException)
                    {
                    }
                }
            }
        }
    }


    protected boolean lockItems(ImpExWorker worker, Collection<Item> matches)
    {
        if(((MultiThreadedImpExImportReader)getReader()).isInParallelMode())
        {
            if(this.workerItems == null)
            {
                throw new IllegalArgumentException("max threads not yet set");
            }
            boolean ret = false;
            int number = worker.getNumber();
            long[] pks = toLongArray(matches);
            synchronized(this.workerItems)
            {
                while(!tryToLock(pks, number))
                {
                    ret = true;
                    try
                    {
                        this.workerItems.wait();
                    }
                    catch(InterruptedException interruptedException)
                    {
                    }
                }
            }
            return ret;
        }
        return false;
    }


    private final void releaseWorkerLocks(ImpExWorker worker)
    {
        if(((MultiThreadedImpExImportReader)getReader()).isInParallelMode())
        {
            if(worker != null)
            {
                if(this.workerItems == null || this.workerQueries == null)
                {
                    throw new IllegalArgumentException("max threads not yet set");
                }
                int number = worker.getNumber();
                synchronized(this.workerItems)
                {
                    this.workerItems[number] = null;
                    this.workerItems.notifyAll();
                }
                synchronized(this.workerQueries)
                {
                    this.workerQueries[number] = null;
                    this.workerQueries.notifyAll();
                }
            }
        }
    }


    private final boolean tryToLock(QueryParameters queryParameters, int workerPosition)
    {
        int workerQueriesCount = this.workerQueries.length;
        for(int i = 0; i < workerQueriesCount; i++)
        {
            if(i != workerPosition)
            {
                QueryParameters present = this.workerQueries[i];
                if(present != null && present.equals(queryParameters))
                {
                    return false;
                }
            }
        }
        this.workerQueries[workerPosition] = queryParameters;
        return true;
    }


    private final boolean tryToLock(long[] matches, int workerPosition)
    {
        if(matches != null)
        {
            int workerItemsCount = this.workerItems.length;
            for(int i = 0; i < workerItemsCount; i++)
            {
                if(i != workerPosition)
                {
                    long[] present = this.workerItems[i];
                    if(present != null)
                    {
                        for(long pk : matches)
                        {
                            if(contains(present, pk))
                            {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        this.workerItems[workerPosition] = matches;
        return true;
    }


    private final boolean contains(long[] pks, long pk)
    {
        int pkCount = pks.length;
        for(int i = 0, j = pkCount - 1; i <= j; i++, j--)
        {
            if(pks[i] == pk || (i <= j && pks[j] == pk))
            {
                return true;
            }
        }
        return false;
    }


    private final long[] toLongArray(Collection<Item> items)
    {
        if(items == null || items.isEmpty())
        {
            return null;
        }
        long[] ret = new long[items.size()];
        int index = 0;
        for(Item i : items)
        {
            ret[index] = i.getPK().getLongValue();
            index++;
        }
        return ret;
    }


    protected ExistingItemResolver createExistingItemsResolver(HeaderDescriptor header, boolean useCache) throws HeaderValidationException
    {
        return (ExistingItemResolver)new SynchronizedExistingItemResolver(this);
    }
}
