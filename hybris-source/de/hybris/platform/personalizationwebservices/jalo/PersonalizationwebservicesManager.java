package de.hybris.platform.personalizationwebservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class PersonalizationwebservicesManager extends GeneratedPersonalizationwebservicesManager
{
    public static final PersonalizationwebservicesManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (PersonalizationwebservicesManager)em.getExtension("personalizationwebservices");
    }
}
