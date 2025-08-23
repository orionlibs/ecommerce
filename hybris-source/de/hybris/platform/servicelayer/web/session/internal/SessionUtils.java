package de.hybris.platform.servicelayer.web.session.internal;

import de.hybris.platform.core.Registry;

public class SessionUtils
{
    public static Object[] createSessionCacheKey(String id)
    {
        String tenantId = Registry.getCurrentTenant().getTenantID();
        return new Object[] {id, tenantId, "__SESSION__"};
    }
}
