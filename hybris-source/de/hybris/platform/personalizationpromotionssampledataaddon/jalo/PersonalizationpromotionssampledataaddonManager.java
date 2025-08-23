package de.hybris.platform.personalizationpromotionssampledataaddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class PersonalizationpromotionssampledataaddonManager extends GeneratedPersonalizationpromotionssampledataaddonManager
{
    public static final PersonalizationpromotionssampledataaddonManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (PersonalizationpromotionssampledataaddonManager)em.getExtension("personalizationpromotionssampledataaddon");
    }
}
