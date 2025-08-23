package de.hybris.platform.servicelayer.internal.tenant.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.event.events.AfterTenantInitializationClusterAwareEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import org.apache.log4j.Logger;

public class DefaultTenantInitializationListener extends AbstractEventListener<AfterTenantInitializationClusterAwareEvent>
{
    private static final Logger LOG = Logger.getLogger(DefaultTenantInitializationListener.class);


    protected void onEvent(AfterTenantInitializationClusterAwareEvent event)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("tenant restart event received");
        }
        Registry.getTenantByID(event.getTenantId()).resetTenantRestartMarker();
    }
}
