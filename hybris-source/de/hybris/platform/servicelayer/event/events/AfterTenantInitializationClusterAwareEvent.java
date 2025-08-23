package de.hybris.platform.servicelayer.event.events;

import de.hybris.platform.core.Tenant;
import de.hybris.platform.servicelayer.event.ClusterAwareEvent;

public class AfterTenantInitializationClusterAwareEvent extends AbstractEvent implements ClusterAwareEvent
{
    private final String tenantId;


    public boolean publish(int sourceNodeId, int targetNodeId)
    {
        return true;
    }


    public String getTenantId()
    {
        return this.tenantId;
    }


    public AfterTenantInitializationClusterAwareEvent(Tenant tenant)
    {
        this.tenantId = tenant.getTenantID();
    }
}
