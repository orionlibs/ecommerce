package de.hybris.platform.personalizationcmsweb.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class PersonalizationcmswebManager extends GeneratedPersonalizationcmswebManager
{
    public static final PersonalizationcmswebManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (PersonalizationcmswebManager)em.getExtension("personalizationcmsweb");
    }
}
