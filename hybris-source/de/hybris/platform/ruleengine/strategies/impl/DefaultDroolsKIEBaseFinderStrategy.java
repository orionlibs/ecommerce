package de.hybris.platform.ruleengine.strategies.impl;

import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.strategies.DroolsKIEBaseFinderStrategy;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;

public class DefaultDroolsKIEBaseFinderStrategy implements DroolsKIEBaseFinderStrategy
{
    public DroolsKIEBaseModel getKIEBaseForKIEModule(DroolsKIEModuleModel kieModule)
    {
        DroolsKIEBaseModel kieBase = kieModule.getDefaultKIEBase();
        if(kieBase == null)
        {
            Collection<DroolsKIEBaseModel> droolsKIEBases = kieModule.getKieBases();
            if(CollectionUtils.isEmpty(droolsKIEBases))
            {
                return null;
            }
            kieBase = droolsKIEBases.iterator().next();
        }
        return kieBase;
    }
}
