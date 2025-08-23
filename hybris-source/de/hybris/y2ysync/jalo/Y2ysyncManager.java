package de.hybris.y2ysync.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class Y2ysyncManager extends GeneratedY2ysyncManager
{
    private static final Logger LOG = Logger.getLogger(Y2ysyncManager.class.getName());


    public static Y2ysyncManager getInstance()
    {
        return (Y2ysyncManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("y2ysync");
    }


    public Y2ysyncManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of Y2ysyncManager called.");
        }
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("init() of Y2ysyncManager called. " + getTenant().getTenantID());
        }
    }


    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("destroy() of Y2ysyncManager called, current tenant: " + getTenant().getTenantID());
        }
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
