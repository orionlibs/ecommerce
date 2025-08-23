package de.hybris.platform.ruleengineservices.rule.dao;

import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengineservices.model.RuleGroupModel;
import java.util.List;
import java.util.Optional;

public interface RuleGroupDao
{
    Optional<RuleGroupModel> findRuleGroupByCode(String paramString);


    List<RuleGroupModel> findRuleGroupOfType(RuleType paramRuleType);


    List<RuleGroupModel> findAllNotReferredRuleGroups();
}
