package de.hybris.platform.personalizationcmsbackoffice.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class PersonalizationcmsbackofficeManager extends GeneratedPersonalizationcmsbackofficeManager
{
    private static final Logger LOG = Logger.getLogger(PersonalizationcmsbackofficeManager.class.getName());


    public PersonalizationcmsbackofficeManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of PersonalizationcmsbackofficeManager called.");
        }
    }


    public static PersonalizationcmsbackofficeManager getInstance()
    {
        return (PersonalizationcmsbackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("personalizationcmsbackoffice");
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
