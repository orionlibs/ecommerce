package de.hybris.platform.spring.ctx;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.config.ConfigIntf;

public class WebScopeTenantIgnoreDocReader extends ScopeTenantIgnoreDocReader
{
    protected boolean readLazyInitOverrideConfiguration()
    {
        ConfigIntf cfg = Registry.getCurrentTenantNoFallback().getConfig();
        return (cfg.getBoolean("spring.lazy.load.singletons", false) || cfg
                        .getBoolean("web.spring.lazy.load.singletons", false));
    }
}
