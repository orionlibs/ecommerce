package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.data.AbstractAsFacetConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.util.MergeMap;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;

public class AsFacetsAddAfterMergeStrategy extends AbstractAsFacetsMergeStrategy
{
    public void mergeFacets(AsSearchProfileResult source, AsSearchProfileResult target)
    {
        mergeAfter(source.getPromotedFacets(), target.getPromotedFacets(), target.getFacets(), target.getExcludedFacets());
        mergeAfter(source.getFacets(), target.getFacets(), target.getPromotedFacets(), target.getExcludedFacets());
        mergeAfter(source.getExcludedFacets(), target.getExcludedFacets(), target.getPromotedFacets(), target.getFacets());
    }


    protected <V extends AbstractAsFacetConfiguration, C1 extends AbstractAsFacetConfiguration, C2 extends AbstractAsFacetConfiguration> void mergeAfter(Map<String, AsConfigurationHolder<V, AbstractAsFacetConfiguration>> source,
                    Map<String, AsConfigurationHolder<V, AbstractAsFacetConfiguration>> target, Map<String, AsConfigurationHolder<C1, AbstractAsFacetConfiguration>> cleanupTarget1, Map<String, AsConfigurationHolder<C2, AbstractAsFacetConfiguration>> cleanupTarget2)
    {
        if(MapUtils.isEmpty(source))
        {
            return;
        }
        MergeMap<String, AsConfigurationHolder<V, AbstractAsFacetConfiguration>> mergeSource = (MergeMap<String, AsConfigurationHolder<V, AbstractAsFacetConfiguration>>)source;
        MergeMap<String, AsConfigurationHolder<V, AbstractAsFacetConfiguration>> mergeTarget = (MergeMap<String, AsConfigurationHolder<V, AbstractAsFacetConfiguration>>)target;
        mergeTarget.mergeAfter(mergeSource, (key, replacedConfiguration, configuration) -> {
            AsConfigurationHolder<V, AbstractAsFacetConfiguration> newConfiguration = cloneConfigurationHolder(configuration);
            updateReplacedConfigurations(newConfiguration, (AsConfigurationHolder)cleanupTarget1.remove(key));
            updateReplacedConfigurations(newConfiguration, (AsConfigurationHolder)cleanupTarget2.remove(key));
            updateReplacedConfigurations(newConfiguration, replacedConfiguration);
            return newConfiguration;
        });
    }
}
