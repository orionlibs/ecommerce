package de.hybris.platform.personalizationintegrationbackoffice.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class PersonalizationintegrationbackofficeManager extends GeneratedPersonalizationintegrationbackofficeManager
{
    private static final Logger LOG = Logger.getLogger(PersonalizationintegrationbackofficeManager.class.getName());


    public PersonalizationintegrationbackofficeManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of PersonalizationintegrationbackofficeManager called.");
        }
    }


    public static PersonalizationintegrationbackofficeManager getInstance()
    {
        return (PersonalizationintegrationbackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("personalizationintegrationbackoffice");
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
