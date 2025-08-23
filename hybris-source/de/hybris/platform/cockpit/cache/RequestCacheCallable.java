package de.hybris.platform.cockpit.cache;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.spring.SpringUtil;

public abstract class RequestCacheCallable<K, V>
{
    private final String cacheName;


    protected abstract V call();


    public RequestCacheCallable(String cacheName)
    {
        this.cacheName = cacheName;
    }


    public V get(K key)
    {
        RequestCache<K, V> requestCache = getRequestCache();
        if(requestCache == null)
        {
            return call();
        }
        if(requestCache.containsKey(key))
        {
            return (V)requestCache.get(key);
        }
        V value = call();
        requestCache.put(key, value);
        return value;
    }


    private RequestCache getRequestCache()
    {
        Execution execution = Executions.getCurrent();
        if(execution == null)
        {
            return null;
        }
        RequestCache requestCache = (RequestCache)execution.getAttribute(this.cacheName);
        if(requestCache == null)
        {
            requestCache = getRequestCacheFromContext();
            execution.setAttribute(this.cacheName, requestCache);
        }
        return requestCache;
    }


    public void updateCache(K key, V value)
    {
        getRequestCache().put(key, value);
    }


    private RequestCache getRequestCacheFromContext()
    {
        return (RequestCache)SpringUtil.getBean(this.cacheName, RequestCache.class);
    }
}
