package de.hybris.platform.servicelayer.web;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.spring.HybrisContextLoaderListener;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.FilterConfig;
import org.apache.commons.lang.StringUtils;

public class DefaultXSSFilterConfig implements XSSFilter.XSSFilterConfig
{
    private final ConfigIntf masterTenantConfiguration;
    private final String webroot;
    private final String extensionName;
    private final String tenantIDForWebapp;
    private FilterConfigChangeListener changeListener;


    public DefaultXSSFilterConfig(FilterConfig filterConfig)
    {
        this.masterTenantConfiguration = Registry.getMasterTenant().getConfig();
        this.webroot = filterConfig.getServletContext().getContextPath();
        this.tenantIDForWebapp = HybrisContextLoaderListener.getTenantIDForWebapp(filterConfig.getServletContext());
        this.extensionName = Utilities.getExtensionForWebroot(this.webroot,
                        (this.tenantIDForWebapp == null) ? "master" : this.tenantIDForWebapp);
    }


    public void registerForConfigChanges(XSSFilter toUpdateOnConfigChange)
    {
        unregisterConfigChangeListener();
        this.changeListener = new FilterConfigChangeListener(toUpdateOnConfigChange, this.masterTenantConfiguration, "(" + this.extensionName + "\\.)?xss\\.filter\\..*");
        this.masterTenantConfiguration.registerConfigChangeListener((ConfigIntf.ConfigChangeListener)this.changeListener);
    }


    public void unregisterConfigChangeListener()
    {
        if(this.changeListener != null)
        {
            this.changeListener.unregister();
            this.changeListener = null;
        }
    }


    public boolean isEnabled()
    {
        boolean enabled;
        String enabledForExtension = this.masterTenantConfiguration.getParameter(this.extensionName + ".xss.filter.enabled");
        if(StringUtils.isBlank(enabledForExtension))
        {
            enabled = this.masterTenantConfiguration.getBoolean("xss.filter.enabled", true);
        }
        else
        {
            enabled = Boolean.parseBoolean(enabledForExtension);
        }
        if(XSSFilter.LOG.isDebugEnabled())
        {
            XSSFilter.LOG.debug("web xss filter for " + this.webroot + "(" + this.extensionName + ") enabled:" + enabled);
        }
        return enabled;
    }


    public boolean sortRules()
    {
        boolean sorted;
        String enabledForExtension = this.masterTenantConfiguration.getParameter(this.extensionName + ".xss.filter.sort.rules");
        if(StringUtils.isBlank(enabledForExtension))
        {
            sorted = this.masterTenantConfiguration.getBoolean("xss.filter.sort.rules", true);
        }
        else
        {
            sorted = Boolean.parseBoolean(enabledForExtension);
        }
        if(XSSFilter.LOG.isDebugEnabled())
        {
            XSSFilter.LOG.debug("web xss filter for " + this.webroot + "(" + this.extensionName + ") is sorting rules:" + sorted);
        }
        return sorted;
    }


    public XSSMatchAction getActionOnMatch()
    {
        String actionCode = this.masterTenantConfiguration.getParameter(this.extensionName + ".xss.filter.action");
        if(StringUtils.isBlank(actionCode))
        {
            actionCode = this.masterTenantConfiguration.getString("xss.filter.action", XSSMatchAction.STRIP.toString());
        }
        XSSMatchAction action = null;
        try
        {
            action = XSSMatchAction.valueOf(actionCode);
        }
        catch(IllegalArgumentException e)
        {
            XSSFilter.LOG
                            .error("Illegal match action code '" + actionCode + "' configured. Using " + XSSMatchAction.STRIP + " instead");
            action = XSSMatchAction.STRIP;
        }
        if(XSSFilter.LOG.isDebugEnabled())
        {
            XSSFilter.LOG.debug("web xss filter for " + this.webroot + "(" + this.extensionName + ") is using action:" + action);
        }
        return action;
    }


    public Map<String, String> getPatternDefinitions()
    {
        Map<String, String> rules = new LinkedHashMap<>(this.masterTenantConfiguration.getParametersMatching("xss\\.filter\\.rule\\..*(?i)"));
        rules.putAll(this.masterTenantConfiguration
                        .getParametersMatching(this.extensionName + "\\.(xss\\.filter\\.rule\\..*(?i))", true));
        return rules;
    }


    public Map<String, String> getHeadersToInject()
    {
        Map<String, String> headers = new LinkedHashMap<>(this.masterTenantConfiguration.getParametersMatching("xss\\.filter\\.header\\..*(?i)"));
        headers.putAll(this.masterTenantConfiguration
                        .getParametersMatching(this.extensionName + "\\.(xss\\.filter\\.header\\..*(?i))", true));
        return headers;
    }


    public Map<String, String> getHostHeaderWhiteList()
    {
        ConfigIntf configuration;
        if(this.tenantIDForWebapp != null)
        {
            Tenant tenant = Registry.getTenantByID(this.tenantIDForWebapp);
            if(tenant == null)
            {
                throw new RuntimeException("Couldn't find tenant '" + this.tenantIDForWebapp + "'");
            }
            configuration = tenant.getConfig();
        }
        else
        {
            configuration = this.masterTenantConfiguration;
        }
        return configuration.getParametersMatching("header\\.host\\.allow\\.(.*)", true);
    }
}
