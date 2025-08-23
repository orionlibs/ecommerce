package de.hybris.platform.ruleengine.versioning.impl;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.versioning.RuleModelChecksumCalculator;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.MapUtils;

public class RuleEngineRuleModelChecksumCalculator implements RuleModelChecksumCalculator
{
    public String calculateChecksumOf(AbstractRuleEngineRuleModel rule)
    {
        Objects.requireNonNull(rule, "Rule model object is expected to be not null here");
        StringBuilder checksumPayload = new StringBuilder();
        String ruleContent = rule.getRuleContent();
        if(Objects.nonNull(ruleContent))
        {
            checksumPayload.append(ruleContent);
            if(rule instanceof DroolsRuleModel)
            {
                DroolsRuleModel droolsRule = (DroolsRuleModel)rule;
                Map<String, String> ruleGlobals = droolsRule.getGlobals();
                if(MapUtils.isNotEmpty(ruleGlobals))
                {
                    checksumPayload.append(";Globals_");
                    ruleGlobals.entrySet().stream()
                                    .forEach(e -> checksumPayload.append((String)e.getKey()).append(":").append((String)e.getValue()).append(";"));
                }
            }
            return calculateContentChecksum(checksumPayload.toString());
        }
        return null;
    }


    protected String calculateContentChecksum(String checksumPayload)
    {
        return DigestUtils.md5Hex(Objects.<String>requireNonNull(checksumPayload));
    }
}
