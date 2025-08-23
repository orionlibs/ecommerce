package de.hybris.platform.persistence.audit.internal;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import java.util.concurrent.ExecutionException;

public class AuditPayloadClassCache
{
    private static final LoadingCache<String, Class> keyToClassCache = initCache();


    public static Class get(String key)
    {
        try
        {
            return (Class)keyToClassCache.get(key);
        }
        catch(ExecutionException e)
        {
            throw new SystemException(e);
        }
    }


    private static LoadingCache<String, Class> initCache()
    {
        return CacheBuilder.newBuilder().build((CacheLoader)new Object());
    }
}
