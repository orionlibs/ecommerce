package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.data.AbstractAsSortConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.enums.AsSortsMergeMode;
import de.hybris.platform.adaptivesearch.util.MergeMap;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;

public class AsSortsReplaceMergeStrategy extends AbstractAsSortsMergeStrategy
{
    public void mergeSorts(AsSearchProfileResult source, AsSearchProfileResult target)
    {
        target.setSortsMergeMode(AsSortsMergeMode.REPLACE);
        replace(source.getPromotedSorts(), target.getPromotedSorts());
        replace(source.getSorts(), target.getSorts());
        replace(source.getExcludedSorts(), target.getExcludedSorts());
    }


    protected <V extends AbstractAsSortConfiguration> void replace(Map<String, AsConfigurationHolder<V, AbstractAsSortConfiguration>> source, Map<String, AsConfigurationHolder<V, AbstractAsSortConfiguration>> target)
    {
        target.clear();
        if(MapUtils.isEmpty(source))
        {
            return;
        }
        MergeMap<String, AsConfigurationHolder<V, AbstractAsSortConfiguration>> mergeSource = (MergeMap<String, AsConfigurationHolder<V, AbstractAsSortConfiguration>>)source;
        MergeMap<String, AsConfigurationHolder<V, AbstractAsSortConfiguration>> mergeTarget = (MergeMap<String, AsConfigurationHolder<V, AbstractAsSortConfiguration>>)target;
        mergeTarget.mergeAfter(mergeSource, (key, replacedConfiguration, configuration) -> cloneConfigurationHolder(configuration));
    }
}
