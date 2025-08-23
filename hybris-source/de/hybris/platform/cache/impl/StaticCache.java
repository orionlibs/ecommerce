package de.hybris.platform.cache.impl;

import de.hybris.platform.util.SingletonCreator;
import java.util.Map;

public class StaticCache
{
    private final SingletonCreator staticCacheCreator = new SingletonCreator();
    private final ThreadLocal<SingletonCreator> requestCacheTL = new ThreadLocal<>();
    private static final SingletonCreator.Creator<Map> REQUESTCACHEMAPCREATOR = (SingletonCreator.Creator<Map>)new RequestCacheMapCreator();


    public void clearStaticCache()
    {
        this.staticCacheCreator.clear();
    }


    public Map getRequestCacheMap()
    {
        return getRequestCacheContent(REQUESTCACHEMAPCREATOR);
    }


    public <T> T getStaticCacheContent(SingletonCreator.Creator<T> creator)
    {
        return (T)this.staticCacheCreator.getSingleton(creator);
    }


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


    public <T> T getRequestCacheContentIfExists(SingletonCreator.Creator<T> creator)
    {
        SingletonCreator singletonCreator = this.requestCacheTL.get();
        if(singletonCreator == null)
        {
            return null;
        }
        return (T)singletonCreator.getSingletonIfExists(creator);
    }


    public void clearRequestCache()
    {
        SingletonCreator singletonCreator = this.requestCacheTL.get();
        if(singletonCreator != null)
        {
            singletonCreator.destroy();
        }
        this.requestCacheTL.set(null);
    }
}
