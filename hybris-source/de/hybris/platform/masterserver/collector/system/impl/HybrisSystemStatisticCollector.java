package de.hybris.platform.masterserver.collector.system.impl;

import com.google.common.collect.ImmutableMap;
import com.hybris.statistics.collector.SystemStatisticsCollector;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.licence.Licence;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;

public class HybrisSystemStatisticCollector implements SystemStatisticsCollector<Map<String, Map<String, Object>>>
{
    public Map<String, Map<String, Object>> collectStatistics()
    {
        Map<String, Object> result = new HashMap<>();
        Licence license = Licence.getDefaultLicence();
        result.put("licenseID", license.getID());
        result.put("isDemoOrDevelop", Boolean.valueOf(license.isDemoOrDevelopLicence()));
        result.put("daysLeftForDemoOrDevelop", getDaysLeftForDemoOrDevelopLicence(license));
        result.put("licenseExpirationDate", nullToEmpty(license.getExpirationDate()));
        result.put("licenseCacheLimit", Integer.valueOf(license.getCacheLimit()));
        result.put("modules", Utilities.getInstalledExtensionNames((Tenant)Registry.getMasterTenant()));
        result.put("slaveTenants", getSlaveTenantsIDsWithoutJunit());
        result.put("currentTenant", Registry.getCurrentTenant().getTenantID());
        result.put("buildNumber", Config.getParameter("build.number"));
        result.put("buildVersion", Config.getParameter("build.version"));
        result.put("applicationServer", getApplicationServerInfo());
        return (Map<String, Map<String, Object>>)ImmutableMap.builder().put("hybris", result).build();
    }


    private List<String> getSlaveTenantsIDsWithoutJunit()
    {
        List<String> result = new ArrayList<>();
        Set<String> tenants = Registry.getMasterTenant().getSlaveTenantIDs();
        for(String tenantID : tenants)
        {
            if(!"junit".equals(tenantID))
            {
                result.add(tenantID);
            }
        }
        return result;
    }


    private Integer getDaysLeftForDemoOrDevelopLicence(Licence licence)
    {
        return licence.isDemoOrDevelopLicence() ? licence.getDaysLeft() : null;
    }


    private Object nullToEmpty(Object value)
    {
        return (value == null) ? "" : value;
    }


    private String getApplicationServerInfo()
    {
        ServletContext servletContext = Registry.getServletContextIfExists();
        return (servletContext == null) ? "N/A" : servletContext.getServerInfo();
    }
}
