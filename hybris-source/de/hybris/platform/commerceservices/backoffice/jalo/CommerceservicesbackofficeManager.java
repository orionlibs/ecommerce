package de.hybris.platform.commerceservices.backoffice.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class CommerceservicesbackofficeManager extends GeneratedCommerceservicesbackofficeManager
{
    private static final Logger LOG = Logger.getLogger(CommerceservicesbackofficeManager.class.getName());


    public static CommerceservicesbackofficeManager getInstance()
    {
        return (CommerceservicesbackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("commerceservicesbackoffice");
    }


    public CommerceservicesbackofficeManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of CommerceservicesbackofficeManager called.");
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
