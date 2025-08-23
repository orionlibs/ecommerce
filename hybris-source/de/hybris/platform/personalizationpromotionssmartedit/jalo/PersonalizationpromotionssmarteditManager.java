package de.hybris.platform.personalizationpromotionssmartedit.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class PersonalizationpromotionssmarteditManager extends GeneratedPersonalizationpromotionssmarteditManager
{
    public static final PersonalizationpromotionssmarteditManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (PersonalizationpromotionssmarteditManager)em.getExtension("personalizationpromotionssmartedit");
    }
}
