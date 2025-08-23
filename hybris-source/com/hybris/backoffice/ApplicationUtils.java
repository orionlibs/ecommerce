package com.hybris.backoffice;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.util.RedeployUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationUtils.class);


    private ApplicationUtils()
    {
        throw new UnsupportedOperationException();
    }


    public static boolean isPlatformReady()
    {
        try
        {
            boolean isStarted = Registry.isCurrentTenantStarted();
            boolean notShuttingDown = !RedeployUtilities.isShutdownInProgress();
            boolean systemInitialized = (Registry.hasCurrentTenant() && Registry.getCurrentTenant().getJaloConnection().isSystemInitialized());
            return (notShuttingDown && systemInitialized && isStarted);
        }
        catch(IllegalStateException e)
        {
            LOGGER.debug("Platform check failed", e);
            return false;
        }
    }


    public static boolean isNotJunitTenant()
    {
        try
        {
            return !Registry.isCurrentTenant((Tenant)Registry.getSlaveJunitTenant());
        }
        catch(IllegalStateException e)
        {
            LOGGER.debug("Tenant check failed", e);
            return true;
        }
    }
}
