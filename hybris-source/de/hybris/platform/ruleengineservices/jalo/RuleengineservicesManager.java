package de.hybris.platform.ruleengineservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class RuleengineservicesManager extends GeneratedRuleengineservicesManager
{
    public static final RuleengineservicesManager getInstance()
    {
        ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
        return (RuleengineservicesManager)em.getExtension("ruleengineservices");
    }
}
