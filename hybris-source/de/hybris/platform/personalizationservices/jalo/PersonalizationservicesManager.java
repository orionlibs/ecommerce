package de.hybris.platform.personalizationservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class PersonalizationservicesManager extends GeneratedPersonalizationservicesManager
{
    public static final PersonalizationservicesManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (PersonalizationservicesManager)em.getExtension("personalizationservices");
    }
}
