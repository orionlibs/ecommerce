package de.hybris.platform.ruleengine.versioning;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;

public interface RuleModelChecksumCalculator
{
    String calculateChecksumOf(AbstractRuleEngineRuleModel paramAbstractRuleEngineRuleModel);
}
