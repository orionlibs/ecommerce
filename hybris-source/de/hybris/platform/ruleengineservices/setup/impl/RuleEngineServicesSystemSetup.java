package de.hybris.platform.ruleengineservices.setup.impl;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.ruleengine.setup.AbstractRuleEngineSystemSetup;

@SystemSetup(extension = "ruleengineservices")
public class RuleEngineServicesSystemSetup extends AbstractRuleEngineSystemSetup
{
    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.ALL)
    public void createEssentialData(SystemSetupContext context)
    {
        importImpexFile("/ruleengineservices/import/essentialdata-coredefinitions.impex", true, false);
        importImpexFile("/ruleengineservices/import/essentialdata-jobs.impex", true, false);
        importImpexFile("/ruleengineservices/import/essentialdata-validation.impex", true, false);
    }
}
