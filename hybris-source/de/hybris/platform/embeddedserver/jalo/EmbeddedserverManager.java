package de.hybris.platform.embeddedserver.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class EmbeddedserverManager extends GeneratedEmbeddedserverManager
{
    private static final Logger LOG = Logger.getLogger(EmbeddedserverManager.class.getName());


    public static EmbeddedserverManager getInstance()
    {
        return (EmbeddedserverManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("embeddedserver");
    }


    public EmbeddedserverManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of EmbeddedserverManager called.");
        }
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("init() of EmbeddedserverManager called. " + getTenant().getTenantID());
        }
    }


    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("destroy() of EmbeddedserverManager called, current tenant: " + getTenant().getTenantID());
        }
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
