package de.hybris.platform.core;

import com.google.common.base.Preconditions;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.support.GenericApplicationContext;

class HybrisContextHolder
{
    volatile GenericApplicationContext globalContextRef;
    private final Set<String> createdContextsForTenants = new HashSet<>();


    static final HybrisContextHolder getInstance()
    {
        return InstanceHolder.instance;
    }


    final GenericApplicationContext getApplicationInstance(Tenant tenant) throws BeansException
    {
        try
        {
            Preconditions.checkArgument(!this.createdContextsForTenants.contains(tenant.getTenantID()), "There is already registered context for tenant " + tenant);
            GenericApplicationContext result = getAppCtxFactory(tenant).build(new String[0]);
            this.createdContextsForTenants.add(tenant.getTenantID());
            return result;
        }
        catch(BeansException ace)
        {
            destroy();
            throw ace;
        }
        catch(Exception ee)
        {
            throw new ApplicationContextException("Error while instantiating application context for tenant " + tenant, ee);
        }
    }


    final GenericApplicationContext getGlobalInstance() throws BeansException
    {
        try
        {
            return getGlobalInstanceCached();
        }
        catch(BeansException ace)
        {
            throw ace;
        }
        catch(Exception ee)
        {
            throw new ApplicationContextException("Error while instantiating global context for tenant ", ee);
        }
    }


    GenericApplicationContext getGlobalInstanceCached() throws FatalBeanException
    {
        if(this.globalContextRef == null)
        {
            synchronized(this)
            {
                if(this.globalContextRef == null)
                {
                    this.globalContextRef = getGlobalCtxFactory().build(new String[0]);
                }
            }
        }
        return this.globalContextRef;
    }


    final void destroy()
    {
        this.createdContextsForTenants.clear();
        if(this.globalContextRef != null)
        {
            this.globalContextRef.close();
            this.globalContextRef = null;
        }
    }


    HybrisContextFactory getGlobalCtxFactory()
    {
        return (HybrisContextFactory)new HybrisContextFactory.GlobalContextFactory();
    }


    HybrisContextFactory getAppCtxFactory(Tenant tenant) throws BeansException
    {
        return (HybrisContextFactory)new HybrisContextFactory.ApplicationContextFactory(tenant.getTenantID(), tenant.getConfig(), getGlobalInstance());
    }
}
