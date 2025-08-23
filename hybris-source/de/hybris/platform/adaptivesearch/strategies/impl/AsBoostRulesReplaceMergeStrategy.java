package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.data.AbstractAsBoostRuleConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.enums.AsBoostRulesMergeMode;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

public class AsBoostRulesReplaceMergeStrategy extends AbstractAsBoostRulesMergeStrategy
{
    public void mergeBoostRules(AsSearchProfileResult source, AsSearchProfileResult target)
    {
        target.setBoostRulesMergeMode(AsBoostRulesMergeMode.REPLACE);
        replace(source.getBoostRules(), target.getBoostRules());
    }


    protected <V extends de.hybris.platform.adaptivesearch.data.AsBoostRule> void replace(List<AsConfigurationHolder<V, AbstractAsBoostRuleConfiguration>> source, List<AsConfigurationHolder<V, AbstractAsBoostRuleConfiguration>> target)
    {
        target.clear();
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
