package de.hybris.platform.personalizationsampledataaddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class PersonalizationsampledataaddonManager extends GeneratedPersonalizationsampledataaddonManager
{
    public static final PersonalizationsampledataaddonManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (PersonalizationsampledataaddonManager)em.getExtension("personalizationsampledataaddon");
    }
}
