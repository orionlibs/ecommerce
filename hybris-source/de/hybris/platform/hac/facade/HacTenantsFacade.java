package de.hybris.platform.hac.facade;

import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.SlaveTenant;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.hac.data.dto.ConfiguredExtensionData;
import de.hybris.platform.hac.data.dto.TenantData;
import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.util.Utilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class HacTenantsFacade
{
    private static final Logger LOG = Logger.getLogger(HacTenantsFacade.class);
    private static final String MASTER_TENANT_ID = "master";
    private TenantService tenantService;


    public List<TenantData> getAllSlaveTenants()
    {
        List<TenantData> result = new ArrayList<>();
        MasterTenant master = Registry.getMasterTenant();
        Set<String> tenantIDs = master.getSlaveTenantIDs();
        for(String tenantID : tenantIDs)
        {
            result.add(getSlaveTenant(tenantID));
        }
        return result;
    }


    public TenantData getTenantDataById(String tenantId)
    {
        return "master".equalsIgnoreCase(tenantId) ? getMasterTenant() :
                        getAllSlaveTenants().stream().filter(r -> r.getTenantID().equals(tenantId)).findFirst().orElse(null);
    }


    public String getCurrentTenantID()
    {
        return this.tenantService.getCurrentTenantId();
    }


    public Map<String, ConfiguredExtensionData> getExtensionsForSlaveTenant(String slaveTenantId)
    {
        SlaveTenant slaveTenant = (SlaveTenant)Registry.getSlaveTenants().get(slaveTenantId);
        List<String> availableExt = new ArrayList<>();
        if(slaveTenant != null)
        {
            List<ExtensionInfo> allExtensions = ConfigUtil.getPlatformConfig(Registry.class).getExtensionInfosInBuildOrder();
            HashSet<String> allowedNames = new HashSet<>(slaveTenant.getTenantSpecificExtensionNames());
            for(ExtensionInfo ext : allExtensions)
            {
                if(ext.getCoreModule() == null || allowedNames.contains(ext.getName()))
                {
                    availableExt.add(ext.getName());
                }
            }
            return fillInConfiguredExtension((AbstractTenant)slaveTenant, availableExt);
        }
        return Collections.EMPTY_MAP;
    }


    public Map<String, ConfiguredExtensionData> getExtensionsForMasterTenant()
    {
        MasterTenant masterTenant = Registry.getMasterTenant();
        List<String> availableExt = new ArrayList<>();
        List<ExtensionInfo> allExtensions = ConfigUtil.getPlatformConfig(Registry.class).getExtensionInfosInBuildOrder();
        for(ExtensionInfo ext : allExtensions)
        {
            availableExt.add(ext.getName());
        }
        return fillInConfiguredExtension((AbstractTenant)masterTenant, availableExt);
    }


    private Map<String, ConfiguredExtensionData> fillInConfiguredExtension(AbstractTenant tenant, List<String> availableExt)
    {
        ImmutableMap.Builder<String, ConfiguredExtensionData> result = new ImmutableMap.Builder();
        Map<String, String> configuredWebModules = Utilities.getInstalledWebModules();
        Collections.sort(availableExt);
        List<String> configured = tenant.getTenantSpecificExtensionNames();
        for(String extName : availableExt)
        {
            ConfiguredExtensionData data = new ConfiguredExtensionData();
            data.setExtensionName(extName);
            data.setEnabled(configured.contains(extName));
            boolean hasWebModule = configuredWebModules.containsKey(extName);
            data.setWebExtension(hasWebModule);
            data.setContextName(hasWebModule ? Utilities.getWebroot(extName, tenant.getTenantID()) : null);
            result.put(extName, data);
        }
        return (Map<String, ConfiguredExtensionData>)result.build();
    }


    public TenantData getMasterTenant()
    {
        MasterTenant masterTenant = Registry.getMasterTenant();
        TenantData tenantData = initContextData((Tenant)masterTenant);
        tenantData.setTenantID(masterTenant.getTenantID());
        tenantData.setActivated(true);
        tenantData.setDbDriver(masterTenant.getDataSource().getDriverVersion());
        tenantData.setDbUrl(masterTenant.getDataSource().getDatabaseURL());
        tenantData.setDbUser(masterTenant.getDataSource().getDatabaseUser());
        tenantData.setDbPassword("");
        tenantData.setTablePrefix(masterTenant.getDataSource().getTablePrefix());
        tenantData.setJndiPool(masterTenant.getDataSource().getJNDIName());
        tenantData.setTimezone(masterTenant.getTenantSpecificTimeZone().getID());
        tenantData.setLocale(masterTenant.getTenantSpecificLocale());
        tenantData.setInitialized(true);
        tenantData.setCreated(true);
        tenantData.setMaster(true);
        return tenantData;
    }


    private TenantData initContextData(Tenant givenTenant)
    {
        TenantData tenantData = new TenantData();
        String hacWebrootForTenant = Utilities.getWebroot("hac", givenTenant.getTenantID());
        if(hacWebrootForTenant != null)
        {
            tenantData.setCtx(hacWebrootForTenant);
            tenantData.setCtxEnabled("");
        }
        else
        {
            LOG.info("Web app 'hac' is not mapped for tenant " + givenTenant + ". Please add hac.webroot=<webroot> if you need to access hac with that tenant.");
            tenantData.setCtxEnabled("disabled");
        }
        return tenantData;
    }


    public TenantData getSlaveTenant(String slaveTenantId)
    {
        TenantData tenantData = null;
        try
        {
            SlaveTenant slaveTenant = (SlaveTenant)Registry.getSlaveTenants().get(slaveTenantId);
            if(slaveTenant != null)
            {
                tenantData = initContextData((Tenant)slaveTenant);
                tenantData.setTenantID(slaveTenantId);
                tenantData.setActivated(slaveTenant.isActive());
                tenantData.setDbDriver(slaveTenant.getDatabaseDriver());
                tenantData.setDbUrl(slaveTenant.getDatabaseURL());
                tenantData.setDbUser(slaveTenant.getDatabaseUser());
                tenantData.setDbPassword(slaveTenant.getDatabasePassword());
                tenantData.setTablePrefix(slaveTenant.getDatabaseTablePrefix());
                tenantData.setJndiPool(slaveTenant.getDatabaseFromJNDI());
                tenantData.setTimezone(slaveTenant.getTenantSpecificTimeZone().getID());
                tenantData.setLocale(slaveTenant.getTenantSpecificLocale());
                tenantData.setInitialized(slaveTenant.isInitialized());
                tenantData.setCreated(true);
                tenantData.setMaster(false);
            }
        }
        catch(IllegalArgumentException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e.getMessage(), e);
            }
            LOG.error(e.getMessage());
        }
        return tenantData;
    }


    public boolean isCurrentTenantMaster()
    {
        return Registry.getCurrentTenant() instanceof MasterTenant;
    }


    @Required
    public void setTenantService(TenantService tenantService)
    {
        this.tenantService = tenantService;
    }
}
