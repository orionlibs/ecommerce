package de.hybris.platform.ruleengine.strategies;

import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;

public interface DroolsKIEBaseFinderStrategy
{
    DroolsKIEBaseModel getKIEBaseForKIEModule(DroolsKIEModuleModel paramDroolsKIEModuleModel);
}
