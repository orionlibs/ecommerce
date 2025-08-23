package de.hybris.platform.ruleengine.init;

import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import java.util.Collection;
import org.apache.commons.lang3.tuple.Pair;

public interface ContentMatchRulesFilter extends RulesFilter<DroolsRuleModel>
{
    Pair<Collection<DroolsRuleModel>, Collection<DroolsRuleModel>> apply(Collection<String> paramCollection);


    Pair<Collection<DroolsRuleModel>, Collection<DroolsRuleModel>> apply(Collection<String> paramCollection, Long paramLong);
}
