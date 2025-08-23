package de.hybris.platform.patches.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class PatchesbackofficeManager extends GeneratedPatchesbackofficeManager
{
    private static final Logger LOG = Logger.getLogger(PatchesbackofficeManager.class.getName());


    public PatchesbackofficeManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of PatchesbackofficeManager called.");
        }
    }


    public static PatchesbackofficeManager getInstance()
    {
        return (PatchesbackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("patchesbackoffice");
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
