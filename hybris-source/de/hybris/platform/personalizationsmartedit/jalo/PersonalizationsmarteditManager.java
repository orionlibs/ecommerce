package de.hybris.platform.personalizationsmartedit.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class PersonalizationsmarteditManager extends GeneratedPersonalizationsmarteditManager
{
    public static final PersonalizationsmarteditManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (PersonalizationsmarteditManager)em.getExtension("personalizationsmartedit");
    }
}
