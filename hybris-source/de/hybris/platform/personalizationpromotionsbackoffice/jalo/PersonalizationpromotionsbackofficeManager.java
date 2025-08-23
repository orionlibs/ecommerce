package de.hybris.platform.personalizationpromotionsbackoffice.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class PersonalizationpromotionsbackofficeManager extends GeneratedPersonalizationpromotionsbackofficeManager
{
    private static final Logger LOG = Logger.getLogger(PersonalizationpromotionsbackofficeManager.class.getName());


    public PersonalizationpromotionsbackofficeManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of PersonalizationpromotionsbackofficeManager called.");
        }
    }


    public static PersonalizationpromotionsbackofficeManager getInstance()
    {
        return (PersonalizationpromotionsbackofficeManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("personalizationpromotionsbackoffice");
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
