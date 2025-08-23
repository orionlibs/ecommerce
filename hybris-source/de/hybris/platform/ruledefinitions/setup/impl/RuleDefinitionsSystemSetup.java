package de.hybris.platform.ruledefinitions.setup.impl;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.ruleengine.setup.AbstractRuleEngineSystemSetup;

@SystemSetup(extension = "ruledefinitions")
public class RuleDefinitionsSystemSetup extends AbstractRuleEngineSystemSetup
{
    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.ALL)
    public void createEssentialData(SystemSetupContext context)
    {
        importImpexFile("/ruledefinitions/import/essentialdata-definitions.impex", true, false);
    }
}
