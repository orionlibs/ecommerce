package de.hybris.platform.ruleengine.dao;

import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import java.util.List;

public interface RulesModuleDao
{
    <T extends AbstractRulesModuleModel> T findByName(String paramString);


    List<AbstractRulesModuleModel> findAll();


    <T extends AbstractRulesModuleModel> T findByNameAndVersion(String paramString, long paramLong);


    List<AbstractRulesModuleModel> findActiveRulesModulesByRuleType(RuleType paramRuleType);
}
