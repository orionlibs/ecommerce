package de.hybris.platform.promotionengineservices.promotionengine.setup.impl;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.ruleengine.setup.AbstractRuleEngineSystemSetup;

@SystemSetup(extension = "promotionengineservices")
public class PromotionEngineServicesSystemSetup extends AbstractRuleEngineSystemSetup
{
    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.ALL)
    public void createEssentialData(SystemSetupContext context)
    {
        importImpexFile("/promotionengineservices/import/essentialdata-cronjobs.impex", true, false);
        importImpexFile("/promotionengineservices/import/essentialdata-definitions.impex", true, false);
        importImpexFile("/promotionengineservices/import/essentialdata-users.impex", true, false);
        importImpexFile("/promotionengineservices/import/essentialdata-validation.impex", true, false);
    }


    @SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
    public void createProjectData(SystemSetupContext context)
    {
        importImpexFile("/promotionengineservices/import/projectdata-module.impex", true, false);
        importImpexFile("/promotionengineservices/import/projectdata-templates.impex", true, false);
    }
}
