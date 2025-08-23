package de.hybris.platform.regioncache.region.impl;

import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import java.util.Collection;
import java.util.Map;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;
import net.sf.ehcache.loader.CacheLoader;
import org.apache.log4j.Logger;

class EHCacheRegionLoader implements CacheLoader
{
    private static final Logger LOG = Logger.getLogger(EHCacheRegionLoader.class);
    private final CacheValueLoader loader;
    private boolean loaded = false;
    private Object value = null;


    public EHCacheRegionLoader(CacheValueLoader loader)
    {
        this.loader = loader;
    }


    public Object load(Object key) throws CacheException
    {
        if(key == null)
        {
            return null;
        }
        try
        {
            this.value = this.loader.load((CacheKey)key);
            setLoaded(true);
        }
        catch(CacheException ce)
        {
            LOG.warn("Unable to load key " + key + " error " + ce.getMessage());
            throw ce;
        }
        catch(RuntimeException re)
        {
            LOG.warn("Unable to load key " + key + " error " + re.getMessage());
            throw re;
        }
        return this.value;
    }


    public Map loadAll(Collection keys)
    {
        LOG.warn("EHCacheRegionLoader unsupported method loadAll(keys)");
        throw new UnsupportedOperationException();
    }


    public Object load(Object key, Object argument)
    {
        return load(key);
    }


    public Map loadAll(Collection keys, Object argument)
    {
        LOG.warn("EHCacheRegionLoader unsupported method loadAll(keys, argument)");
        throw new UnsupportedOperationException();
    }


    public String getName()
    {
        return getClass().getName();
    }


    public CacheLoader clone(Ehcache cache) throws CloneNotSupportedException
    {
        LOG.warn("EHCacheRegionLoader unsupported method clone()");
        throw new UnsupportedOperationException();
    }


    public void init()
    {
    }


    public void dispose() throws CacheException
    {
    }


    public Status getStatus()
    {
        return Status.STATUS_ALIVE;
    }


    public boolean loadPerformed()
    {
        return this.loaded;
    }


    public void setLoaded(boolean loaded)
    {
        this.loaded = loaded;
    }


    public Object getLoadedValue()
    {
        return this.value;
    }
}
