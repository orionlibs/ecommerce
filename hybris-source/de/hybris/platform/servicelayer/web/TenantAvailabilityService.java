package de.hybris.platform.servicelayer.web;

import de.hybris.platform.core.Initialization;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.system.InitializationLockDao;
import de.hybris.platform.core.system.InitializationLockHandler;
import de.hybris.platform.core.system.InitializationLockInfo;
import de.hybris.platform.core.system.impl.DefaultInitLockDao;

public class TenantAvailabilityService
{
    private final InitializationLockDao defaultInitLockDao;


    public TenantAvailabilityService(InitializationLockDao defaultInitLockDao)
    {
        this.defaultInitLockDao = defaultInitLockDao;
    }


    public static TenantAvailabilityService createDefault()
    {
        return new TenantAvailabilityService((InitializationLockDao)new DefaultInitLockDao());
    }


    public boolean isSystemInitialized()
    {
        Tenant tenant = Registry.getCurrentTenantNoFallback();
        if(tenant == null)
        {
            return false;
        }
        return tenant.getJaloConnection().isSystemInitialized();
    }


    public boolean isMasterTenantRunning()
    {
        return Initialization.hasRunningMasterTenant();
    }


    public boolean isTenantAvailable(String tenantId)
    {
        if(!isSystemInitialized())
        {
            return false;
        }
        if(isMasterTenantRunning())
        {
            InitializationLockInfo lockInfo = (new InitializationLockHandler(this.defaultInitLockDao)).getLockInfo();
            boolean isBeingInitialized = (lockInfo != null && lockInfo.isLocked() && "System initialization".equals(lockInfo.getProcessName()) && (lockInfo.getTenantId().equals(tenantId) || lockInfo.getTenantId().equals("master")));
            return !isBeingInitialized;
        }
        return true;
    }
}
