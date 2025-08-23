package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.data.AbstractAsSortConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.util.MergeMap;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;

public class AsSortsAddAfterMergeStrategy extends AbstractAsSortsMergeStrategy
{
    public void mergeSorts(AsSearchProfileResult source, AsSearchProfileResult target)
    {
        mergeAfter(source.getPromotedSorts(), target.getPromotedSorts(), target.getSorts(), target.getExcludedSorts());
        mergeAfter(source.getSorts(), target.getSorts(), target.getPromotedSorts(), target.getExcludedSorts());
        mergeAfter(source.getExcludedSorts(), target.getExcludedSorts(), target.getPromotedSorts(), target.getSorts());
    }


    protected <V extends AbstractAsSortConfiguration, C1 extends AbstractAsSortConfiguration, C2 extends AbstractAsSortConfiguration> void mergeAfter(Map<String, AsConfigurationHolder<V, AbstractAsSortConfiguration>> source, Map<String, AsConfigurationHolder<V, AbstractAsSortConfiguration>> target,
                    Map<String, AsConfigurationHolder<C1, AbstractAsSortConfiguration>> cleanupTarget1, Map<String, AsConfigurationHolder<C2, AbstractAsSortConfiguration>> cleanupTarget2)
    {
        if(MapUtils.isEmpty(source))
        {
            return;
        }
        MergeMap<String, AsConfigurationHolder<V, AbstractAsSortConfiguration>> mergeSource = (MergeMap<String, AsConfigurationHolder<V, AbstractAsSortConfiguration>>)source;
        MergeMap<String, AsConfigurationHolder<V, AbstractAsSortConfiguration>> mergeTarget = (MergeMap<String, AsConfigurationHolder<V, AbstractAsSortConfiguration>>)target;
        mergeTarget.mergeAfter(mergeSource, (key, replacedConfiguration, configuration) -> {
            AsConfigurationHolder<V, AbstractAsSortConfiguration> newConfiguration = cloneConfigurationHolder(configuration);
            updateReplacedConfigurations(newConfiguration, (AsConfigurationHolder)cleanupTarget1.remove(key));
            updateReplacedConfigurations(newConfiguration, (AsConfigurationHolder)cleanupTarget2.remove(key));
            updateReplacedConfigurations(newConfiguration, replacedConfiguration);
            return newConfiguration;
        });
    }
}
