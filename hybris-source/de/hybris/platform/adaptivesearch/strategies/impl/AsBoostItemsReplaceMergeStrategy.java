package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.data.AbstractAsBoostItemConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.enums.AsBoostItemsMergeMode;
import de.hybris.platform.adaptivesearch.util.MergeMap;
import de.hybris.platform.core.PK;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;

public class AsBoostItemsReplaceMergeStrategy extends AbstractAsBoostItemsMergeStrategy
{
    public void mergeBoostItems(AsSearchProfileResult source, AsSearchProfileResult target)
    {
        target.setBoostItemsMergeMode(AsBoostItemsMergeMode.REPLACE);
        replace(source.getPromotedItems(), target.getPromotedItems());
        replace(source.getExcludedItems(), target.getExcludedItems());
    }


    protected <V extends AbstractAsBoostItemConfiguration> void replace(Map<PK, AsConfigurationHolder<V, AbstractAsBoostItemConfiguration>> source, Map<PK, AsConfigurationHolder<V, AbstractAsBoostItemConfiguration>> target)
    {
        target.clear();
        if(MapUtils.isEmpty(source))
        {
            return;
        }
        MergeMap<PK, AsConfigurationHolder<V, AbstractAsBoostItemConfiguration>> mergeSource = (MergeMap<PK, AsConfigurationHolder<V, AbstractAsBoostItemConfiguration>>)source;
        MergeMap<PK, AsConfigurationHolder<V, AbstractAsBoostItemConfiguration>> mergeTarget = (MergeMap<PK, AsConfigurationHolder<V, AbstractAsBoostItemConfiguration>>)target;
        mergeTarget.mergeAfter(mergeSource, (key, replacedConfiguration, configuration) -> cloneConfigurationHolder(configuration));
    }
}
