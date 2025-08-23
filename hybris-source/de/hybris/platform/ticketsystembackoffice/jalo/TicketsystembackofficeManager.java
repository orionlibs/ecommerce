package de.hybris.platform.ticketsystembackoffice.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class TicketsystembackofficeManager extends GeneratedTicketsystembackofficeManager
{
    private static final Logger LOG = Logger.getLogger(TicketsystembackofficeManager.class.getName());


    public static TicketsystembackofficeManager getInstance()
    {
        return (TicketsystembackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("ticketsystembackoffice");
    }


    public TicketsystembackofficeManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of TicketsystembackofficeManager called.");
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
