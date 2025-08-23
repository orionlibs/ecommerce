package de.hybris.platform.cache.impl;

import de.hybris.platform.util.SingletonCreator;

public class RequestCache
{
    private final ThreadLocal<SingletonCreator> requestCacheTL = new ThreadLocal<>();
    private final SingletonCreator staticCache = new SingletonCreator();


    public <T> T getRequestCacheContent(SingletonCreator.Creator<T> creator)
    {
        SingletonCreator singletonCreator = this.requestCacheTL.get();
        if(singletonCreator == null)
        {
            singletonCreator = new SingletonCreator();
            this.requestCacheTL.set(singletonCreator);
        }
        return (T)singletonCreator.getSingleton(creator);
    }


    public void clearRequestCache()
    {
        SingletonCreator singletonCreator = this.requestCacheTL.get();
        if(singletonCreator != null)
        {
            singletonCreator.destroy();
        }
        this.requestCacheTL.remove();
    }


    public <T> T getStaticCacheContent(SingletonCreator.Creator<T> creator)
    {
        return (T)this.staticCache.getSingleton(creator);
    }


    public void clear()
    {
        this.staticCache.clear();
    }
}
