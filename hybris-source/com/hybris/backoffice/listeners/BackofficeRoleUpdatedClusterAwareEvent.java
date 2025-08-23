package com.hybris.backoffice.listeners;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.PublishEventContext;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BackofficeRoleUpdatedClusterAwareEvent extends AbstractEvent implements ClusterAwareEvent
{
    private final List<PK> roles;


    public BackofficeRoleUpdatedClusterAwareEvent(List<PK> roles)
    {
        this.roles = roles;
    }


    public boolean canPublish(PublishEventContext publishEventContext)
    {
        return true;
    }


    public Collection<PK> getRoles()
    {
        return Collections.unmodifiableCollection(this.roles);
    }
}
