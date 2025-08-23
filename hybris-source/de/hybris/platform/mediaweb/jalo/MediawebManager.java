package de.hybris.platform.mediaweb.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class MediawebManager extends GeneratedMediawebManager
{
    private static final Logger LOG = Logger.getLogger(MediawebManager.class.getName());


    public static MediawebManager getInstance()
    {
        return (MediawebManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("mediaweb");
    }


    public MediawebManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of MediawebManager called.");
        }
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("init() of MediawebManager called. " + getTenant().getTenantID());
        }
    }


    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("destroy() of MediawebManager called, current tenant: " + getTenant().getTenantID());
        }
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
