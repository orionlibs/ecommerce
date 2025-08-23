package de.hybris.platform.ruleengineservices.rule.dao;

import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import java.util.List;
import java.util.Optional;

public interface RuleDao
{
    <T extends AbstractRuleModel> List<T> findAllRules();


    <T extends AbstractRuleModel> List<T> findAllRulesByType(Class<T> paramClass);


    <T extends AbstractRuleModel> List<T> findAllActiveRules();


    <T extends AbstractRuleModel> List<T> findAllActiveRulesByType(Class<T> paramClass);


    <T extends AbstractRuleModel> T findRuleByCode(String paramString);


    <T extends AbstractRuleModel> List<T> findAllRuleVersionsByCode(String paramString);


    <T extends AbstractRuleModel> T findRuleByCodeAndType(String paramString, Class<T> paramClass);


    RuleType findEngineRuleTypeByRuleType(Class<?> paramClass);


    Long getRuleVersion(String paramString);


    Optional<AbstractRuleModel> findRuleByCodeAndStatus(String paramString, RuleStatus paramRuleStatus);


    <T extends AbstractRuleModel> List<T> findAllRuleVersionsByCodeAndStatus(String paramString, RuleStatus paramRuleStatus);


    <T extends AbstractRuleModel> List<T> findAllRuleVersionsByCodeAndStatuses(String paramString, RuleStatus... paramVarArgs);


    <T extends AbstractRuleModel> List<T> findAllRulesWithStatuses(RuleStatus... paramVarArgs);


    <T extends AbstractRuleModel> List<T> findByVersionAndStatuses(Long paramLong, RuleStatus... paramVarArgs);


    Optional<AbstractRuleModel> findRuleByCodeAndVersion(String paramString, Long paramLong);
}
