package de.hybris.platform.personalizationpromotionsweb.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class PersonalizationpromotionswebManager extends GeneratedPersonalizationpromotionswebManager
{
    public static final PersonalizationpromotionswebManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (PersonalizationpromotionswebManager)em.getExtension("personalizationpromotionsweb");
    }
}
