package de.hybris.platform.ruleengine.init;

import java.util.Collection;
import org.apache.commons.lang3.tuple.Pair;

public interface RulesFilter<T extends de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel>
{
    Pair<Collection<T>, Collection<T>> apply(Collection<String> paramCollection);


    Pair<Collection<T>, Collection<T>> apply(Collection<String> paramCollection, Long paramLong);
}
