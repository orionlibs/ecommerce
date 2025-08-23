package de.hybris.platform.util.collections;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.log4j.Logger;

public class YFastMap<K, V> extends AbstractDelegatingMap<K, V> implements YMap<K, V>
{
    private static final Logger LOG = Logger.getLogger(YFastMap.class);
    private int maxReachedSize = 0;
    private volatile transient ReadWriteLock readWriteLock;
    private static final long serialVersionUID = 5369123968374479235L;


    public YFastMap()
    {
    }


    public YFastMap(int initialCapacity)
    {
        this();
    }


    protected Map<K, V> createMapInstance()
    {
        return new LinkedHashMap<>();
    }


    public YFastMap(Map<? extends K, ? extends V> map)
    {
        this();
        putAll(map);
    }


    public void clear()
    {
        clear(null);
    }


    public void clear(YMap.ClearHandler<K, V> handler)
    {
        Lock l = lockForWriting();
        try
        {
            synchronized(this)
            {
                if(handler != null)
                {
                    for(Map.Entry<K, V> entry : (Iterable<Map.Entry<K, V>>)entrySet())
                    {
                        try
                        {
                            handler.handleClearedEntry(entry);
                        }
                        catch(Exception e)
                        {
                            LOG.error("error processing cleared cache entry : " + e.getMessage(), e);
                        }
                    }
                }
                super.clear();
            }
        }
        finally
        {
            unlock(l);
        }
    }


    public Map.Entry<K, V> getEntry(Object key)
    {
        return getEntry(key, false);
    }


    public Map.Entry<K, V> getEntry(Object key, boolean lock)
    {
        Lock l = null;
        if(lock)
        {
            l = lockForReading();
        }
        try
        {
            if(!containsKey(key))
            {
                return null;
            }
            MyEntry myEntry = new MyEntry(key, get(key));
            if(!lock && myEntry.getValue() == null)
            {
                return getEntry(key, true);
            }
            return (Map.Entry<K, V>)myEntry;
        }
        finally
        {
            if(lock)
            {
                unlock(l);
            }
        }
    }


    public boolean containsKey(Object key)
    {
        Lock l = lockForReading();
        try
        {
            return super.containsKey(key);
        }
        finally
        {
            unlock(l);
        }
    }


    public V put(K key, V value)
    {
        Lock l = lockForWriting();
        try
        {
        }
        finally
        {
            unlock(l);
        }
    }


    public void putAll(Map<? extends K, ? extends V> t)
    {
        Lock l = lockForWriting();
        try
        {
            synchronized(this)
            {
                super.putAll(t);
                this.maxReachedSize = Math.max(size(), this.maxReachedSize);
            }
        }
        finally
        {
            unlock(l);
        }
    }


    public V remove(Object key)
    {
        Lock l = lockForWriting();
        try
        {
        }
        finally
        {
            unlock(l);
        }
    }


    public int getMaxReachedSize()
    {
        return this.maxReachedSize;
    }


    private void unlock(Lock l)
    {
        l.unlock();
    }


    private Lock lockForWriting()
    {
        Lock l = getReadWriteLock().writeLock();
        l.lock();
        return l;
    }


    private Lock lockForReading()
    {
        Lock l = getReadWriteLock().readLock();
        l.lock();
        return l;
    }


    private ReadWriteLock getReadWriteLock()
    {
        if(this.readWriteLock == null)
        {
            synchronized(this)
            {
                if(this.readWriteLock == null)
                {
                    this.readWriteLock = new ReentrantReadWriteLock();
                }
            }
        }
        return this.readWriteLock;
    }
}
