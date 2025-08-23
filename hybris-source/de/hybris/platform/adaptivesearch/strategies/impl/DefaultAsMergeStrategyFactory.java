package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.enums.AsBoostItemsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsBoostRulesMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsFacetsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsGroupMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsSortsMergeMode;
import de.hybris.platform.adaptivesearch.strategies.AsBoostItemsMergeStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsBoostRulesMergeStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsFacetsMergeStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsGroupMergeStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsMergeStrategyFactory;
import de.hybris.platform.adaptivesearch.strategies.AsSortsMergeStrategy;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsMergeStrategyFactory implements AsMergeStrategyFactory
{
    private Map<AsFacetsMergeMode, AsFacetsMergeStrategy> facetsMergeModeMapping;
    private Map<AsBoostItemsMergeMode, AsBoostItemsMergeStrategy> boostItemsMergeModeMapping;
    private Map<AsBoostRulesMergeMode, AsBoostRulesMergeStrategy> boostRulesMergeModeMapping;
    private Map<AsSortsMergeMode, AsSortsMergeStrategy> sortsMergeModeMapping;
    private Map<AsGroupMergeMode, AsGroupMergeStrategy> groupMergeModeMapping;


    public AsFacetsMergeStrategy getFacetsMergeStrategy(AsFacetsMergeMode mergeMode)
    {
        return this.facetsMergeModeMapping.get(mergeMode);
    }


    public AsBoostItemsMergeStrategy getBoostItemsMergeStrategy(AsBoostItemsMergeMode mergeMode)
    {
        return this.boostItemsMergeModeMapping.get(mergeMode);
    }


    public AsBoostRulesMergeStrategy getBoostRulesMergeStrategy(AsBoostRulesMergeMode mergeMode)
    {
        return this.boostRulesMergeModeMapping.get(mergeMode);
    }


    public AsSortsMergeStrategy getSortsMergeStrategy(AsSortsMergeMode mergeMode)
    {
        return this.sortsMergeModeMapping.get(mergeMode);
    }


    public AsGroupMergeStrategy getGroupMergeStrategy(AsGroupMergeMode mergeMode)
    {
        return this.groupMergeModeMapping.get(mergeMode);
    }


    public Map<AsFacetsMergeMode, AsFacetsMergeStrategy> getFacetsMergeModeMapping()
    {
        return this.facetsMergeModeMapping;
    }


    @Required
    public void setFacetsMergeModeMapping(Map<AsFacetsMergeMode, AsFacetsMergeStrategy> facetsMergeModeMapping)
    {
        this.facetsMergeModeMapping = facetsMergeModeMapping;
    }


    public Map<AsBoostItemsMergeMode, AsBoostItemsMergeStrategy> getBoostItemsMergeModeMapping()
    {
        return this.boostItemsMergeModeMapping;
    }


    @Required
    public void setBoostItemsMergeModeMapping(Map<AsBoostItemsMergeMode, AsBoostItemsMergeStrategy> resultMergeModeMapping)
    {
        this.boostItemsMergeModeMapping = resultMergeModeMapping;
    }


    public Map<AsBoostRulesMergeMode, AsBoostRulesMergeStrategy> getBoostRulesMergeModeMapping()
    {
        return this.boostRulesMergeModeMapping;
    }


    @Required
    public void setBoostRulesMergeModeMapping(Map<AsBoostRulesMergeMode, AsBoostRulesMergeStrategy> boostMergeModeMapping)
    {
        this.boostRulesMergeModeMapping = boostMergeModeMapping;
    }


    public Map<AsSortsMergeMode, AsSortsMergeStrategy> getSortsMergeModeMapping()
    {
        return this.sortsMergeModeMapping;
    }


    @Required
    public void setSortsMergeModeMapping(Map<AsSortsMergeMode, AsSortsMergeStrategy> sortsMergeModeMapping)
    {
        this.sortsMergeModeMapping = sortsMergeModeMapping;
    }


    public Map<AsGroupMergeMode, AsGroupMergeStrategy> getGroupMergeModeMapping()
    {
        return this.groupMergeModeMapping;
    }


    @Required
    public void setGroupMergeModeMapping(Map<AsGroupMergeMode, AsGroupMergeStrategy> groupMergeModeMapping)
    {
        this.groupMergeModeMapping = groupMergeModeMapping;
    }
}
