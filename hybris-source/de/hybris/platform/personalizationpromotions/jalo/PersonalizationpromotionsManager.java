package de.hybris.platform.personalizationpromotions.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class PersonalizationpromotionsManager extends GeneratedPersonalizationpromotionsManager
{
    public static final PersonalizationpromotionsManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (PersonalizationpromotionsManager)em.getExtension("personalizationpromotions");
    }
}
