package de.hybris.subscriptionbackoffice.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class SubscriptionbackofficeManager extends GeneratedSubscriptionbackofficeManager
{
    private static final Logger LOG = Logger.getLogger(SubscriptionbackofficeManager.class.getName());


    public static SubscriptionbackofficeManager getInstance()
    {
        return (SubscriptionbackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("subscriptionbackoffice");
    }


    public SubscriptionbackofficeManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of SubscriptionbackofficeManager called.");
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
