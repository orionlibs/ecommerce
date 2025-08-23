package com.hybris.hybrisdatasupplierbackoffice.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class HybrisdatasupplierbackofficeManager extends GeneratedHybrisdatasupplierbackofficeManager
{
    private static final Logger LOG = Logger.getLogger(HybrisdatasupplierbackofficeManager.class.getName());


    public static HybrisdatasupplierbackofficeManager getInstance()
    {
        return (HybrisdatasupplierbackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("hybrisdatasupplierbackoffice");
    }


    public HybrisdatasupplierbackofficeManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of HybrisdatasupplierbackofficeManager called.");
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
