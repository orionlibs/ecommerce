package de.hybris.platform.scripting.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class ScriptingManager extends GeneratedScriptingManager
{
    private static final Logger LOG = Logger.getLogger(ScriptingManager.class.getName());


    public static ScriptingManager getInstance()
    {
        return (ScriptingManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("scripting");
    }


    public ScriptingManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of ScriptingManager called.");
        }
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("init() of ScriptingManager called. " + getTenant().getTenantID());
        }
    }


    public void destroy()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("destroy() of ScriptingManager called, current tenant: " + getTenant().getTenantID());
        }
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
