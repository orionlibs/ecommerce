package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.data.AbstractAsBoostItemConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.util.MergeMap;
import de.hybris.platform.core.PK;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;

public class AsBoostItemsAddAfterMergeStrategy extends AbstractAsBoostItemsMergeStrategy
{
    public void mergeBoostItems(AsSearchProfileResult source, AsSearchProfileResult target)
    {
        mergeAfter(source.getPromotedItems(), target.getPromotedItems(), target.getExcludedItems());
        mergeAfter(source.getExcludedItems(), target.getExcludedItems(), target.getPromotedItems());
    }


    protected <V extends AbstractAsBoostItemConfiguration, C extends AbstractAsBoostItemConfiguration> void mergeAfter(Map<PK, AsConfigurationHolder<V, AbstractAsBoostItemConfiguration>> source, Map<PK, AsConfigurationHolder<V, AbstractAsBoostItemConfiguration>> target,
                    Map<PK, AsConfigurationHolder<C, AbstractAsBoostItemConfiguration>> cleanupTarget)
    {
        if(MapUtils.isEmpty(source))
        {
            return;
        }
        MergeMap<PK, AsConfigurationHolder<V, AbstractAsBoostItemConfiguration>> mergeSource = (MergeMap<PK, AsConfigurationHolder<V, AbstractAsBoostItemConfiguration>>)source;
        MergeMap<PK, AsConfigurationHolder<V, AbstractAsBoostItemConfiguration>> mergeTarget = (MergeMap<PK, AsConfigurationHolder<V, AbstractAsBoostItemConfiguration>>)target;
        mergeTarget.mergeAfter(mergeSource, (key, replacedConfiguration, configuration) -> {
            AsConfigurationHolder<V, AbstractAsBoostItemConfiguration> newConfiguration = cloneConfigurationHolder(configuration);
            updateReplacedConfigurations(newConfiguration, (AsConfigurationHolder)cleanupTarget.remove(key));
            updateReplacedConfigurations(newConfiguration, replacedConfiguration);
            return newConfiguration;
        });
    }
}
