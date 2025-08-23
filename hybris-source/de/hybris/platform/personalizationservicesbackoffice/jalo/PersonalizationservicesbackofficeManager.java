package de.hybris.platform.personalizationservicesbackoffice.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class PersonalizationservicesbackofficeManager extends GeneratedPersonalizationservicesbackofficeManager
{
    private static final Logger LOG = Logger.getLogger(PersonalizationservicesbackofficeManager.class.getName());


    public PersonalizationservicesbackofficeManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of PersonalizationservicesbackofficeManager called.");
        }
    }


    public static PersonalizationservicesbackofficeManager getInstance()
    {
        return (PersonalizationservicesbackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("personalizationservicesbackoffice");
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
