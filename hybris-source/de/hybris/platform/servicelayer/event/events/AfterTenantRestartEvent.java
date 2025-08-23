package de.hybris.platform.servicelayer.event.events;

import java.io.Serializable;

public class AfterTenantRestartEvent extends AbstractEvent
{
    private String tenantId;


    public AfterTenantRestartEvent()
    {
    }


    public AfterTenantRestartEvent(Serializable source)
    {
        super(source);
    }


    public void setTenantId(String tenantId)
    {
        this.tenantId = tenantId;
    }


    public String getTenantId()
    {
        return this.tenantId;
    }
}
