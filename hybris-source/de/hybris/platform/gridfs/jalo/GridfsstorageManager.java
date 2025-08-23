package de.hybris.platform.gridfs.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class GridfsstorageManager extends GeneratedGridfsstorageManager
{
    private static final Logger LOG = Logger.getLogger(GridfsstorageManager.class.getName());


    public static GridfsstorageManager getInstance()
    {
        return (GridfsstorageManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("gridfsstorage");
    }


    public GridfsstorageManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of GridfsstorageManager called.");
        }
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("init() of GridfsstorageManager called. " + getTenant().getTenantID());
        }
    }


    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("destroy() of GridfsstorageManager called, current tenant: " + getTenant().getTenantID());
        }
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
