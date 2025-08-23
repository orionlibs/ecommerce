package de.hybris.e2e.hybrisrootcauseanalysis.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class HybrisrootcauseanalysisManager extends GeneratedHybrisrootcauseanalysisManager
{
    private static final Logger LOG = Logger.getLogger(HybrisrootcauseanalysisManager.class.getName());


    public static HybrisrootcauseanalysisManager getInstance()
    {
        return (HybrisrootcauseanalysisManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("hybrisrootcauseanalysis");
    }


    public HybrisrootcauseanalysisManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of E2eManager called.");
        }
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("init() of E2eManager called. " + getTenant().getTenantID());
        }
    }


    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("destroy() of E2eManager called, current tenant: " + getTenant().getTenantID());
        }
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
