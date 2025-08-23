package de.hybris.platform.jalo;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.PK;

public abstract class JaloItemCacheUnit extends AbstractCacheUnit implements Item.StillInCacheCallback
{
    private final PK pk;
    private volatile boolean removedFromCacheFlag;
    private boolean addedToCache;


    protected JaloItemCacheUnit(Cache cache, PK pk)
    {
        super(cache);
        this.pk = pk;
    }


    public PK getPk()
    {
        return this.pk;
    }


    public Object[] createKey()
    {
        return new Object[] {Cache.CACHEKEY_JALOITEMCACHE, this.pk};
    }


    public abstract Object compute();


    public void invalidate(int invalidationType)
    {
        setCacheBoundFalse();
        super.invalidate(invalidationType);
    }


    public void setCacheBoundFalse()
    {
        Object object = getWithoutComputeIgnoringTransaction();
        if(object instanceof Item)
        {
            ((Item)object).setCacheBound(false);
        }
    }


    public void removedFromCache()
    {
        super.removedFromCache();
        this.removedFromCacheFlag = true;
        setCacheBoundFalse();
    }


    public void addedToCacheBeforeComputation()
    {
        super.addedToCacheBeforeComputation();
        this.addedToCache = true;
    }


    public Object getCached()
    {
        try
        {
            this.addedToCache = false;
            this.removedFromCacheFlag = false;
            Object ret = get();
            if(ret instanceof Item && this.addedToCache)
            {
                Item item = (Item)ret;
                item.setStillInCacheCallback(this);
                item.setCacheBound(!this.removedFromCacheFlag);
            }
            return ret;
        }
        catch(Exception e)
        {
            if(e instanceof RuntimeException)
            {
                throw (RuntimeException)e;
            }
            throw new JaloSystemException(e);
        }
    }


    public boolean isStillInCache()
    {
        return !this.removedFromCacheFlag;
    }
}
