package de.hybris.platform.personalizationintegration.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class PersonalizationintegrationManager extends GeneratedPersonalizationintegrationManager
{
    public static final PersonalizationintegrationManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (PersonalizationintegrationManager)em.getExtension("personalizationintegration");
    }
}
