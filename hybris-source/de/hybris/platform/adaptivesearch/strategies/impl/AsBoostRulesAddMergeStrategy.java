package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.data.AbstractAsBoostRuleConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

public class AsBoostRulesAddMergeStrategy extends AbstractAsBoostRulesMergeStrategy
{
    public void mergeBoostRules(AsSearchProfileResult source, AsSearchProfileResult target)
    {
        mergeAfter(source.getBoostRules(), target.getBoostRules());
    }


    protected <V extends de.hybris.platform.adaptivesearch.data.AsBoostRule> void mergeAfter(List<AsConfigurationHolder<V, AbstractAsBoostRuleConfiguration>> source, List<AsConfigurationHolder<V, AbstractAsBoostRuleConfiguration>> target)
    {
        if(CollectionUtils.isEmpty(source))
        {
            return;
        }
        for(AsConfigurationHolder<V, AbstractAsBoostRuleConfiguration> configuration : source)
        {
            AsConfigurationHolder<V, AbstractAsBoostRuleConfiguration> newConfiguration = getAsSearchProfileResultFactory().cloneConfigurationHolder(configuration);
            target.add(newConfiguration);
        }
    }
}
