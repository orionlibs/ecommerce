package de.hybris.platform.ruleengine.versioning;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import java.util.Optional;
import java.util.Set;

public interface ModuleVersioningService
{
    Optional<Long> getModuleVersion(AbstractRuleEngineRuleModel paramAbstractRuleEngineRuleModel);


    void assertRuleModuleVersion(AbstractRuleEngineRuleModel paramAbstractRuleEngineRuleModel, AbstractRulesModuleModel paramAbstractRulesModuleModel);


    void assertRuleModuleVersion(AbstractRulesModuleModel paramAbstractRulesModuleModel, Set<AbstractRuleEngineRuleModel> paramSet);


    Optional<Long> getDeployedModuleVersionForRule(String paramString1, String paramString2);
}
