package de.hybris.platform.spring;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.util.SingletonCreator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.util.Assert;

public class HybrisRequestScope implements Scope, InitializingBean, DisposableBean
{
    private static final Logger LOG = Logger.getLogger(HybrisRequestScope.class);
    private static final SingletonCreator.Creator<ScopeMap> CREATOR = (SingletonCreator.Creator<ScopeMap>)new RequestCacheMapCreator();


    protected ScopeMap getCacheMap()
    {
        try
        {
            return (ScopeMap)Registry.getCurrentTenantNoFallback().getCache().getRequestCacheContent(CREATOR);
        }
        catch(NullPointerException e)
        {
            throw new IllegalStateException("no tenant active - cannot get scope map");
        }
    }


    protected ScopeMap getCacheMapIfExists()
    {
        try
        {
            return (ScopeMap)Registry.getCurrentTenantNoFallback().getCache().getRequestCacheContentIfExists(CREATOR);
        }
        catch(NullPointerException e)
        {
            throw new IllegalStateException("no tenant active - cannot get scope map");
        }
    }


    public void afterPropertiesSet() throws Exception
    {
        Registry.registerTenantListener((TenantListener)new Object(this));
    }


    public Object get(String name, ObjectFactory<?> objectFactory)
    {
        return getCacheMap().get(name, objectFactory);
    }


    public String getConversationId()
    {
        return Long.toString(Thread.currentThread().getId());
    }


    public void registerDestructionCallback(String name, Runnable callback)
    {
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(callback, "Callback must not be null");
        getCacheMap().addDestructionCallback(name, callback);
    }


    public Object remove(String name)
    {
        return getCacheMap().remove(name);
    }


    public void destroy() throws Exception
    {
    }


    public Object resolveContextualObject(String arg0)
    {
        return null;
    }
}
