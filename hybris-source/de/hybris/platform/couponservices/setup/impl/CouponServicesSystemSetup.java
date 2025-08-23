package de.hybris.platform.couponservices.setup.impl;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.ruleengine.setup.AbstractRuleEngineSystemSetup;

@SystemSetup(extension = "couponservices")
public class CouponServicesSystemSetup extends AbstractRuleEngineSystemSetup
{
    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.ALL)
    public void createEssentialData(SystemSetupContext context)
    {
        importImpexFile("/couponservices/import/essentialdata-definitions.impex", true, false);
        importImpexFile("/couponservices/import/essentialdata-mediafolder.impex", true, false);
        importImpexFile("/couponservices/import/essentialdata-users.impex", true, false);
        importImpexFile("/couponservices/import/essentialdata-validation.impex", true, false);
    }


    @SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
    public void createProjectData(SystemSetupContext context)
    {
        importImpexFile("/couponservices/import/projectdata-templates.impex", true, false);
    }
}
