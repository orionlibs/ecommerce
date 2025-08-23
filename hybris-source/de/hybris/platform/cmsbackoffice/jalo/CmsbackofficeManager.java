package de.hybris.platform.cmsbackoffice.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class CmsbackofficeManager extends GeneratedCmsbackofficeManager
{
    private static final Logger LOG = Logger.getLogger(CmsbackofficeManager.class.getName());


    public static CmsbackofficeManager getInstance()
    {
        return (CmsbackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("cmsbackoffice");
    }


    public CmsbackofficeManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of CmsbackofficeManager called.");
        }
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
