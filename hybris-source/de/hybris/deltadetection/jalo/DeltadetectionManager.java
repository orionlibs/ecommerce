package de.hybris.deltadetection.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class DeltadetectionManager extends GeneratedDeltadetectionManager
{
    private static final Logger LOG = Logger.getLogger(DeltadetectionManager.class.getName());


    public static DeltadetectionManager getInstance()
    {
        return (DeltadetectionManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("deltadetection");
    }


    public DeltadetectionManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of DeltadetectionManager called.");
        }
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("init() of DeltadetectionManager called. " + getTenant().getTenantID());
        }
    }


    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("destroy() of DeltadetectionManager called, current tenant: " + getTenant().getTenantID());
        }
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
