package de.hybris.platform.ruleengineservices.maintenance.systemsetup;

import de.hybris.platform.ruleengineservices.jalo.SourceRule;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import java.util.Collection;

public interface RuleEngineSystemSetup
{
    void registerSourceRulesForDeployment(Collection<SourceRuleModel> paramCollection, Collection<String> paramCollection1);


    void registerSourceRuleForDeployment(SourceRule paramSourceRule, String... paramVarArgs);


    <T extends de.hybris.platform.ruleengine.jalo.AbstractRulesModule> void initializeModule(T paramT);
}
