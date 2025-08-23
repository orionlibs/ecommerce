package de.hybris.platform.platformbackoffice.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlatformbackofficeManager extends GeneratedPlatformbackofficeManager
{
    private static final Logger LOG = LoggerFactory.getLogger(PlatformbackofficeManager.class.getName());


    public static PlatformbackofficeManager getInstance()
    {
        return (PlatformbackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("platformbackoffice");
    }


    public PlatformbackofficeManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of PlatformbackofficeManager called.");
        }
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("init() of YbackofficeManager called. %s", new Object[] {getTenant().getTenantID()}));
        }
    }


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
