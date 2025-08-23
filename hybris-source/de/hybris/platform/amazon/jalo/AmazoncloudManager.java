package de.hybris.platform.amazon.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class AmazoncloudManager extends GeneratedAmazoncloudManager
{
    private static final Logger LOG = Logger.getLogger(AmazoncloudManager.class.getName());


    public static AmazoncloudManager getInstance()
    {
        return (AmazoncloudManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("amazoncloud");
    }


    public AmazoncloudManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of AmazoncloudManager called.");
        }
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("init() of AmazoncloudManager called. " + getTenant().getTenantID());
        }
    }


    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("destroy() of AmazoncloudManager called, current tenant: " + getTenant().getTenantID());
        }
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
