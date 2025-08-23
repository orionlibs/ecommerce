package de.hybris.platform.util.threadpool;

import de.hybris.platform.core.Registry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class ThreadPool extends GenericObjectPool implements AutoCloseable
{
    private static final Logger LOG = Logger.getLogger(ThreadPool.class.getName());
    public static final int DEFAULT_MAX_THREADS = 50;
    private final Set<PoolableThread> physicalThreads = Collections.synchronizedSet(new HashSet<>());
    private volatile boolean isClosed = false;
    private final String tenantID;
    private AtomicLong counter = new AtomicLong(0L);


    public static ThreadPool getInstance()
    {
        return Registry.getCurrentTenant().getThreadPool();
    }


    public ThreadPool(String tenantID, int poolsize)
    {
        this.tenantID = tenantID;
        setFactory((PoolableObjectFactory)new PoolableThreadFactory(this));
        if(LOG.isDebugEnabled())
        {
            LOG.debug("New ThreadPool initialized with max. " + poolsize + " threads.");
        }
    }


    String getTenantID()
    {
        return this.tenantID;
    }


    long inc()
    {
        return this.counter.incrementAndGet();
    }


    void setTenant()
    {
        if(this.tenantID != null)
        {
            if(!this.tenantID.equals("master"))
            {
                Registry.setCurrentTenantByID(this.tenantID);
            }
            else
            {
                Registry.activateMasterTenantAndFailIfAlreadySet();
            }
        }
    }


    void unsetTenant()
    {
        if(this.tenantID != null)
        {
            Registry.unsetCurrentTenant();
        }
    }


    void registerForCleanup(PoolableThread pt)
    {
        this.physicalThreads.add(pt);
    }


    public final Object borrowObject()
    {
        if(this.isClosed)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("borrowThread called, but threadpool was already closed.");
            }
            throw new RuntimeException("Pool already closed. Cannot get a new thread.");
        }
        try
        {
            return super.borrowObject();
        }
        catch(Exception e)
        {
            if(e instanceof RuntimeException)
            {
                throw (RuntimeException)e;
            }
            throw new RuntimeException(e);
        }
    }


    public final void returnObject(Object thread) throws Exception
    {
        if(this.isClosed)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("returnThread called, but threadpool was already closed.");
            }
        }
        try
        {
            super.returnObject(thread);
        }
        catch(Exception e)
        {
            if(e instanceof RuntimeException)
            {
                throw (RuntimeException)e;
            }
            throw new RuntimeException(e);
        }
    }


    public final void returnThread(PoolableThread thread) throws Exception
    {
        thread.setBorrowed(false);
        returnObject(thread);
    }


    public final PoolableThread borrowThread()
    {
        PoolableThread ret = (PoolableThread)borrowObject();
        ret.setBorrowed(true);
        return ret;
    }


    public void close()
    {
        destroy();
    }


    private final void destroy()
    {
        if(this.isClosed)
        {
            return;
        }
        this.isClosed = true;
        try
        {
            Thread.sleep(100L);
            boolean stillThreadsActive = false;
            for(PoolableThread t : new HashSet(this.physicalThreads))
            {
                if(t.isAlive())
                {
                    try
                    {
                        stillThreadsActive = true;
                        t.invalidate();
                        t.interrupt();
                    }
                    catch(Exception exception)
                    {
                    }
                }
            }
            if(stillThreadsActive)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("still threads running, trying to interrupt them smoothly.");
                }
                long start = System.currentTimeMillis();
                while(stillThreadsActive && System.currentTimeMillis() - start < 2000L)
                {
                    stillThreadsActive = false;
                    for(PoolableThread t : new HashSet(this.physicalThreads))
                    {
                        if(t.isAlive())
                        {
                            stillThreadsActive = true;
                        }
                    }
                    Thread.sleep(100L);
                }
                if(stillThreadsActive)
                {
                    if(LOG.isEnabledFor((Priority)Level.WARN))
                    {
                        LOG.warn("not all threads closed smoothly after 1s, good bye!");
                    }
                }
            }
        }
        catch(Exception e)
        {
            if(e instanceof RuntimeException)
            {
                throw (RuntimeException)e;
            }
            throw new RuntimeException(e);
        }
        finally
        {
            try
            {
                super.close();
            }
            catch(Exception e)
            {
                if(e instanceof RuntimeException)
                {
                    throw (RuntimeException)e;
                }
                throw new RuntimeException(e);
            }
        }
    }
}
