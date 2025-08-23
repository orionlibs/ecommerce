package de.hybris.platform.util.threadpool;

import org.apache.commons.pool.BasePoolableObjectFactory;

public class PoolableThreadFactory extends BasePoolableObjectFactory
{
    private final ThreadPool pool;


    public PoolableThreadFactory(ThreadPool pool)
    {
        this.pool = pool;
    }


    public final Object makeObject() throws Exception
    {
        return new PoolableThread(this.pool);
    }


    public void destroyObject(Object obj) throws Exception
    {
        ((PoolableThread)obj).invalidate();
    }


    public boolean validateObject(Object obj)
    {
        return ((PoolableThread)obj).isValid();
    }
}
