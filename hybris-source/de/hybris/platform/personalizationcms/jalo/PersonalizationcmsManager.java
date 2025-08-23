package de.hybris.platform.personalizationcms.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class PersonalizationcmsManager extends GeneratedPersonalizationcmsManager
{
    public static final PersonalizationcmsManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (PersonalizationcmsManager)em.getExtension("personalizationcms");
    }
}
