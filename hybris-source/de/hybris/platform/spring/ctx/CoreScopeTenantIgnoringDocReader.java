package de.hybris.platform.spring.ctx;

import de.hybris.platform.core.Registry;

public class CoreScopeTenantIgnoringDocReader extends ScopeTenantIgnoreDocReader
{
    protected boolean readLazyInitOverrideConfiguration()
    {
        return Registry.getCurrentTenantNoFallback().getConfig()
                        .getBoolean("spring.lazy.load.singletons", false);
    }
}
