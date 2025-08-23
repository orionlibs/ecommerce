package de.hybris.platform.core;

import java.io.ObjectStreamException;
import java.io.Serializable;

public class TenantDTO implements Serializable
{
    private final String tenantID;


    public TenantDTO(String tenantID)
    {
        if(tenantID == null)
        {
            throw new NullPointerException("tenant ID was null");
        }
        this.tenantID = tenantID;
    }


    public Object readResolve() throws ObjectStreamException
    {
        Tenant ret = Registry.getTenantByID(getTenantID());
        if(ret == null)
        {
            throw new NullPointerException("cannot deserialize tenant '" + this.tenantID + "' - cannot find tenant");
        }
        return ret;
    }


    public String getTenantID()
    {
        return this.tenantID;
    }
}
