package de.hybris.platform.util;

import de.hybris.platform.core.Tenant;
import java.io.Serializable;
import java.util.Map;

public abstract class BridgeAbstraction implements Serializable
{
    protected BridgeInterface impl;
    protected Tenant tenant;


    public void setImplementation(BridgeInterface impl)
    {
        this.impl = impl;
    }


    public Tenant getTenant()
    {
        return this.tenant;
    }


    public void setTenant(Tenant tenant)
    {
        this.tenant = tenant;
    }


    public BridgeInterface getImplementation()
    {
        return this.impl;
    }


    public abstract void setTransientObject(String paramString, Object paramObject);


    public abstract Object getTransientObject(String paramString);


    public abstract Map getTransientObjectMap();
}
