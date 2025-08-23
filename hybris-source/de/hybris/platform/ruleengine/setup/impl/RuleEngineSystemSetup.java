package de.hybris.platform.ruleengine.setup.impl;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.ruleengine.setup.AbstractRuleEngineSystemSetup;

@SystemSetup(extension = "ruleengine")
public class RuleEngineSystemSetup extends AbstractRuleEngineSystemSetup
{
    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.INIT)
    public void createEssentialData(SystemSetupContext context)
    {
        importImpexFile("/ruleengine/import/essentialdata-mediafolder.impex", true, false);
        importImpexFile("/ruleengine/import/essentialdata-jobs.impex", true, false);
    }
}
