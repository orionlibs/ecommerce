package de.hybris.platform.ruleengine.dao;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import java.util.List;

public interface RuleEngineContextDao
{
    AbstractRuleEngineContextModel findRuleEngineContextByName(String paramString);


    <T extends AbstractRuleEngineContextModel> List<T> findRuleEngineContextByRulesModule(AbstractRulesModuleModel paramAbstractRulesModuleModel);
}
