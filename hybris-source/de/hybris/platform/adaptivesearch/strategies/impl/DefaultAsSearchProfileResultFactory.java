package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.data.AbstractAsBoostItemConfiguration;
import de.hybris.platform.adaptivesearch.data.AbstractAsBoostRuleConfiguration;
import de.hybris.platform.adaptivesearch.data.AbstractAsFacetConfiguration;
import de.hybris.platform.adaptivesearch.data.AbstractAsSortConfiguration;
import de.hybris.platform.adaptivesearch.data.AsBoostRule;
import de.hybris.platform.adaptivesearch.data.AsConfigurableSearchConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsExcludedFacet;
import de.hybris.platform.adaptivesearch.data.AsExcludedItem;
import de.hybris.platform.adaptivesearch.data.AsExcludedSort;
import de.hybris.platform.adaptivesearch.data.AsFacet;
import de.hybris.platform.adaptivesearch.data.AsGroup;
import de.hybris.platform.adaptivesearch.data.AsPromotedFacet;
import de.hybris.platform.adaptivesearch.data.AsPromotedItem;
import de.hybris.platform.adaptivesearch.data.AsPromotedSort;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.data.AsSort;
import de.hybris.platform.adaptivesearch.enums.AsBoostItemsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsBoostRulesMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsFacetsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsGroupMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsSortsMergeMode;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileResultFactory;
import de.hybris.platform.adaptivesearch.util.MergeMap;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsSearchProfileResultFactory implements AsSearchProfileResultFactory
{
    protected static final FacetComparator FACET_COMPARATOR = new FacetComparator();
    protected static final SortComparator SORT_COMPARATOR = new SortComparator();
    private ConfigurationService configurationService;


    public AsSearchProfileResult createResult()
    {
        AsSearchProfileResult result = new AsSearchProfileResult();
        initializeFacets(result);
        initializeBoostItems(result);
        initializeBoostRules(result);
        initializeSorts(result);
        initializeGroup(result);
        return result;
    }


    protected void initializeFacets(AsSearchProfileResult result)
    {
        AsFacetsMergeMode facetsMergeMode = AsFacetsMergeMode.valueOf(this.configurationService.getConfiguration()
                        .getString("adaptivesearch.merge.facets.default", AsFacetsMergeMode.ADD_AFTER.name()));
        result.setFacetsMergeMode(facetsMergeMode);
        result.setPromotedFacets((Map)new DefaultMergeMap());
        result.setFacets((Map)new DefaultMergeMap((Comparator)FACET_COMPARATOR));
        result.setExcludedFacets((Map)new DefaultMergeMap());
    }


    protected void initializeBoostItems(AsSearchProfileResult result)
    {
        AsBoostItemsMergeMode boostItemsMergeMode = AsBoostItemsMergeMode.valueOf(this.configurationService.getConfiguration()
                        .getString("adaptivesearch.merge.boostitems.default", AsBoostItemsMergeMode.ADD_AFTER.name()));
        result.setBoostItemsMergeMode(boostItemsMergeMode);
        result.setPromotedItems((Map)new DefaultMergeMap());
        result.setExcludedItems((Map)new DefaultMergeMap());
    }


    protected void initializeBoostRules(AsSearchProfileResult result)
    {
        AsBoostRulesMergeMode boostRulesMergeMode = AsBoostRulesMergeMode.valueOf(this.configurationService.getConfiguration()
                        .getString("adaptivesearch.merge.boostrules.default", AsBoostRulesMergeMode.ADD.name()));
        result.setBoostRulesMergeMode(boostRulesMergeMode);
        result.setBoostRules(new ArrayList());
    }


    protected void initializeSorts(AsSearchProfileResult result)
    {
        AsSortsMergeMode sortsMergetMode = AsSortsMergeMode.valueOf(this.configurationService.getConfiguration()
                        .getString("adaptivesearch.merge.sorts.default", AsSortsMergeMode.ADD_AFTER.name()));
        result.setSortsMergeMode(sortsMergetMode);
        result.setPromotedSorts((Map)new DefaultMergeMap());
        result.setSorts((Map)new DefaultMergeMap((Comparator)SORT_COMPARATOR));
        result.setExcludedSorts((Map)new DefaultMergeMap());
    }


    protected void initializeGroup(AsSearchProfileResult result)
    {
        AsGroupMergeMode groupMergeMode = AsGroupMergeMode.valueOf(this.configurationService.getConfiguration()
                        .getString("adaptivesearch.merge.group.default", AsGroupMergeMode.INHERIT.name()));
        result.setGroupMergeMode(groupMergeMode);
        result.setGroup(null);
    }


    public AsSearchProfileResult createResultFromSearchConfiguration(AsConfigurableSearchConfiguration searchConfiguration)
    {
        AsSearchProfileResult result = createResult();
        populateFacetsFromSearchConfiguration(result, searchConfiguration);
        populateBoostItemsFromSearchConfiguration(result, searchConfiguration);
        populateBoostRulesFromSearchConfiguration(result, searchConfiguration);
        populateSortsFromSearchConfiguration(result, searchConfiguration);
        populateGroupFromSearchConfiguration(result, searchConfiguration);
        return result;
    }


    protected void populateFacetsFromSearchConfiguration(AsSearchProfileResult result, AsConfigurableSearchConfiguration searchConfiguration)
    {
        result.setFacetsMergeMode(searchConfiguration.getFacetsMergeMode());
        MergeMap<String, AsConfigurationHolder<AsPromotedFacet, AbstractAsFacetConfiguration>> promotedFacets = (MergeMap<String, AsConfigurationHolder<AsPromotedFacet, AbstractAsFacetConfiguration>>)result.getPromotedFacets();
        for(AsPromotedFacet promotedFacet : searchConfiguration.getPromotedFacets())
        {
            promotedFacets.mergeAfter(promotedFacet.getIndexProperty(), createConfigurationHolder(promotedFacet));
        }
        MergeMap<String, AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration>> facets = (MergeMap<String, AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration>>)result.getFacets();
        for(AsFacet facet : searchConfiguration.getFacets())
        {
            facets.mergeAfter(facet.getIndexProperty(), createConfigurationHolder(facet));
        }
        MergeMap<String, AsConfigurationHolder<AsExcludedFacet, AbstractAsFacetConfiguration>> excludedFacets = (MergeMap<String, AsConfigurationHolder<AsExcludedFacet, AbstractAsFacetConfiguration>>)result.getExcludedFacets();
        for(AsExcludedFacet excludedFacet : searchConfiguration.getExcludedFacets())
        {
            excludedFacets.mergeAfter(excludedFacet.getIndexProperty(), createConfigurationHolder(excludedFacet));
        }
    }


    protected void populateBoostItemsFromSearchConfiguration(AsSearchProfileResult result, AsConfigurableSearchConfiguration searchConfiguration)
    {
        result.setBoostItemsMergeMode(searchConfiguration.getBoostItemsMergeMode());
        MergeMap<PK, AsConfigurationHolder<AsPromotedItem, AbstractAsBoostItemConfiguration>> promotedItems = (MergeMap<PK, AsConfigurationHolder<AsPromotedItem, AbstractAsBoostItemConfiguration>>)result.getPromotedItems();
        for(AsPromotedItem promotedItem : searchConfiguration.getPromotedItems())
        {
            promotedItems.mergeAfter(promotedItem.getItemPk(), createConfigurationHolder(promotedItem));
        }
        MergeMap<PK, AsConfigurationHolder<AsExcludedItem, AbstractAsBoostItemConfiguration>> excludedItems = (MergeMap<PK, AsConfigurationHolder<AsExcludedItem, AbstractAsBoostItemConfiguration>>)result.getExcludedItems();
        for(AsExcludedItem excludedItem : searchConfiguration.getExcludedItems())
        {
            excludedItems.mergeAfter(excludedItem.getItemPk(), createConfigurationHolder(excludedItem));
        }
    }


    protected void populateBoostRulesFromSearchConfiguration(AsSearchProfileResult result, AsConfigurableSearchConfiguration searchConfiguration)
    {
        result.setBoostRulesMergeMode(searchConfiguration.getBoostRulesMergeMode());
        List<AsConfigurationHolder<AsBoostRule, AbstractAsBoostRuleConfiguration>> boostRules = result.getBoostRules();
        for(AsBoostRule boostRule : searchConfiguration.getBoostRules())
        {
            boostRules.add(createConfigurationHolder(boostRule));
        }
    }


    protected void populateSortsFromSearchConfiguration(AsSearchProfileResult result, AsConfigurableSearchConfiguration searchConfiguration)
    {
        result.setSortsMergeMode(searchConfiguration.getSortsMergeMode());
        MergeMap<String, AsConfigurationHolder<AsPromotedSort, AbstractAsSortConfiguration>> promotedSorts = (MergeMap<String, AsConfigurationHolder<AsPromotedSort, AbstractAsSortConfiguration>>)result.getPromotedSorts();
        for(AsPromotedSort promotedSort : searchConfiguration.getPromotedSorts())
        {
            promotedSorts.mergeAfter(promotedSort.getCode(), createConfigurationHolder(promotedSort));
        }
        MergeMap<String, AsConfigurationHolder<AsSort, AbstractAsSortConfiguration>> sorts = (MergeMap<String, AsConfigurationHolder<AsSort, AbstractAsSortConfiguration>>)result.getSorts();
        for(AsSort sort : searchConfiguration.getSorts())
        {
            sorts.mergeAfter(sort.getCode(), createConfigurationHolder(sort));
        }
        MergeMap<String, AsConfigurationHolder<AsExcludedSort, AbstractAsSortConfiguration>> excludedSorts = (MergeMap<String, AsConfigurationHolder<AsExcludedSort, AbstractAsSortConfiguration>>)result.getExcludedSorts();
        for(AsExcludedSort excludedSort : searchConfiguration.getExcludedSorts())
        {
            excludedSorts.mergeAfter(excludedSort.getCode(), createConfigurationHolder(excludedSort));
        }
    }


    protected void populateGroupFromSearchConfiguration(AsSearchProfileResult result, AsConfigurableSearchConfiguration searchConfiguration)
    {
        AsGroup group = new AsGroup();
        group.setSearchProfileCode(searchConfiguration.getSearchProfileCode());
        group.setSearchConfigurationUid(searchConfiguration.getUid());
        group.setCatalogVersion(searchConfiguration.getCatalogVersion());
        group.setUid(searchConfiguration.getUid());
        group.setExpression(searchConfiguration.getGroupExpression());
        group.setLimit(searchConfiguration.getGroupLimit());
        result.setGroupMergeMode(searchConfiguration.getGroupMergeMode());
        result.setGroup(createConfigurationHolder(group));
    }


    public <T, R> AsConfigurationHolder<T, R> createConfigurationHolder(T configuration)
    {
        AsConfigurationHolder<T, R> configurationHolder = new AsConfigurationHolder();
        configurationHolder.setConfiguration(configuration);
        configurationHolder.setReplacedConfigurations(new ArrayList());
        configurationHolder.setRank(0);
        return configurationHolder;
    }


    public <T, R> AsConfigurationHolder<T, R> createConfigurationHolder(T configuration, Object data)
    {
        AsConfigurationHolder<T, R> configurationHolder = new AsConfigurationHolder();
        configurationHolder.setConfiguration(configuration);
        configurationHolder.setReplacedConfigurations(new ArrayList());
        configurationHolder.setRank(0);
        configurationHolder.setData(data);
        return configurationHolder;
    }


    public <T, R> AsConfigurationHolder<T, R> cloneConfigurationHolder(AsConfigurationHolder<T, R> configurationHolder)
    {
        AsConfigurationHolder<T, R> newConfigurationHolder = new AsConfigurationHolder();
        newConfigurationHolder.setConfiguration(configurationHolder.getConfiguration());
        newConfigurationHolder.setReplacedConfigurations((List)ObjectUtils.clone(configurationHolder.getReplacedConfigurations()));
        newConfigurationHolder.setRank(configurationHolder.getRank());
        newConfigurationHolder.setData(configurationHolder.getData());
        return newConfigurationHolder;
    }


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
