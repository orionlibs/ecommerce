package de.hybris.platform.personalizationfacades.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.JspContext;
import java.util.Map;
import org.apache.log4j.Logger;

public class PersonalizationfacadesManager extends GeneratedPersonalizationfacadesManager
{
    private static final Logger LOG = Logger.getLogger(PersonalizationfacadesManager.class.getName());


    public PersonalizationfacadesManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of PersonalizationfacadesManager called.");
        }
    }


    public static PersonalizationfacadesManager getInstance()
    {
        return (PersonalizationfacadesManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("personalizationfacades");
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }
}
