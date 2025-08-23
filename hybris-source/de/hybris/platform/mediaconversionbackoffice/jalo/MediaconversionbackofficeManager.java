package de.hybris.platform.mediaconversionbackoffice.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class MediaconversionbackofficeManager extends GeneratedMediaconversionbackofficeManager
{
    private static final Logger LOG = Logger.getLogger(MediaconversionbackofficeManager.class.getName());


    public MediaconversionbackofficeManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of MediaconversionbackofficeManager called.");
        }
    }


    public static MediaconversionbackofficeManager getInstance()
    {
        return (MediaconversionbackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("mediaconversionbackoffice");
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("init() of YbackofficeManager called. " + getTenant().getTenantID());
        }
    }


    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("destroy() of YbackofficeManager called, current tenant: " + getTenant().getTenantID());
        }
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
