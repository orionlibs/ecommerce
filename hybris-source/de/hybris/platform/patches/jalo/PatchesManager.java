package de.hybris.platform.patches.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class PatchesManager extends GeneratedPatchesManager
{
    private static final long serialVersionUID = 8359349634689021979L;
    private static final Logger LOG = Logger.getLogger(PatchesManager.class.getName());


    public PatchesManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of PatchesManager called.");
        }
    }


    public static PatchesManager getInstance()
    {
        return (PatchesManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("patches");
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
