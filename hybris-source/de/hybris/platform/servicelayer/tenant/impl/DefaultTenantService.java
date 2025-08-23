package de.hybris.platform.servicelayer.tenant.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.internal.service.AbstractService;
import de.hybris.platform.servicelayer.tenant.TenantService;

public class DefaultTenantService extends AbstractService implements TenantService
{
    public String getCurrentTenantId()
    {
        return Registry.getCurrentTenantNoFallback().getTenantID();
    }
}
