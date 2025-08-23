package de.hybris.platform.droolsruleengineservices.interceptors;

import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import java.util.function.BiPredicate;
import org.apache.commons.lang.builder.EqualsBuilder;

public class DroolsRuleSameNameAndPackageBiPredicate implements BiPredicate<DroolsRuleModel, DroolsRuleModel>
{
    public boolean test(DroolsRuleModel rule1, DroolsRuleModel rule2)
    {
        return (
                        !(new EqualsBuilder()).append(rule1, rule2).isEquals() && ((new EqualsBuilder())
                                        .append(rule1.getCode(), rule2.getCode())
                                        .append(rule1.getVersion(), rule2.getVersion())
                                        .append(rule1.getCurrentVersion(), rule2.getCurrentVersion())
                                        .isEquals() || (new EqualsBuilder())
                                        .append(rule1.getUuid(), rule2.getUuid())
                                        .append(rule1.getRulePackage(), rule2.getRulePackage())
                                        .append(rule1.getVersion(), rule2.getVersion())
                                        .append(rule1.getCurrentVersion(), rule2.getCurrentVersion())
                                        .isEquals()));
    }
}
