package de.hybris.platform.adaptivesearchsolr.listeners;

import de.hybris.platform.adaptivesearch.context.AsKeyword;
import de.hybris.platform.adaptivesearch.context.AsKeywordModifier;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.context.impl.DefaultAsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsBoostItemConfiguration;
import de.hybris.platform.adaptivesearch.data.AbstractAsBoostRuleConfiguration;
import de.hybris.platform.adaptivesearch.data.AbstractAsFacetConfiguration;
import de.hybris.platform.adaptivesearch.data.AbstractAsFacetValueConfiguration;
import de.hybris.platform.adaptivesearch.data.AbstractAsSortConfiguration;
import de.hybris.platform.adaptivesearch.data.AsBoostRule;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsExcludedFacetValue;
import de.hybris.platform.adaptivesearch.data.AsExcludedItem;
import de.hybris.platform.adaptivesearch.data.AsFacet;
import de.hybris.platform.adaptivesearch.data.AsGroup;
import de.hybris.platform.adaptivesearch.data.AsPromotedFacet;
import de.hybris.platform.adaptivesearch.data.AsPromotedFacetValue;
import de.hybris.platform.adaptivesearch.data.AsPromotedItem;
import de.hybris.platform.adaptivesearch.data.AsPromotedSort;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileActivationGroup;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.data.AsSort;
import de.hybris.platform.adaptivesearch.data.AsSortExpression;
import de.hybris.platform.adaptivesearch.enums.AsBoostItemsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsBoostRulesMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsFacetsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsGroupMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsSortOrder;
import de.hybris.platform.adaptivesearch.enums.AsSortsMergeMode;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileActivationService;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileCalculationService;
import de.hybris.platform.adaptivesearch.strategies.AsUidGenerator;
import de.hybris.platform.adaptivesearch.util.MergeMap;
import de.hybris.platform.adaptivesearchsolr.strategies.SolrAsCatalogVersionResolver;
import de.hybris.platform.adaptivesearchsolr.strategies.SolrAsCategoryPathResolver;
import de.hybris.platform.adaptivesearchsolr.strategies.SolrAsTypeMappingRegistry;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeSortField;
import de.hybris.platform.solrfacetsearch.search.BoostField;
import de.hybris.platform.solrfacetsearch.search.Document;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.FacetField;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.GroupCommandField;
import de.hybris.platform.solrfacetsearch.search.Keyword;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class SolrAsSearchProfileCalculationListener implements FacetSearchListener
{
    private static final Logger LOG = LoggerFactory.getLogger(SolrAsSearchProfileCalculationListener.class);
    private SolrAsCatalogVersionResolver solrAsCatalogVersionResolver;
    private SolrAsCategoryPathResolver solrAsCategoryPathResolver;
    private SolrAsTypeMappingRegistry solrAsTypeMappingRegistry;
    private AsSearchProfileActivationService asSearchProfileActivationService;
    private AsSearchProfileCalculationService asSearchProfileCalculationService;
    private CommonI18NService commonI18NService;


    public void beforeSearch(FacetSearchContext facetSearchContext) throws FacetSearchException
    {
        if(facetSearchContext.getFacetSearchConfig().getSearchConfig().isLegacyMode())
        {
            LOG.warn("Adaptive search does not support search legacy mode: {}/{}", facetSearchContext
                            .getFacetSearchConfig().getName(), facetSearchContext.getIndexedType().getIdentifier());
            return;
        }
        SearchQuery searchQuery = facetSearchContext.getSearchQuery();
        String indexConfiguration = facetSearchContext.getFacetSearchConfig().getName();
        String indexType = facetSearchContext.getIndexedType().getIdentifier();
        List<CatalogVersionModel> catalogVersions = this.solrAsCatalogVersionResolver.resolveCatalogVersions(searchQuery);
        List<CatalogVersionModel> sessionCatalogVersions = (facetSearchContext.getParentSessionCatalogVersions() == null) ? Collections.<CatalogVersionModel>emptyList() : new ArrayList<>(facetSearchContext.getParentSessionCatalogVersions());
        List<CategoryModel> categoryPath = this.solrAsCategoryPathResolver.resolveCategoryPath(searchQuery, catalogVersions);
        LanguageModel language = resolveLanguage(searchQuery);
        CurrencyModel currency = resolveCurrency(searchQuery);
        DefaultAsSearchProfileContext defaultAsSearchProfileContext = DefaultAsSearchProfileContext.builder().withQueryContexts(searchQuery.getQueryContexts()).withIndexConfiguration(indexConfiguration).withIndexType(indexType).withCatalogVersions(catalogVersions)
                        .withSessionCatalogVersions(sessionCatalogVersions).withCategoryPath(categoryPath).withLanguage(language).withCurrency(currency).build();
        if(StringUtils.isNotBlank(searchQuery.getUserQuery()))
        {
            defaultAsSearchProfileContext.setQuery(searchQuery.getUserQuery());
            defaultAsSearchProfileContext.setKeywords(convertKeywords(searchQuery.getKeywords()));
        }
        List<AsSearchProfileActivationGroup> groups = this.asSearchProfileActivationService.getSearchProfileActivationGroupsForContext((AsSearchProfileContext)defaultAsSearchProfileContext);
        if(CollectionUtils.isNotEmpty(groups))
        {
            AsSearchProfileResult searchQueryResult = createResultFromFacetSearchContext((AsSearchProfileContext)defaultAsSearchProfileContext, facetSearchContext);
            AsSearchProfileResult result = this.asSearchProfileCalculationService.calculateGroups((AsSearchProfileContext)defaultAsSearchProfileContext, searchQueryResult, groups);
            Map<String, Object> contextAttributes = facetSearchContext.getAttributes();
            contextAttributes.put("adaptiveSearchContext", defaultAsSearchProfileContext);
            contextAttributes.put("adaptiveSearchResult", result);
            applyResult(facetSearchContext, result);
        }
    }


    public void afterSearch(FacetSearchContext facetSearchContext) throws FacetSearchException
    {
        if(facetSearchContext.getFacetSearchConfig().getSearchConfig().isLegacyMode())
        {
            return;
        }
        SearchResult searchResult = facetSearchContext.getSearchResult();
        Map<String, Object> contextAttributes = facetSearchContext.getAttributes();
        Map<String, Object> searchResultAttributes = searchResult.getAttributes();
        AsSearchProfileResult searchProfileResult = (AsSearchProfileResult)contextAttributes.get("adaptiveSearchResult");
        if(searchProfileResult != null)
        {
            for(Document document : searchResult.getDocuments())
            {
                PK pk = extractPkFromDocument(document);
                if(searchProfileResult.getPromotedItems().containsKey(pk) && searchResult.getCurrentNamedSort() != null)
                {
                    if(searchResult.getCurrentNamedSort().isApplyPromotedItems())
                    {
                        document.getTags().add("promoted");
                    }
                    if(searchResult.getCurrentNamedSort().isHighlightPromotedItems())
                    {
                        document.getTags().add("highlighted");
                    }
                }
            }
            for(Facet facet : searchResult.getFacets())
            {
                if(searchProfileResult.getPromotedFacets().containsKey(facet.getName()))
                {
                    facet.getTags().add("promoted");
                }
            }
        }
        searchResultAttributes.put("adaptiveSearchContext", contextAttributes
                        .get("adaptiveSearchContext"));
        searchResultAttributes.put("adaptiveSearchResult", searchProfileResult);
    }


    public void afterSearchError(FacetSearchContext facetSearchContext) throws FacetSearchException
    {
    }


    protected LanguageModel resolveLanguage(SearchQuery searchQuery)
    {
        if(searchQuery == null || searchQuery.getLanguage() == null)
        {
            return null;
        }
        return this.commonI18NService.getLanguage(searchQuery.getLanguage());
    }


    protected CurrencyModel resolveCurrency(SearchQuery searchQuery)
    {
        if(searchQuery == null || searchQuery.getCurrency() == null)
        {
            return null;
        }
        return this.commonI18NService.getCurrency(searchQuery.getCurrency());
    }


    protected List<AsKeyword> convertKeywords(List<Keyword> keywords)
    {
        if(CollectionUtils.isEmpty(keywords))
        {
            return Collections.emptyList();
        }
        return (List<AsKeyword>)keywords.stream().map(this::convertKeyword).collect(Collectors.toList());
    }


    protected AsKeyword convertKeyword(Keyword source)
    {
        AsKeywordModifier[] modifiers = (AsKeywordModifier[])source.getModifiers().stream().map(modifier -> AsKeywordModifier.valueOf(modifier.name())).toArray(x$0 -> new AsKeywordModifier[x$0]);
        return new AsKeyword(source.getValue(), modifiers);
    }


    protected AsSearchProfileResult createResultFromFacetSearchContext(AsSearchProfileContext context, FacetSearchContext facetSearchContext)
    {
        SearchQuery searchQuery = facetSearchContext.getSearchQuery();
        AsSearchProfileResult result = this.asSearchProfileCalculationService.createResult(context);
        result.setFacetsMergeMode(AsFacetsMergeMode.REPLACE);
        result.setBoostRulesMergeMode(AsBoostRulesMergeMode.REPLACE);
        result.setBoostItemsMergeMode(AsBoostItemsMergeMode.REPLACE);
        result.setSortsMergeMode(AsSortsMergeMode.REPLACE);
        result.setGroupMergeMode(AsGroupMergeMode.REPLACE);
        AsUidGenerator asUidGenerator = createUidGenerator();
        if(CollectionUtils.isNotEmpty(searchQuery.getFacets()))
        {
            MergeMap<String, AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration>> facets = (MergeMap<String, AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration>>)result.getFacets();
            searchQuery.getFacets()
                            .forEach(facet -> facets.mergeAfter(facet.getField(), convertFacet(context, facet, asUidGenerator)));
        }
        if(CollectionUtils.isNotEmpty(searchQuery.getBoosts()))
        {
            searchQuery.getBoosts().forEach(boost -> result.getBoostRules().add(convertBoostRule(context, boost, asUidGenerator)));
        }
        MergeMap<PK, AsConfigurationHolder<AsPromotedItem, AbstractAsBoostItemConfiguration>> promotedItems = (MergeMap<PK, AsConfigurationHolder<AsPromotedItem, AbstractAsBoostItemConfiguration>>)result.getPromotedItems();
        if(CollectionUtils.isNotEmpty(searchQuery.getPromotedItems()))
        {
            searchQuery.getPromotedItems().forEach(promotedItemPk -> promotedItems.mergeAfter(promotedItemPk, convertBoostItem(context, promotedItemPk, asUidGenerator, AsPromotedItem::new)));
        }
        MergeMap<PK, AsConfigurationHolder<AsExcludedItem, AbstractAsBoostItemConfiguration>> excludedItems = (MergeMap<PK, AsConfigurationHolder<AsExcludedItem, AbstractAsBoostItemConfiguration>>)result.getExcludedItems();
        if(CollectionUtils.isNotEmpty(searchQuery.getExcludedItems()))
        {
            searchQuery.getExcludedItems().forEach(excludedItemPk -> excludedItems.mergeAfter(excludedItemPk, convertBoostItem(context, excludedItemPk, asUidGenerator, AsExcludedItem::new)));
        }
        MergeMap<String, AsConfigurationHolder<AsSort, AbstractAsSortConfiguration>> sorts = (MergeMap<String, AsConfigurationHolder<AsSort, AbstractAsSortConfiguration>>)result.getSorts();
        if(CollectionUtils.isNotEmpty(facetSearchContext.getAvailableNamedSorts()))
        {
            facetSearchContext.getAvailableNamedSorts()
                            .forEach(sort -> sorts.mergeAfter(sort.getCode(), convertSort(context, sort, asUidGenerator)));
        }
        if(CollectionUtils.isNotEmpty(searchQuery.getGroupCommands()))
        {
            result.setGroup(convertGroup(context, searchQuery.getGroupCommands(), asUidGenerator));
        }
        return result;
    }


    protected AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration> convertFacet(AsSearchProfileContext context, FacetField facetField, AsUidGenerator asUidGenerator)
    {
        AsFacet facet = new AsFacet();
        facet.setUid(asUidGenerator.generateUid());
        facet.setIndexProperty(facetField.getField());
        facet.setPriority((facetField.getPriority() != null) ? facetField.getPriority() : Integer.valueOf(100));
        facet.setValuesDisplayNameProvider(facetField.getDisplayNameProvider());
        facet.setValuesSortProvider(facetField.getSortProvider());
        facet.setFacetType(this.solrAsTypeMappingRegistry.toAsFacetType(facetField.getFacetType()));
        facet.setPromotedValues(convertAll(facetField.getPromotedValues(), value -> (AsPromotedFacetValue)convertFacetValue(value, asUidGenerator, AsPromotedFacetValue::new)));
        facet.setExcludedValues(convertAll(facetField.getExcludedValues(), value -> (AsExcludedFacetValue)convertFacetValue(value, asUidGenerator, AsExcludedFacetValue::new)));
        return this.asSearchProfileCalculationService.createConfigurationHolder(context, facet, facetField);
    }


    protected <T extends AbstractAsFacetValueConfiguration> T convertFacetValue(String value, AsUidGenerator asUidGenerator, Supplier<T> createSupplier)
    {
        AbstractAsFacetValueConfiguration abstractAsFacetValueConfiguration = (AbstractAsFacetValueConfiguration)createSupplier.get();
        abstractAsFacetValueConfiguration.setUid(asUidGenerator.generateUid());
        abstractAsFacetValueConfiguration.setValue(value);
        return (T)abstractAsFacetValueConfiguration;
    }


    protected AsConfigurationHolder<AsBoostRule, AbstractAsBoostRuleConfiguration> convertBoostRule(AsSearchProfileContext context, BoostField boostField, AsUidGenerator asUidGenerator)
    {
        AsBoostRule boost = new AsBoostRule();
        boost.setUid(asUidGenerator.generateUid());
        boost.setIndexProperty(boostField.getField());
        boost.setOperator(this.solrAsTypeMappingRegistry.toAsBoostOperator(boostField.getQueryOperator()));
        boost.setValue(String.valueOf(boostField.getValue()));
        boost.setBoostType(this.solrAsTypeMappingRegistry.toAsBoostType(boostField.getBoostType()));
        boost.setBoost(boostField.getBoostValue());
        return this.asSearchProfileCalculationService.createConfigurationHolder(context, boost, boostField);
    }


    protected <T extends AbstractAsBoostItemConfiguration> AsConfigurationHolder<T, AbstractAsBoostItemConfiguration> convertBoostItem(AsSearchProfileContext context, PK promotedItemPk, AsUidGenerator asUidGenerator, Supplier<T> createSupplier)
    {
        AbstractAsBoostItemConfiguration abstractAsBoostItemConfiguration = (AbstractAsBoostItemConfiguration)createSupplier.get();
        abstractAsBoostItemConfiguration.setUid(asUidGenerator.generateUid());
        abstractAsBoostItemConfiguration.setItemPk(promotedItemPk);
        return this.asSearchProfileCalculationService.createConfigurationHolder(context, abstractAsBoostItemConfiguration);
    }


    protected AsConfigurationHolder<AsSort, AbstractAsSortConfiguration> convertSort(AsSearchProfileContext context, IndexedTypeSort indexedTypeSort, AsUidGenerator asUidGenerator)
    {
        AsSort sort = new AsSort();
        sort.setUid(asUidGenerator.generateUid());
        sort.setCode(indexedTypeSort.getCode());
        sort.setName(indexedTypeSort.getLocalizedName());
        sort.setPriority(Integer.valueOf(100));
        sort.setApplyPromotedItems(indexedTypeSort.isApplyPromotedItems());
        sort.setHighlightPromotedItems(indexedTypeSort.isHighlightPromotedItems());
        sort.setExpressions((List)indexedTypeSort.getFields().stream().map(this::convertSortExpression).collect(Collectors.toList()));
        return this.asSearchProfileCalculationService.createConfigurationHolder(context, sort, indexedTypeSort);
    }


    protected AsSortExpression convertSortExpression(IndexedTypeSortField indexedTypeSortField)
    {
        AsSortExpression asSortExpression = new AsSortExpression();
        asSortExpression.setExpression(indexedTypeSortField.getFieldName());
        asSortExpression.setOrder(indexedTypeSortField.isAscending() ? AsSortOrder.ASCENDING : AsSortOrder.DESCENDING);
        return asSortExpression;
    }


    protected AsConfigurationHolder<AsGroup, AsGroup> convertGroup(AsSearchProfileContext context, List<GroupCommandField> groupCommands, AsUidGenerator asUidGenerator)
    {
        GroupCommandField groupCommand = groupCommands.get(0);
        AsGroup group = new AsGroup();
        group.setUid(asUidGenerator.generateUid());
        group.setExpression(groupCommand.getField());
        group.setLimit(groupCommand.getGroupLimit());
        List<GroupCommandField> data = new ArrayList<>(groupCommands);
        return this.asSearchProfileCalculationService.createConfigurationHolder(context, group, data);
    }


    protected void applyResult(FacetSearchContext facetSearchContext, AsSearchProfileResult result)
    {
        applyFacets(facetSearchContext, result);
        applyBoostItems(facetSearchContext, result);
        applyBoostRules(facetSearchContext, result);
        applySorts(facetSearchContext, result);
        applyGroup(facetSearchContext, result);
    }


    protected void applyFacets(FacetSearchContext facetSearchContext, AsSearchProfileResult result)
    {
        SearchQuery searchQuery = facetSearchContext.getSearchQuery();
        searchQuery.getFacets().clear();
        int priority = Integer.MAX_VALUE;
        MergeMap<String, AsConfigurationHolder<AsPromotedFacet, AbstractAsFacetConfiguration>> promotedFacets = (MergeMap<String, AsConfigurationHolder<AsPromotedFacet, AbstractAsFacetConfiguration>>)result.getPromotedFacets();
        if(MapUtils.isNotEmpty((Map)promotedFacets))
        {
            for(AsConfigurationHolder<AsPromotedFacet, AbstractAsFacetConfiguration> promotedFacet : (Iterable<AsConfigurationHolder<AsPromotedFacet, AbstractAsFacetConfiguration>>)promotedFacets
                            .orderedValues())
            {
                if(isValidFacet(facetSearchContext, (AsConfigurationHolder)promotedFacet))
                {
                    FacetField facetField = createFacetField((AsConfigurationHolder)promotedFacet);
                    facetField.setPriority(Integer.valueOf(priority));
                    searchQuery.addFacet(facetField);
                    priority--;
                }
            }
        }
        MergeMap<String, AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration>> facets = (MergeMap<String, AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration>>)result.getFacets();
        if(MapUtils.isNotEmpty((Map)facets))
        {
            for(AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration> facet : (Iterable<AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration>>)facets.orderedValues())
            {
                if(isValidFacet(facetSearchContext, (AsConfigurationHolder)facet))
                {
                    FacetField facetField = createFacetField((AsConfigurationHolder)facet);
                    facetField.setPriority(Integer.valueOf(priority));
                    searchQuery.addFacet(facetField);
                    priority--;
                }
            }
        }
    }


    protected boolean isValidFacet(FacetSearchContext facetSearchContext, AsConfigurationHolder<? extends AbstractAsFacetConfiguration, AbstractAsFacetConfiguration> facetHolder)
    {
        AbstractAsFacetConfiguration facet = (AbstractAsFacetConfiguration)facetHolder.getConfiguration();
        Map<String, IndexedProperty> indexedProperties = facetSearchContext.getIndexedType().getIndexedProperties();
        if(!indexedProperties.containsKey(facet.getIndexProperty()))
        {
            LOG.warn("Facet {} is no longer valid!", facet.getIndexProperty());
            return false;
        }
        return true;
    }


    protected FacetField createFacetField(AsConfigurationHolder<? extends AbstractAsFacetConfiguration, AbstractAsFacetConfiguration> facetHolder)
    {
        if(facetHolder.getData() instanceof FacetField)
        {
            return (FacetField)facetHolder.getData();
        }
        AbstractAsFacetConfiguration facet = (AbstractAsFacetConfiguration)facetHolder.getConfiguration();
        FacetField facetField = new FacetField(facet.getIndexProperty(), this.solrAsTypeMappingRegistry.toFacetType(facet.getFacetType()));
        facetField.setPriority(facet.getPriority());
        facetField.setDisplayNameProvider(facet.getValuesDisplayNameProvider());
        facetField.setSortProvider(facet.getValuesSortProvider());
        facetField.setTopValuesProvider(facet.getTopValuesProvider());
        facetField.setPromotedValues(convertAll(facet.getPromotedValues(), AbstractAsFacetValueConfiguration::getValue));
        facetField.setExcludedValues(convertAll(facet.getExcludedValues(), AbstractAsFacetValueConfiguration::getValue));
        return facetField;
    }


    protected void applyBoostItems(FacetSearchContext facetSearchContext, AsSearchProfileResult result)
    {
        SearchQuery searchQuery = facetSearchContext.getSearchQuery();
        searchQuery.getPromotedItems().clear();
        searchQuery.getExcludedItems().clear();
        if(MapUtils.isNotEmpty(result.getPromotedItems()))
        {
            MergeMap<PK, AsConfigurationHolder<AsPromotedItem, AbstractAsBoostItemConfiguration>> promotedItems = (MergeMap<PK, AsConfigurationHolder<AsPromotedItem, AbstractAsBoostItemConfiguration>>)result.getPromotedItems();
            for(AsConfigurationHolder<AsPromotedItem, AbstractAsBoostItemConfiguration> promotedItem : (Iterable<AsConfigurationHolder<AsPromotedItem, AbstractAsBoostItemConfiguration>>)promotedItems
                            .orderedValues())
            {
                searchQuery.addPromotedItem(((AsPromotedItem)promotedItem.getConfiguration()).getItemPk());
            }
        }
        if(MapUtils.isNotEmpty(result.getExcludedItems()))
        {
            MergeMap<PK, AsConfigurationHolder<AsExcludedItem, AbstractAsBoostItemConfiguration>> excludedItems = (MergeMap<PK, AsConfigurationHolder<AsExcludedItem, AbstractAsBoostItemConfiguration>>)result.getExcludedItems();
            for(AsConfigurationHolder<AsExcludedItem, AbstractAsBoostItemConfiguration> excludedItem : (Iterable<AsConfigurationHolder<AsExcludedItem, AbstractAsBoostItemConfiguration>>)excludedItems
                            .orderedValues())
            {
                searchQuery.addExcludedItem(((AsExcludedItem)excludedItem.getConfiguration()).getItemPk());
            }
        }
    }


    protected void applyBoostRules(FacetSearchContext facetSearchContext, AsSearchProfileResult result)
    {
        SearchQuery searchQuery = facetSearchContext.getSearchQuery();
        searchQuery.getBoosts().clear();
        if(CollectionUtils.isNotEmpty(result.getBoostRules()))
        {
            for(AsConfigurationHolder<AsBoostRule, AbstractAsBoostRuleConfiguration> boostRule : (Iterable<AsConfigurationHolder<AsBoostRule, AbstractAsBoostRuleConfiguration>>)result.getBoostRules())
            {
                if(isValidBoostRule(facetSearchContext, boostRule))
                {
                    searchQuery.addBoost(createBoostField(boostRule));
                }
            }
        }
    }


    protected boolean isValidBoostRule(FacetSearchContext facetSearchContext, AsConfigurationHolder<AsBoostRule, AbstractAsBoostRuleConfiguration> boostRuleHolder)
    {
        AsBoostRule boostRule = (AsBoostRule)boostRuleHolder.getConfiguration();
        Map<String, IndexedProperty> indexedProperties = facetSearchContext.getIndexedType().getIndexedProperties();
        if(!indexedProperties.containsKey(boostRule.getIndexProperty()))
        {
            LOG.warn("Boost rule {}{}{} is no longer valid!", new Object[] {boostRule.getIndexProperty(), boostRule.getOperator(), boostRule
                            .getValue()});
            return false;
        }
        return true;
    }


    protected BoostField createBoostField(AsConfigurationHolder<? extends AsBoostRule, AbstractAsBoostRuleConfiguration> boostRuleHolder)
    {
        if(boostRuleHolder.getData() instanceof BoostField)
        {
            return (BoostField)boostRuleHolder.getData();
        }
        AsBoostRule boostRule = (AsBoostRule)boostRuleHolder.getConfiguration();
        return new BoostField(boostRule.getIndexProperty(), this.solrAsTypeMappingRegistry.toQueryOperator(boostRule.getOperator()), boostRule
                        .getValue(), boostRule.getBoost(), this.solrAsTypeMappingRegistry.toBoostType(boostRule.getBoostType()));
    }


    protected void applySorts(FacetSearchContext facetSearchContext, AsSearchProfileResult result)
    {
        facetSearchContext.getAvailableNamedSorts().clear();
        if(MapUtils.isNotEmpty(result.getPromotedSorts()))
        {
            MergeMap<String, AsConfigurationHolder<AsPromotedSort, AbstractAsSortConfiguration>> promotedSorts = (MergeMap<String, AsConfigurationHolder<AsPromotedSort, AbstractAsSortConfiguration>>)result.getPromotedSorts();
            for(AsConfigurationHolder<AsPromotedSort, AbstractAsSortConfiguration> sort : (Iterable<AsConfigurationHolder<AsPromotedSort, AbstractAsSortConfiguration>>)promotedSorts.orderedValues())
            {
                facetSearchContext.getAvailableNamedSorts().add(createIndexedTypeSort(facetSearchContext, (AsConfigurationHolder)sort));
            }
        }
        if(MapUtils.isNotEmpty(result.getSorts()))
        {
            MergeMap<String, AsConfigurationHolder<AsSort, AbstractAsSortConfiguration>> sorts = (MergeMap<String, AsConfigurationHolder<AsSort, AbstractAsSortConfiguration>>)result.getSorts();
            for(AsConfigurationHolder<AsSort, AbstractAsSortConfiguration> sort : (Iterable<AsConfigurationHolder<AsSort, AbstractAsSortConfiguration>>)sorts.orderedValues())
            {
                facetSearchContext.getAvailableNamedSorts().add(createIndexedTypeSort(facetSearchContext, (AsConfigurationHolder)sort));
            }
        }
    }


    protected IndexedTypeSort createIndexedTypeSort(FacetSearchContext facetSearchContext, AsConfigurationHolder<? extends AbstractAsSortConfiguration, AbstractAsSortConfiguration> sortHolder)
    {
        if(sortHolder.getData() instanceof IndexedTypeSort)
        {
            return (IndexedTypeSort)sortHolder.getData();
        }
        AbstractAsSortConfiguration sort = (AbstractAsSortConfiguration)sortHolder.getConfiguration();
        IndexedTypeSort indexedTypeSort = new IndexedTypeSort();
        indexedTypeSort.setCode(sort.getCode());
        indexedTypeSort.setName((String)sort.getName().get(facetSearchContext.getSearchQuery().getLanguage()));
        indexedTypeSort.setApplyPromotedItems(sort.isApplyPromotedItems());
        indexedTypeSort.setHighlightPromotedItems(sort.isHighlightPromotedItems());
        indexedTypeSort
                        .setFields((List)sort.getExpressions().stream().filter(expression -> isValidSortExpression(facetSearchContext, expression))
                                        .map(this::createIndexedTypeSortField).collect(Collectors.toList()));
        return indexedTypeSort;
    }


    protected boolean isValidSortExpression(FacetSearchContext facetSearchContext, AsSortExpression sortExpression)
    {
        Map<String, IndexedProperty> indexedProperties = facetSearchContext.getIndexedType().getIndexedProperties();
        if(!StringUtils.equals("score", sortExpression.getExpression()) &&
                        !indexedProperties.containsKey(sortExpression.getExpression()))
        {
            LOG.warn("Sort expression {} is no longer valid!", sortExpression.getExpression());
            return false;
        }
        return true;
    }


    protected IndexedTypeSortField createIndexedTypeSortField(AsSortExpression sortExpression)
    {
        IndexedTypeSortField indexedTypeSortField = new IndexedTypeSortField();
        indexedTypeSortField.setFieldName(sortExpression.getExpression());
        indexedTypeSortField.setAscending(Objects.equals(sortExpression.getOrder(), AsSortOrder.ASCENDING));
        return indexedTypeSortField;
    }


    protected void applyGroup(FacetSearchContext facetSearchContext, AsSearchProfileResult result)
    {
        SearchQuery searchQuery = facetSearchContext.getSearchQuery();
        searchQuery.getGroupCommands().clear();
        AsConfigurationHolder<AsGroup, AsGroup> groupHolder = result.getGroup();
        if(groupHolder != null && isValidGroup(facetSearchContext, groupHolder))
        {
            searchQuery.getGroupCommands().addAll(createGroupCommandFields(groupHolder));
        }
    }


    protected List<GroupCommandField> createGroupCommandFields(AsConfigurationHolder<AsGroup, AsGroup> groupHolder)
    {
        if(groupHolder.getData() instanceof List)
        {
            return (List<GroupCommandField>)groupHolder.getData();
        }
        AsGroup group = (AsGroup)groupHolder.getConfiguration();
        return Collections.singletonList(createGroupCommandField(group));
    }


    protected GroupCommandField createGroupCommandField(AsGroup group)
    {
        return new GroupCommandField(group.getExpression(), group.getLimit());
    }


    protected boolean isValidGroup(FacetSearchContext facetSearchContext, AsConfigurationHolder<AsGroup, AsGroup> groupHolder)
    {
        AsGroup group = (AsGroup)groupHolder.getConfiguration();
        if(group.getExpression() == null)
        {
            return false;
        }
        Map<String, IndexedProperty> indexedProperties = facetSearchContext.getIndexedType().getIndexedProperties();
        if(!indexedProperties.containsKey(group.getExpression()))
        {
            LOG.warn("Group expression {} is no longer valid!", group.getExpression());
            return false;
        }
        return true;
    }


    protected <S, T> List<T> convertAll(Collection<? extends S> source, Function<S, T> converter)
    {
        if(source == null || source.isEmpty())
        {
            return Collections.emptyList();
        }
        Objects.requireNonNull(converter);
        return (List<T>)source.stream().map(converter::apply).collect(Collectors.toList());
    }


    protected PK extractPkFromDocument(Document document)
    {
        Object pk = document.getFields().get("pk");
        if(pk instanceof Long)
        {
            return PK.fromLong(((Long)pk).longValue());
        }
        return null;
    }


    public SolrAsCatalogVersionResolver getSolrAsCatalogVersionResolver()
    {
        return this.solrAsCatalogVersionResolver;
    }


    @Required
    public void setSolrAsCatalogVersionResolver(SolrAsCatalogVersionResolver solrAsCatalogVersionResolver)
    {
        this.solrAsCatalogVersionResolver = solrAsCatalogVersionResolver;
    }


    public SolrAsCategoryPathResolver getSolrAsCategoryPathResolver()
    {
        return this.solrAsCategoryPathResolver;
    }


    @Required
    public void setSolrAsCategoryPathResolver(SolrAsCategoryPathResolver solrAsCategoryPathResolver)
    {
        this.solrAsCategoryPathResolver = solrAsCategoryPathResolver;
    }


    public SolrAsTypeMappingRegistry getSolrAsTypeMappingRegistry()
    {
        return this.solrAsTypeMappingRegistry;
    }


    @Required
    public void setSolrAsTypeMappingRegistry(SolrAsTypeMappingRegistry solrAsTypeMappingRegistry)
    {
        this.solrAsTypeMappingRegistry = solrAsTypeMappingRegistry;
    }


    public AsSearchProfileActivationService getAsSearchProfileActivationService()
    {
        return this.asSearchProfileActivationService;
    }


    @Required
    public void setAsSearchProfileActivationService(AsSearchProfileActivationService asSearchProfileActivationService)
    {
        this.asSearchProfileActivationService = asSearchProfileActivationService;
    }


    public AsSearchProfileCalculationService getAsSearchProfileCalculationService()
    {
        return this.asSearchProfileCalculationService;
    }


    @Required
    public void setAsSearchProfileCalculationService(AsSearchProfileCalculationService asSearchProfileCalculationService)
    {
        this.asSearchProfileCalculationService = asSearchProfileCalculationService;
    }


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    protected AsUidGenerator createUidGenerator()
    {
        return (AsUidGenerator)new SearchQueryUidGenerator();
    }
}
