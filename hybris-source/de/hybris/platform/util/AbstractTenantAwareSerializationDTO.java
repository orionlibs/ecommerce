package de.hybris.platform.util;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import java.io.ObjectStreamException;
import java.io.Serializable;

public abstract class AbstractTenantAwareSerializationDTO implements Serializable
{
    private final String tenantID;


    protected AbstractTenantAwareSerializationDTO(Tenant tenant)
    {
        this(tenant.getTenantID());
    }


    protected AbstractTenantAwareSerializationDTO(String tenantID)
    {
        this.tenantID = tenantID;
    }


    protected String getTenantID()
    {
        return this.tenantID;
    }


    protected Tenant getTenant()
    {
        return Registry.getTenantByID(this.tenantID);
    }


    public abstract Object resolveObject() throws ObjectStreamException;


    public final Object readResolve() throws ObjectStreamException
    {
        if(this.tenantID == null)
        {
            throw new IllegalStateException("Stream DTO " + this + " hasn't any tenant!");
        }
        Tenant t = getTenant();
        boolean mustSwitchTenant = !Registry.isCurrentTenant(t);
        Tenant backup = null;
        try
        {
            if(mustSwitchTenant)
            {
                backup = Registry.getCurrentTenantNoFallback();
                Registry.setCurrentTenant(t);
            }
            return resolveObject();
        }
        finally
        {
            if(mustSwitchTenant)
            {
                if(backup == null)
                {
                    Registry.unsetCurrentTenant();
                }
                else
                {
                    Registry.setCurrentTenant(backup);
                }
            }
        }
    }


    public String toString()
    {
        return super.toString() + " tenantID:" + super.toString();
    }
}
