package de.hybris.platform.samlsinglesignon.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class SamlsinglesignonManager extends GeneratedSamlsinglesignonManager
{
    private static final Logger LOG = Logger.getLogger(SamlsinglesignonManager.class.getName());


    public static SamlsinglesignonManager getInstance()
    {
        return (SamlsinglesignonManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("samlsinglesignon");
    }


    public SamlsinglesignonManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of SamlsinglesignonManager called.");
        }
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("init() of SamlsinglesignonManager called. " + getTenant().getTenantID());
        }
    }


    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("destroy() of SamlsinglesignonManager called, current tenant: " + getTenant().getTenantID());
        }
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
