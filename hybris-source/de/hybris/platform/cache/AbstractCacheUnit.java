package de.hybris.platform.cache;

import de.hybris.platform.cache.impl.DefaultCache;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.legacy.LegacyCacheKey;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Utilities;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public abstract class AbstractCacheUnit implements CacheValueLoader
{
    public static final int INVALIDATIONTYPE_MODIFIED = 1;
    public static final int INVALIDATIONTYPE_REMOVED = 2;
    public static final int INVALIDATIONTYPE_CREATED = 4;
    public static final String INVALIDATIONTYPE_MODIFIED_STRING = String.valueOf(1).intern();
    public static final String INVALIDATIONTYPE_REMOVED_STRING = String.valueOf(2).intern();
    public static Object UNKNOWN = "UNKNOWN";
    private final String tenantID;
    private final Cache cache;
    private volatile Object[] theKey;
    protected volatile CacheKey cacheKey;
    private int cachedHash = 0;
    private volatile Object value = UNKNOWN;


    public AbstractCacheUnit(Cache cache, Tenant tenant)
    {
        this(cache, tenant.getTenantID());
    }


    public AbstractCacheUnit(Cache cache, String tenantId)
    {
        if(tenantId == null)
        {
            throw new IllegalArgumentException("Tennant must be not null");
        }
        if(!tenantId.equals(cache.getTenantId()))
        {
            throw new IllegalArgumentException("Given tenantid <<" + tenantId + ">> must match the cache's tenant <<" + cache
                            .getTenantId() + ">>");
        }
        this.tenantID = tenantId;
        this.cache = cache;
    }


    public AbstractCacheUnit(Cache cache)
    {
        this(cache, cache.getTenant().getTenantID());
    }


    public abstract Object[] createKey();


    public CacheKey getKey()
    {
        if(this.cacheKey == null)
        {
            this.cacheKey = (CacheKey)new LegacyCacheKey(getKeyAsArray(), this.tenantID);
        }
        return this.cacheKey;
    }


    public CacheValueLoader<? extends AbstractCacheUnit> getLoader()
    {
        return this;
    }


    public Object load(CacheKey key) throws CacheValueLoadException
    {
        try
        {
            addedToCacheBeforeComputation();
            return this;
        }
        catch(RuntimeException jaloError)
        {
            throw jaloError;
        }
        catch(Exception ex)
        {
            throw new CacheValueLoadException("Unable to load value", ex);
        }
    }


    public abstract Object compute() throws Exception;


    protected Cache getCache()
    {
        return this.cache;
    }


    public boolean isValueKnown()
    {
        return (this.value != UNKNOWN);
    }


    private final boolean isValueUnknown(Object anyValue)
    {
        return (anyValue == UNKNOWN);
    }


    public boolean isCachingSupported()
    {
        return true;
    }


    public String getTenantID()
    {
        return this.tenantID;
    }


    public final Object[] getKeyAsArray()
    {
        Object[] currentKey = this.theKey;
        if(currentKey == null)
        {
            currentKey = createKey();
            this.theKey = currentKey;
        }
        return currentKey;
    }


    protected boolean useCache()
    {
        return Transaction.current().useCache(this);
    }


    public final Object get() throws Exception
    {
        if(!useCache())
        {
            return computeUncached();
        }
        TransactionAwareCache transactionAwareCache = getTransactionAwareCache();
        AbstractCacheUnit valueUnit = this;
        boolean success = false;
        valueUnit = transactionAwareCache.getOrAddUnit(this);
        Object ret = valueUnit.value;
        try
        {
            ret = isValueUnknown(ret) ? valueUnit.privateGet() : ret;
            success = true;
        }
        catch(CacheValueLoadException sqlEx)
        {
            if(sqlEx.getCause() instanceof Exception)
            {
                throw (Exception)sqlEx.getCause();
            }
            throw sqlEx;
        }
        finally
        {
            if(!success)
            {
                transactionAwareCache.removeUnit(valueUnit);
            }
        }
        return ret;
    }


    private TransactionAwareCache getTransactionAwareCache()
    {
        return Transaction.current().getTransactionAwareCache(getCache());
    }


    private final Object computeUncached() throws Exception
    {
        if(getTransactionAwareCache().isForceExclusiveComputation(this))
        {
            synchronized(this)
            {
                return compute();
            }
        }
        return compute();
    }


    public Object getWithoutCompute()
    {
        if(!useCache())
        {
            return null;
        }
        AbstractCacheUnit unitFromCache = getTransactionAwareCache().getUnit(this);
        if(unitFromCache == null)
        {
            return null;
        }
        Object ret = unitFromCache.value;
        if(isValueUnknown(ret))
        {
            return null;
        }
        return ret;
    }


    public Object getWithoutComputeIgnoringTransaction()
    {
        Object ret = this.value;
        return (ret == UNKNOWN) ? null : ret;
    }


    public int getInvalidationTopicDepth()
    {
        throw new RuntimeException("cannot externally invalidate AbstractCacheUnit that does not have an implementation of getInvalidationTopicDepth");
    }


    protected void hintValue(Object newValue)
    {
        this.value = newValue;
        getTransactionAwareCache().getOrAddUnit(this);
    }


    private final Object privateGet() throws Exception
    {
        return getTransactionAwareCache().isForceExclusiveComputation(this) ? privateGetSynchronized() : privateGetNoLock();
    }


    private final Object privateGetSynchronized() throws Exception
    {
        Object ret = this.value;
        if(isValueUnknown(ret))
        {
            synchronized(this)
            {
                ret = this.value;
                if(isValueUnknown(ret))
                {
                    ret = compute();
                    this.value = ret;
                }
            }
        }
        return ret;
    }


    private final Object privateGetNoLock() throws Exception
    {
        Object ret = this.value;
        if(isValueUnknown(ret))
        {
            ret = compute();
            this.value = ret;
        }
        return ret;
    }


    public void invalidate(int invalidationType)
    {
        getTransactionAwareCache().invalidate(this, invalidationType);
    }


    public void removedFromCache()
    {
    }


    public void addedToCacheBeforeComputation()
    {
    }


    public void executeInvalidation()
    {
        this.value = UNKNOWN;
    }


    public final int hashCode()
    {
        int ret = this.cachedHash;
        if(ret == 0)
        {
            ret = this.tenantID.hashCode() ^ Arrays.deepHashCode(getKeyAsArray());
            this.cachedHash = ret;
        }
        return ret;
    }


    public final boolean equals(Object object)
    {
        if(this == object)
        {
            return true;
        }
        if(object == null)
        {
            return false;
        }
        if(getClass() != object.getClass())
        {
            return false;
        }
        return (this.tenantID.equals(((AbstractCacheUnit)object).tenantID) &&
                        Utilities.arrayDeepEqualsReverse(getKeyAsArray(), ((AbstractCacheUnit)object).getKeyAsArray()));
    }


    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(isValueKnown() ? "value" : "no value").append("[");
        Object[] key = getKeyAsArray();
        if(DefaultCache.isMultiPathKey(key))
        {
            for(int i = 0; i < key.length; i++)
            {
                stringBuilder.append("[").append(this.tenantID).append(":");
                Object[] singleKey = (Object[])key[i];
                for(int j = 0; j < singleKey.length; j++)
                {
                    Object object = singleKey[j];
                    stringBuilder.append(object);
                    if(j + 1 < singleKey.length)
                    {
                        stringBuilder.append(",");
                    }
                }
                stringBuilder.append((i + 1 < key.length) ? "]," : "]");
            }
        }
        else
        {
            for(int i = 0; i < key.length; i++)
            {
                Object object = key[i];
                stringBuilder.append(object);
                if(i + 1 < key.length)
                {
                    stringBuilder.append(",");
                }
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }


    private void writeObject(ObjectOutputStream out) throws IOException
    {
        throw new IOException("unable to serialize an AbstractCacheUnit!");
    }
}
