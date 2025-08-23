package de.hybris.platform.b2bcommerce.backoffice.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class B2bcommercebackofficeManager extends GeneratedB2bcommercebackofficeManager
{
    private static final Logger LOG = Logger.getLogger(B2bcommercebackofficeManager.class.getName());


    public static B2bcommercebackofficeManager getInstance()
    {
        return (B2bcommercebackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("b2bcommercebackoffice");
    }


    public B2bcommercebackofficeManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of B2bcommercebackofficeManager called.");
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
