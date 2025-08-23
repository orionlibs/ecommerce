package de.hybris.platform.embeddedserver.tomcat.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class TomcatembeddedserverManager extends GeneratedTomcatembeddedserverManager
{
    private static final Logger LOG = Logger.getLogger(TomcatembeddedserverManager.class.getName());


    public static TomcatembeddedserverManager getInstance()
    {
        return (TomcatembeddedserverManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("tomcatembeddedserver");
    }


    public TomcatembeddedserverManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of TomcatembeddedserverManager called.");
        }
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("init() of TomcatembeddedserverManager called. " + getTenant().getTenantID());
        }
    }


    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("destroy() of TomcatembeddedserverManager called, current tenant: " + getTenant().getTenantID());
        }
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
