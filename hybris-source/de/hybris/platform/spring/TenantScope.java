package de.hybris.platform.spring;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

@Deprecated(since = "5.0", forRemoval = true)
public class TenantScope implements Scope
{
    private Tenant tenant;


    @PostConstruct
    public void init()
    {
        Tenant ctxTenant = Registry.getCurrentTenantNoFallback();
        if(ctxTenant == null)
        {
            throw new IllegalStateException("no tenant active");
        }
        this.tenant = ctxTenant;
    }


    public Object get(String name, ObjectFactory objectFactory)
    {
        throw new UnsupportedOperationException();
    }


    public String getConversationId()
    {
        throw new UnsupportedOperationException();
    }


    public void registerDestructionCallback(String theName, Runnable callback)
    {
        throw new UnsupportedOperationException();
    }


    public Object resolveContextualObject(String key)
    {
        throw new UnsupportedOperationException();
    }


    public Object remove(String theName)
    {
        throw new UnsupportedOperationException();
    }


    public Tenant getCurrentTenant()
    {
        return this.tenant;
    }
}
