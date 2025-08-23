package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.data.AbstractAsFacetConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.enums.AsFacetsMergeMode;
import de.hybris.platform.adaptivesearch.util.MergeMap;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;

public class AsFacetsReplaceMergeStrategy extends AbstractAsFacetsMergeStrategy
{
    public void mergeFacets(AsSearchProfileResult source, AsSearchProfileResult target)
    {
        target.setFacetsMergeMode(AsFacetsMergeMode.REPLACE);
        replace(source.getPromotedFacets(), target.getPromotedFacets());
        replace(source.getFacets(), target.getFacets());
        replace(source.getExcludedFacets(), target.getExcludedFacets());
    }


    protected <V extends AbstractAsFacetConfiguration> void replace(Map<String, AsConfigurationHolder<V, AbstractAsFacetConfiguration>> source, Map<String, AsConfigurationHolder<V, AbstractAsFacetConfiguration>> target)
    {
        target.clear();
        if(MapUtils.isEmpty(source))
        {
            return;
        }
        MergeMap<String, AsConfigurationHolder<V, AbstractAsFacetConfiguration>> mergeSource = (MergeMap<String, AsConfigurationHolder<V, AbstractAsFacetConfiguration>>)source;
        MergeMap<String, AsConfigurationHolder<V, AbstractAsFacetConfiguration>> mergeTarget = (MergeMap<String, AsConfigurationHolder<V, AbstractAsFacetConfiguration>>)target;
        mergeTarget.mergeAfter(mergeSource, (key, replacedConfiguration, configuration) -> cloneConfigurationHolder(configuration));
    }
}
