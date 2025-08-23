package com.hybris.backoffice.searchservices.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackofficesearchservicesManager extends GeneratedBackofficesearchservicesManager
{
    private static final Logger LOG = LoggerFactory.getLogger(BackofficesearchservicesManager.class.getName());


    public BackofficesearchservicesManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of BackofficesearchservicesManager called.");
        }
    }


    public static BackofficesearchservicesManager getInstance()
    {
        return (BackofficesearchservicesManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("backofficesearchservices");
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("init() of YbackofficeManager called. %s", new Object[] {getTenant().getTenantID()}));
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("destroy() of YbackofficeManager called, current tenant: %s", new Object[] {getTenant().getTenantID()}));
        }
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
