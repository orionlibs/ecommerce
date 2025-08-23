package de.hybris.platform.servicelayer.security.permissions;

import de.hybris.platform.util.logging.Logs;
import de.hybris.platform.util.persistence.PersistenceUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class PermissionManagementStrategyFactory
{
    private static final Logger LOG = Logger.getLogger(PermissionManagementStrategyFactory.class);
    private PermissionManagementStrategy jaloStrategy;
    private PermissionManagementStrategy sldStrategy;


    public PermissionManagementStrategy getStrategy()
    {
        if(isLegacyPersistence())
        {
            Logs.debug(LOG, () -> "Legacy persistence enabled - using Jalo strategy for managing ACLs");
            return this.jaloStrategy;
        }
        Logs.debug(LOG, () -> "Legacy persistence disabled - using SLD strategy for managing ACLs");
        return this.sldStrategy;
    }


    boolean isLegacyPersistence()
    {
        return PersistenceUtils.isPersistenceLegacyModeEnabled();
    }


    @Required
    public void setJaloStrategy(PermissionManagementStrategy jaloStrategy)
    {
        this.jaloStrategy = jaloStrategy;
    }


    @Required
    public void setSldStrategy(PermissionManagementStrategy sldStrategy)
    {
        this.sldStrategy = sldStrategy;
    }
}
