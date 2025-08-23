package de.hybris.platform.previewpersonalizationweb.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class PreviewpersonalizationwebManager extends GeneratedPreviewpersonalizationwebManager
{
    private static final Logger LOG = Logger.getLogger(PreviewpersonalizationwebManager.class.getName());


    public PreviewpersonalizationwebManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of PreviewpersonalizationwebManager called.");
        }
    }


    public static PreviewpersonalizationwebManager getInstance()
    {
        return (PreviewpersonalizationwebManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("previewpersonalizationweb");
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
