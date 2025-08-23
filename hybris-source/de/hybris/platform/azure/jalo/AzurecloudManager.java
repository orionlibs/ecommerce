package de.hybris.platform.azure.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class AzurecloudManager extends GeneratedAzurecloudManager
{
    private static final Logger LOG = Logger.getLogger(AzurecloudManager.class.getName());


    public static AzurecloudManager getInstance()
    {
        return (AzurecloudManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("azurecloud");
    }


    public AzurecloudManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of AzurecloudManager called.");
        }
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("init() of AzurecloudManager called. " + getTenant().getTenantID());
        }
    }


    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("destroy() of AzurecloudManager called, current tenant: " + getTenant().getTenantID());
        }
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
