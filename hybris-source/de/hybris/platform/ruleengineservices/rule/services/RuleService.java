package de.hybris.platform.ruleengineservices.rule.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.AbstractRuleTemplateModel;
import java.util.List;

public interface RuleService
{
    <T extends AbstractRuleModel> List<T> getAllRules();


    <T extends AbstractRuleModel> List<T> getAllRulesForType(Class paramClass);


    <T extends AbstractRuleModel> List<T> getAllActiveRules();


    <T extends AbstractRuleModel> List<T> getAllActiveRulesForType(Class paramClass);


    <T extends AbstractRuleModel> List<T> getActiveRulesForCatalogVersionAndRuleType(CatalogVersionModel paramCatalogVersionModel, RuleType paramRuleType);


    AbstractRuleModel getRuleForCode(String paramString);


    <T extends AbstractRuleModel> List<T> getAllRulesForCode(String paramString);


    <T extends AbstractRuleModel> List<T> getAllRulesForCodeAndStatus(String paramString, RuleStatus... paramVarArgs);


    <T extends AbstractRuleModel> List<T> getAllRulesForStatus(RuleStatus... paramVarArgs);


    <T extends AbstractRuleModel> T createRuleFromTemplate(AbstractRuleTemplateModel paramAbstractRuleTemplateModel);


    <T extends AbstractRuleModel> T createRuleFromTemplate(String paramString, AbstractRuleTemplateModel paramAbstractRuleTemplateModel);


    AbstractRuleModel cloneRule(AbstractRuleModel paramAbstractRuleModel);


    AbstractRuleModel cloneRule(String paramString, AbstractRuleModel paramAbstractRuleModel);


    Class<? extends AbstractRuleModel> getRuleTypeFromTemplate(Class<? extends AbstractRuleTemplateModel> paramClass);


    RuleType getEngineRuleTypeForRuleType(Class<?> paramClass);
}
