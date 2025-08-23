package de.hybris.platform.adaptivesearch.searchservices.listeners;

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
import de.hybris.platform.adaptivesearch.data.AsFacetRange;
import de.hybris.platform.adaptivesearch.data.AsGroup;
import de.hybris.platform.adaptivesearch.data.AsPromotedFacet;
import de.hybris.platform.adaptivesearch.data.AsPromotedFacetValue;
import de.hybris.platform.adaptivesearch.data.AsPromotedItem;
import de.hybris.platform.adaptivesearch.data.AsPromotedSort;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileActivationGroup;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.data.AsSort;
import de.hybris.platform.adaptivesearch.data.AsSortExpression;
import de.hybris.platform.adaptivesearch.enums.AsBoostOperator;
import de.hybris.platform.adaptivesearch.enums.AsFacetType;
import de.hybris.platform.adaptivesearch.enums.AsFacetsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsGroupMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsSortOrder;
import de.hybris.platform.adaptivesearch.enums.AsSortsMergeMode;
import de.hybris.platform.adaptivesearch.searchservices.strategies.SnAsCatalogVersionResolver;
import de.hybris.platform.adaptivesearch.searchservices.strategies.SnAsCategoryPathResolver;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileActivationService;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileCalculationService;
import de.hybris.platform.adaptivesearch.strategies.AsUidGenerator;
import de.hybris.platform.adaptivesearch.util.MergeMap;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.admin.data.SnIndexType;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.core.service.SnIdentityProvider;
import de.hybris.platform.searchservices.core.service.SnQualifier;
import de.hybris.platform.searchservices.core.service.SnQualifierType;
import de.hybris.platform.searchservices.core.service.SnQualifierTypeFactory;
import de.hybris.platform.searchservices.search.SnSearchException;
import de.hybris.platform.searchservices.search.data.AbstractSnBucketsFacetRequest;
import de.hybris.platform.searchservices.search.data.AbstractSnFacetRequest;
import de.hybris.platform.searchservices.search.data.AbstractSnQuery;
import de.hybris.platform.searchservices.search.data.SnBucketsSort;
import de.hybris.platform.searchservices.search.data.SnEqualQuery;
import de.hybris.platform.searchservices.search.data.SnFacetFilterMode;
import de.hybris.platform.searchservices.search.data.SnFilter;
import de.hybris.platform.searchservices.search.data.SnGreaterThanOrEqualQuery;
import de.hybris.platform.searchservices.search.data.SnGreaterThanQuery;
import de.hybris.platform.searchservices.search.data.SnGroupRequest;
import de.hybris.platform.searchservices.search.data.SnLessThanOrEqualQuery;
import de.hybris.platform.searchservices.search.data.SnLessThanQuery;
import de.hybris.platform.searchservices.search.data.SnMatchQuery;
import de.hybris.platform.searchservices.search.data.SnMatchTermsQuery;
import de.hybris.platform.searchservices.search.data.SnMatchType;
import de.hybris.platform.searchservices.search.data.SnNotQuery;
import de.hybris.platform.searchservices.search.data.SnPromotedHit;
import de.hybris.platform.searchservices.search.data.SnPromotedHitsRankRule;
import de.hybris.platform.searchservices.search.data.SnQueryFunctionRankRule;
import de.hybris.platform.searchservices.search.data.SnRangeBucketRequest;
import de.hybris.platform.searchservices.search.data.SnRangeBucketsFacetRequest;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import de.hybris.platform.searchservices.search.data.SnSort;
import de.hybris.platform.searchservices.search.data.SnSortExpression;
import de.hybris.platform.searchservices.search.data.SnTermBucketsFacetRequest;
import de.hybris.platform.searchservices.search.service.SnSearchContext;
import de.hybris.platform.searchservices.search.service.SnSearchListener;
import de.hybris.platform.searchservices.util.ConverterUtils;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SnAsSearchProfileCalculationListener implements SnSearchListener, InitializingBean, ApplicationContextAware
{
    private CommonI18NService commonI18NService;
    private SnAsCatalogVersionResolver snAsCatalogVersionResolver;
    private SnAsCategoryPathResolver snAsCategoryPathResolver;
    private AsSearchProfileActivationService asSearchProfileActivationService;
    private AsSearchProfileCalculationService asSearchProfileCalculationService;
    private SnQualifierTypeFactory snQualifierTypeFactory;
    private ApplicationContext applicationContext;
    private final Map<AsBoostOperator, Function<AsBoostRule, AbstractSnQuery>> createBoostQueryFunctions = new EnumMap<>(AsBoostOperator.class);


    public void afterPropertiesSet()
    {
        this.createBoostQueryFunctions.put(AsBoostOperator.MATCH, this::createBoostMatchQueryFunction);
        this.createBoostQueryFunctions.put(AsBoostOperator.EQUAL, this::createBoostEqualQueryFunction);
        this.createBoostQueryFunctions.put(AsBoostOperator.GREATER_THAN_OR_EQUAL, this::createBoostGreaterThanOrEqualQueryFunction);
        this.createBoostQueryFunctions.put(AsBoostOperator.GREATER_THAN, this::createBoostGreaterThanQueryFunction);
        this.createBoostQueryFunctions.put(AsBoostOperator.LESS_THAN_OR_EQUAL, this::createBoostLessThanOrEqualQueryFunction);
        this.createBoostQueryFunctions.put(AsBoostOperator.LESS_THAN, this::createBoostLessThanQueryFunction);
    }


    public void beforeSearch(SnSearchContext context) throws SnSearchException
    {
        SnSearchQuery searchQuery = context.getSearchRequest().getSearchQuery();
        String indexConfigurationId = context.getIndexConfiguration().getId();
        String indexTypeId = context.getIndexType().getId();
        LanguageModel language = resolveLanguage();
        CurrencyModel currency = resolveCurrency();
        List<CatalogVersionModel> catalogVersions = resolveCatalogVersions(context);
        List<CategoryModel> categoryPath = resolveCategoryPath(context, catalogVersions);
        Map<String, List<String>> qualifiers = resolveQualifiers(context);
        DefaultAsSearchProfileContext defaultAsSearchProfileContext = DefaultAsSearchProfileContext.builder().withQueryContexts(searchQuery.getQueryContexts()).withIndexConfiguration(indexConfigurationId).withIndexType(indexTypeId).withCatalogVersions(catalogVersions).withCategoryPath(categoryPath)
                        .withLanguage(language).withCurrency(currency).withQualifiers(qualifiers).build();
        if(StringUtils.isNotBlank(searchQuery.getQuery()))
        {
            defaultAsSearchProfileContext.setQuery(searchQuery.getQuery());
        }
        List<AsSearchProfileActivationGroup> groups = this.asSearchProfileActivationService.getSearchProfileActivationGroupsForContext((AsSearchProfileContext)defaultAsSearchProfileContext);
        if(CollectionUtils.isNotEmpty(groups))
        {
            AsSearchProfileResult searchQueryResult = createSearchProfileResult((AsSearchProfileContext)defaultAsSearchProfileContext, searchQuery);
            AsSearchProfileResult result = this.asSearchProfileCalculationService.calculateGroups((AsSearchProfileContext)defaultAsSearchProfileContext, searchQueryResult, groups);
            context.getAttributes().put("adaptiveSearchContext", defaultAsSearchProfileContext);
            context.getAttributes().put("adaptiveSearchResult", result);
            updateSearchQuery(context, (AsSearchProfileContext)defaultAsSearchProfileContext, result);
        }
    }


    public void afterSearch(SnSearchContext context)
    {
        Map<String, Object> debug = context.getSearchResponse().getSearchResult().getDebug();
        debug.put("adaptiveSearchContext", context
                        .getAttributes().get("adaptiveSearchContext"));
        debug.put("adaptiveSearchResult", context
                        .getAttributes().get("adaptiveSearchResult"));
    }


    public void afterSearchError(SnSearchContext context)
    {
    }


    protected LanguageModel resolveLanguage()
    {
        return this.commonI18NService.getCurrentLanguage();
    }


    protected CurrencyModel resolveCurrency()
    {
        return this.commonI18NService.getCurrentCurrency();
    }


    protected List<CatalogVersionModel> resolveCatalogVersions(SnSearchContext context)
    {
        return this.snAsCatalogVersionResolver.resolveCatalogVersions(context);
    }


    protected List<CategoryModel> resolveCategoryPath(SnSearchContext context, List<CatalogVersionModel> catalogVersions)
    {
        return this.snAsCategoryPathResolver.resolveCategoryPath(context, catalogVersions);
    }


    protected Map<String, List<String>> resolveQualifiers(SnSearchContext context)
    {
        return (Map<String, List<String>>)context.getQualifiers().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> (List)((List)entry.getValue()).stream().map(SnQualifier::getId).collect(Collectors.toList())));
    }


    protected AsSearchProfileResult createSearchProfileResult(AsSearchProfileContext asContext, SnSearchQuery searchQuery)
    {
        AsSearchProfileResult result = this.asSearchProfileCalculationService.createResult(asContext);
        AsUidGenerator asUidGenerator = createUidGenerator();
        createSearchProfileResultFacets(asContext, searchQuery, result, asUidGenerator);
        createSearchProfileResultSorts(asContext, searchQuery, result, asUidGenerator);
        createSearchProfileResultGroup(asContext, searchQuery, result, asUidGenerator);
        return result;
    }


    protected AsUidGenerator createUidGenerator()
    {
        return (AsUidGenerator)new SearchQueryUidGenerator();
    }


    protected void createSearchProfileResultFacets(AsSearchProfileContext asContext, SnSearchQuery searchQuery, AsSearchProfileResult result, AsUidGenerator asUidGenerator)
    {
        result.setFacetsMergeMode(AsFacetsMergeMode.REPLACE);
        if(CollectionUtils.isNotEmpty(searchQuery.getFacets()))
        {
            MergeMap<String, AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration>> resultFacets = (MergeMap<String, AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration>>)result.getFacets();
            Objects.requireNonNull(AbstractSnBucketsFacetRequest.class);
            Objects.requireNonNull(AbstractSnBucketsFacetRequest.class);
            searchQuery.getFacets().stream().filter(AbstractSnBucketsFacetRequest.class::isInstance).map(AbstractSnBucketsFacetRequest.class::cast)
                            .forEach(bucketsFacetRequest -> resultFacets.mergeAfter(bucketsFacetRequest.getExpression(), createFacetHolder(asContext, bucketsFacetRequest, asUidGenerator)));
        }
    }


    protected AsFacetType convertFilterMode(SnFacetFilterMode source)
    {
        return (source == SnFacetFilterMode.MULTISELECT) ? AsFacetType.MULTISELECT : AsFacetType.REFINE;
    }


    protected AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration> createFacetHolder(AsSearchProfileContext asContext, AbstractSnBucketsFacetRequest source, AsUidGenerator asUidGenerator)
    {
        AsFacet facet = new AsFacet();
        facet.setUid(asUidGenerator.generateUid());
        facet.setIndexProperty(source.getExpression());
        facet.setPriority(Integer.valueOf(100));
        facet.setFacetType((AsFacetType)ConverterUtils.convert(source.getFilterMode(), this::convertFilterMode));
        if(source instanceof SnTermBucketsFacetRequest)
        {
            SnTermBucketsFacetRequest sourceFacet = (SnTermBucketsFacetRequest)source;
            facet.setRanged(false);
            facet.setTopValuesSize(sourceFacet.getTopBucketsSize());
            facet.setPromotedValues(ConverterUtils.convertAll(sourceFacet.getPromotedBucketIds(), value -> (AsPromotedFacetValue)convertFacetValue(value, asUidGenerator, AsPromotedFacetValue::new)));
            facet.setExcludedValues(ConverterUtils.convertAll(sourceFacet.getExcludedBucketIds(), value -> (AsExcludedFacetValue)convertFacetValue(value, asUidGenerator, AsExcludedFacetValue::new)));
            facet.setSort((sourceFacet.getSort() != null) ? sourceFacet.getSort().getId() : null);
        }
        else if(source instanceof SnRangeBucketsFacetRequest)
        {
            SnRangeBucketsFacetRequest sourceFacet = (SnRangeBucketsFacetRequest)source;
            facet.setRanged(true);
            facet.setRangeIncludeFrom(BooleanUtils.isTrue(sourceFacet.getIncludeFrom()));
            facet.setRangeIncludeTo(BooleanUtils.isTrue(sourceFacet.getIncludeTo()));
            facet.setRanges(ConverterUtils.convertAll(sourceFacet.getBuckets(), value -> convertFacetRange(value, asUidGenerator)));
        }
        return this.asSearchProfileCalculationService.createConfigurationHolder(asContext, facet, source);
    }


    protected <T extends AbstractAsFacetValueConfiguration> T convertFacetValue(String value, AsUidGenerator asUidGenerator, Supplier<T> createSupplier)
    {
        AbstractAsFacetValueConfiguration abstractAsFacetValueConfiguration = (AbstractAsFacetValueConfiguration)createSupplier.get();
        abstractAsFacetValueConfiguration.setUid(asUidGenerator.generateUid());
        abstractAsFacetValueConfiguration.setValue(value);
        return (T)abstractAsFacetValueConfiguration;
    }


    protected AsFacetRange convertFacetRange(SnRangeBucketRequest source, AsUidGenerator asUidGenerator)
    {
        LanguageModel currentLanguage = this.commonI18NService.getCurrentLanguage();
        AsFacetRange facetRange = new AsFacetRange();
        facetRange.setUid(asUidGenerator.generateUid());
        facetRange.setId(source.getId());
        facetRange.setName(Collections.singletonMap(currentLanguage.getIsocode(), source.getName()));
        facetRange.setFrom(String.valueOf(source.getFrom()));
        facetRange.setTo(String.valueOf(source.getTo()));
        return facetRange;
    }


    protected void createSearchProfileResultSorts(AsSearchProfileContext asContext, SnSearchQuery searchQuery, AsSearchProfileResult result, AsUidGenerator asUidGenerator)
    {
        result.setSortsMergeMode(AsSortsMergeMode.REPLACE);
        if(CollectionUtils.isNotEmpty(searchQuery.getAvailableSorts()))
        {
            MergeMap<String, AsConfigurationHolder<AsSort, AbstractAsSortConfiguration>> resultSorts = (MergeMap<String, AsConfigurationHolder<AsSort, AbstractAsSortConfiguration>>)result.getSorts();
            for(SnSort availableSort : searchQuery.getAvailableSorts())
            {
                resultSorts.mergeAfter(availableSort.getId(), createSortHolder(asContext, availableSort, asUidGenerator));
            }
        }
    }


    protected AsConfigurationHolder<AsSort, AbstractAsSortConfiguration> createSortHolder(AsSearchProfileContext asContext, SnSort source, AsUidGenerator asUidGenerator)
    {
        LanguageModel currentLanguage = this.commonI18NService.getCurrentLanguage();
        AsSort sort = new AsSort();
        sort.setUid(asUidGenerator.generateUid());
        sort.setCode(source.getId());
        sort.setName(Collections.singletonMap(currentLanguage.getIsocode(), source.getName()));
        sort.setPriority(Integer.valueOf(100));
        sort.setApplyPromotedItems(BooleanUtils.isTrue(source.getApplyPromotedHits()));
        sort.setHighlightPromotedItems(BooleanUtils.isTrue(source.getHighlightPromotedHits()));
        sort.setExpressions(ConverterUtils.convertAll(source.getExpressions(), this::createSortExpression));
        return this.asSearchProfileCalculationService.createConfigurationHolder(asContext, sort, source);
    }


    protected AsSortExpression createSortExpression(SnSortExpression source)
    {
        AsSortExpression sortExpression = new AsSortExpression();
        sortExpression.setExpression(source.getExpression());
        sortExpression.setOrder(BooleanUtils.isNotFalse(source.getAscending()) ? AsSortOrder.ASCENDING : AsSortOrder.DESCENDING);
        return sortExpression;
    }


    protected void createSearchProfileResultGroup(AsSearchProfileContext asContext, SnSearchQuery searchQuery, AsSearchProfileResult result, AsUidGenerator asUidGenerator)
    {
        result.setGroupMergeMode(AsGroupMergeMode.REPLACE);
        if(searchQuery.getGroup() != null)
        {
            result.setGroup(createGroupHolder(asContext, searchQuery.getGroup(), asUidGenerator));
        }
    }


    protected AsConfigurationHolder<AsGroup, AsGroup> createGroupHolder(AsSearchProfileContext asContext, SnGroupRequest source, AsUidGenerator asUidGenerator)
    {
        AsGroup group = new AsGroup();
        group.setUid(asUidGenerator.generateUid());
        group.setExpression(source.getExpression());
        group.setLimit(source.getLimit());
        return this.asSearchProfileCalculationService.createConfigurationHolder(asContext, group, source);
    }


    protected void updateSearchQuery(SnSearchContext context, AsSearchProfileContext asContext, AsSearchProfileResult result) throws SnSearchException
    {
        SnIdentityProvider<?> identityProvider = getIdentityProvider(context.getIndexType());
        updateSearchQueryFacets(context, asContext, result);
        updateSearchQueryRankRules(context, identityProvider, result);
        updateSearchQueryExcludedItems(context, identityProvider, result);
        updateSearchQuerySorts(context, result);
        updateSearchQueryGroup(context, result);
    }


    protected void updateSearchQueryFacets(SnSearchContext context, AsSearchProfileContext asContext, AsSearchProfileResult result)
    {
        SnSearchQuery searchQuery = context.getSearchRequest().getSearchQuery();
        searchQuery.getFacets().clear();
        MergeMap<String, AsConfigurationHolder<AsPromotedFacet, AbstractAsFacetConfiguration>> promotedFacets = (MergeMap<String, AsConfigurationHolder<AsPromotedFacet, AbstractAsFacetConfiguration>>)result.getPromotedFacets();
        if(MapUtils.isNotEmpty((Map)promotedFacets))
        {
            for(AsConfigurationHolder<AsPromotedFacet, AbstractAsFacetConfiguration> promotedFacet : (Iterable<AsConfigurationHolder<AsPromotedFacet, AbstractAsFacetConfiguration>>)promotedFacets
                            .orderedValues())
            {
                AbstractSnFacetRequest facetRequest = ((AsPromotedFacet)promotedFacet.getConfiguration()).isRanged()
                                ? (AbstractSnFacetRequest)createRangeBucketsFacetRequest(context, asContext, (AsConfigurationHolder)promotedFacet)
                                : (AbstractSnFacetRequest)createTermBucketsFacetRequest((AsConfigurationHolder)promotedFacet);
                searchQuery.getFacets().add(facetRequest);
            }
        }
        MergeMap<String, AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration>> facets = (MergeMap<String, AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration>>)result.getFacets();
        if(MapUtils.isNotEmpty((Map)facets))
        {
            for(AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration> facet : (Iterable<AsConfigurationHolder<AsFacet, AbstractAsFacetConfiguration>>)facets.orderedValues())
            {
                AbstractSnFacetRequest facetRequest = ((AsFacet)facet.getConfiguration()).isRanged() ? (AbstractSnFacetRequest)createRangeBucketsFacetRequest(context, asContext, (AsConfigurationHolder)facet) : (AbstractSnFacetRequest)createTermBucketsFacetRequest((AsConfigurationHolder)facet);
                searchQuery.getFacets().add(facetRequest);
            }
        }
    }


    protected SnTermBucketsFacetRequest createTermBucketsFacetRequest(AsConfigurationHolder<? extends AbstractAsFacetConfiguration, AbstractAsFacetConfiguration> facetHolder)
    {
        if(facetHolder.getData() instanceof SnTermBucketsFacetRequest)
        {
            return (SnTermBucketsFacetRequest)facetHolder.getData();
        }
        AbstractAsFacetConfiguration source = (AbstractAsFacetConfiguration)facetHolder.getConfiguration();
        SnTermBucketsFacetRequest facetRequest = new SnTermBucketsFacetRequest();
        facetRequest.setId(source.getIndexProperty());
        facetRequest.setExpression(source.getIndexProperty());
        facetRequest.setFilterMode(convertFacetType(source.getFacetType()));
        facetRequest.setTopBucketsSize(source.getTopValuesSize());
        facetRequest.setPromotedBucketIds(ConverterUtils.convertAll(source.getPromotedValues(), AbstractAsFacetValueConfiguration::getValue));
        facetRequest.setExcludedBucketIds(ConverterUtils.convertAll(source.getExcludedValues(), AbstractAsFacetValueConfiguration::getValue));
        if(StringUtils.isNotBlank(source.getSort()))
        {
            SnBucketsSort bucketsSort = new SnBucketsSort();
            bucketsSort.setId(source.getSort());
            facetRequest.setSort(bucketsSort);
        }
        LanguageModel currentLanguage = this.commonI18NService.getCurrentLanguage();
        if(StringUtils.isNotBlank((CharSequence)source.getName().get(currentLanguage.getIsocode())))
        {
            facetRequest.setName((String)source.getName().get(currentLanguage.getIsocode()));
        }
        return facetRequest;
    }


    protected SnRangeBucketsFacetRequest createRangeBucketsFacetRequest(SnSearchContext context, AsSearchProfileContext asContext, AsConfigurationHolder<? extends AbstractAsFacetConfiguration, AbstractAsFacetConfiguration> facetHolder)
    {
        if(facetHolder.getData() instanceof SnRangeBucketsFacetRequest)
        {
            return (SnRangeBucketsFacetRequest)facetHolder.getData();
        }
        AbstractAsFacetConfiguration source = (AbstractAsFacetConfiguration)facetHolder.getConfiguration();
        SnRangeBucketsFacetRequest facetRequest = new SnRangeBucketsFacetRequest();
        facetRequest.setId(source.getIndexProperty());
        facetRequest.setExpression(source.getIndexProperty());
        facetRequest.setFilterMode(convertFacetType(source.getFacetType()));
        facetRequest.setIncludeFrom(Boolean.valueOf(source.isRangeIncludeFrom()));
        facetRequest.setIncludeTo(Boolean.valueOf(source.isRangeIncludeTo()));
        LanguageModel currentLanguage = this.commonI18NService.getCurrentLanguage();
        if(StringUtils.isNotBlank((CharSequence)source.getName().get(currentLanguage.getIsocode())))
        {
            facetRequest.setName((String)source.getName().get(currentLanguage.getIsocode()));
        }
        facetRequest.setBuckets(
                        ConverterUtils.convertAll(getRangesForBucketsRequest(context, asContext, source), this::convertRangeRequest));
        return facetRequest;
    }


    protected List<AsFacetRange> getRangesForBucketsRequest(SnSearchContext context, AsSearchProfileContext asContext, AbstractAsFacetConfiguration source)
    {
        Optional<SnQualifierType> qualifierType = getQualifierType(context, source);
        if(qualifierType.isPresent())
        {
            List<String> currentQualifiers = (List<String>)asContext.getQualifiers().get(((SnQualifierType)qualifierType.get()).getId());
            if(CollectionUtils.isNotEmpty(currentQualifiers))
            {
                return (List<AsFacetRange>)source.getRanges().stream().filter(r -> currentQualifiers.contains(r.getQualifier()))
                                .collect(Collectors.toList());
            }
        }
        return source.getRanges();
    }


    protected Optional<SnQualifierType> getQualifierType(SnSearchContext context, AbstractAsFacetConfiguration source)
    {
        SnField snField = (SnField)context.getIndexType().getFields().get(source.getIndexProperty());
        if(snField != null)
        {
            return this.snQualifierTypeFactory.getQualifierType((SnContext)context, snField);
        }
        return Optional.empty();
    }


    protected SnFacetFilterMode convertFacetType(AsFacetType source)
    {
        if(source == null)
        {
            return null;
        }
        return (source == AsFacetType.MULTISELECT) ? SnFacetFilterMode.MULTISELECT : SnFacetFilterMode.REFINE;
    }


    protected SnRangeBucketRequest convertRangeRequest(AsFacetRange source)
    {
        LanguageModel languageModel = resolveLanguage();
        SnRangeBucketRequest bucketRequest = new SnRangeBucketRequest();
        bucketRequest.setId(source.getId());
        bucketRequest.setName((String)source.getName().get(languageModel.getIsocode()));
        bucketRequest.setFrom(source.getFrom());
        bucketRequest.setTo(source.getTo());
        return bucketRequest;
    }


    protected void updateSearchQueryRankRules(SnSearchContext context, SnIdentityProvider<?> identityProvider, AsSearchProfileResult result) throws SnSearchException
    {
        updateSearchQueryPromotedHitsRankRules(context, identityProvider, result);
        updateSearchQueryQueryFunctionRankRules(context, result);
    }


    protected void updateSearchQueryPromotedHitsRankRules(SnSearchContext context, SnIdentityProvider<?> identityProvider, AsSearchProfileResult result) throws SnSearchException
    {
        SnSearchQuery searchQuery = context.getSearchRequest().getSearchQuery();
        if(MapUtils.isNotEmpty(result.getPromotedItems()))
        {
            MergeMap<PK, AsConfigurationHolder<AsPromotedItem, AbstractAsBoostItemConfiguration>> promotedItems = (MergeMap<PK, AsConfigurationHolder<AsPromotedItem, AbstractAsBoostItemConfiguration>>)result.getPromotedItems();
            List<SnPromotedHit> promotedHits = new ArrayList<>();
            for(AsConfigurationHolder<AsPromotedItem, AbstractAsBoostItemConfiguration> promotedItem : (Iterable<AsConfigurationHolder<AsPromotedItem, AbstractAsBoostItemConfiguration>>)promotedItems
                            .orderedValues())
            {
                SnPromotedHit promotedHit = new SnPromotedHit();
                promotedHit.setId(getIdentifierFromPk(context, identityProvider, ((AsPromotedItem)promotedItem.getConfiguration()).getItemPk()));
                promotedHits.add(promotedHit);
            }
            SnPromotedHitsRankRule promotedHitsRankRule = new SnPromotedHitsRankRule();
            promotedHitsRankRule.setHits(promotedHits);
            searchQuery.getRankRules().add(promotedHitsRankRule);
        }
    }


    protected void updateSearchQueryExcludedItems(SnSearchContext context, SnIdentityProvider<?> identityProvider, AsSearchProfileResult result) throws SnSearchException
    {
        SnSearchQuery searchQuery = context.getSearchRequest().getSearchQuery();
        if(MapUtils.isNotEmpty(result.getExcludedItems()))
        {
            List<AsConfigurationHolder<AsExcludedItem, AbstractAsBoostItemConfiguration>> excludedItems = ((MergeMap)result.getExcludedItems()).orderedValues();
            Set<PK> excludedPks = (Set<PK>)excludedItems.stream().map(i -> ((AsExcludedItem)i.getConfiguration()).getItemPk()).collect(Collectors.toSet());
            List<Object> excludedIds = new ArrayList();
            for(PK excludedPk : excludedPks)
            {
                excludedIds.add(getIdentifierFromPk(context, identityProvider, excludedPk));
            }
            SnMatchTermsQuery matchTermsQuery = new SnMatchTermsQuery();
            matchTermsQuery.setMatchType(SnMatchType.ANY);
            matchTermsQuery.setExpression("_id_");
            matchTermsQuery.setValues(excludedIds);
            SnNotQuery notQuery = new SnNotQuery();
            notQuery.setQuery((AbstractSnQuery)matchTermsQuery);
            SnFilter filter = new SnFilter();
            filter.setQuery((AbstractSnQuery)notQuery);
            searchQuery.getFilters().add(filter);
        }
    }


    protected String getIdentifierFromPk(SnSearchContext context, SnIdentityProvider<?> identityProvider, PK pk) throws SnSearchException
    {
        try
        {
            return identityProvider.getIdentifier((SnContext)context, pk);
        }
        catch(SnException e)
        {
            throw new SnSearchException("Could not retrieve identifier", e);
        }
    }


    protected SnIdentityProvider<ItemModel> getIdentityProvider(SnIndexType type) throws SnSearchException
    {
        try
        {
            return (SnIdentityProvider<ItemModel>)this.applicationContext.getBean(type.getIdentityProvider(), SnIdentityProvider.class);
        }
        catch(BeansException e)
        {
            throw new SnSearchException("Cannot create identity provider [" + type.getIdentityProvider() + "]", e);
        }
    }


    protected void updateSearchQueryQueryFunctionRankRules(SnSearchContext context, AsSearchProfileResult result)
    {
        SnSearchQuery searchQuery = context.getSearchRequest().getSearchQuery();
        if(CollectionUtils.isNotEmpty(result.getBoostRules()))
        {
            for(AsConfigurationHolder<AsBoostRule, AbstractAsBoostRuleConfiguration> boostRule : (Iterable<AsConfigurationHolder<AsBoostRule, AbstractAsBoostRuleConfiguration>>)result.getBoostRules())
            {
                searchQuery.getRankRules().add(createQueryFunctionRankRule(boostRule));
            }
        }
    }


    protected SnQueryFunctionRankRule createQueryFunctionRankRule(AsConfigurationHolder<? extends AsBoostRule, AbstractAsBoostRuleConfiguration> boostRuleHolder)
    {
        if(boostRuleHolder.getData() instanceof SnQueryFunctionRankRule)
        {
            return (SnQueryFunctionRankRule)boostRuleHolder.getData();
        }
        AsBoostRule source = (AsBoostRule)boostRuleHolder.getConfiguration();
        Function<AsBoostRule, AbstractSnQuery> createBoostQueryFunction = this.createBoostQueryFunctions.get(source.getOperator());
        if(createBoostQueryFunction == null)
        {
            throw new UnsupportedOperationException(MessageFormat.format("Cannot convert boost query: {}", new Object[] {source}));
        }
        AbstractSnQuery boostQuery = createBoostQueryFunction.apply(source);
        SnQueryFunctionRankRule queryFunctionRankRule = new SnQueryFunctionRankRule();
        queryFunctionRankRule.setWeight(source.getBoost());
        queryFunctionRankRule.setQuery(boostQuery);
        return queryFunctionRankRule;
    }


    protected AbstractSnQuery createBoostMatchQueryFunction(AsBoostRule source)
    {
        SnMatchQuery target = new SnMatchQuery();
        target.setExpression(source.getIndexProperty());
        target.setValue(source.getValue());
        return (AbstractSnQuery)target;
    }


    protected AbstractSnQuery createBoostEqualQueryFunction(AsBoostRule source)
    {
        SnEqualQuery target = new SnEqualQuery();
        target.setExpression(source.getIndexProperty());
        target.setValue(source.getValue());
        return (AbstractSnQuery)target;
    }


    protected AbstractSnQuery createBoostGreaterThanOrEqualQueryFunction(AsBoostRule source)
    {
        SnGreaterThanOrEqualQuery target = new SnGreaterThanOrEqualQuery();
        target.setExpression(source.getIndexProperty());
        target.setValue(source.getValue());
        return (AbstractSnQuery)target;
    }


    protected AbstractSnQuery createBoostGreaterThanQueryFunction(AsBoostRule source)
    {
        SnGreaterThanQuery target = new SnGreaterThanQuery();
        target.setExpression(source.getIndexProperty());
        target.setValue(source.getValue());
        return (AbstractSnQuery)target;
    }


    protected AbstractSnQuery createBoostLessThanOrEqualQueryFunction(AsBoostRule source)
    {
        SnLessThanOrEqualQuery target = new SnLessThanOrEqualQuery();
        target.setExpression(source.getIndexProperty());
        target.setValue(source.getValue());
        return (AbstractSnQuery)target;
    }


    protected AbstractSnQuery createBoostLessThanQueryFunction(AsBoostRule source)
    {
        SnLessThanQuery target = new SnLessThanQuery();
        target.setExpression(source.getIndexProperty());
        target.setValue(source.getValue());
        return (AbstractSnQuery)target;
    }


    protected void updateSearchQuerySorts(SnSearchContext context, AsSearchProfileResult result)
    {
        SnSearchQuery searchQuery = context.getSearchRequest().getSearchQuery();
        searchQuery.getAvailableSorts().clear();
        MergeMap<String, AsConfigurationHolder<AsPromotedSort, AbstractAsSortConfiguration>> resultPromotedSorts = (MergeMap<String, AsConfigurationHolder<AsPromotedSort, AbstractAsSortConfiguration>>)result.getPromotedSorts();
        if(MapUtils.isNotEmpty((Map)resultPromotedSorts))
        {
            for(AsConfigurationHolder<AsPromotedSort, AbstractAsSortConfiguration> resultPromotedSort : (Iterable<AsConfigurationHolder<AsPromotedSort, AbstractAsSortConfiguration>>)resultPromotedSorts
                            .orderedValues())
            {
                searchQuery.getAvailableSorts().add(createSort((AsConfigurationHolder)resultPromotedSort));
            }
        }
        MergeMap<String, AsConfigurationHolder<AsSort, AbstractAsSortConfiguration>> resultSorts = (MergeMap<String, AsConfigurationHolder<AsSort, AbstractAsSortConfiguration>>)result.getSorts();
        if(MapUtils.isNotEmpty((Map)resultSorts))
        {
            for(AsConfigurationHolder<AsSort, AbstractAsSortConfiguration> resultSort : (Iterable<AsConfigurationHolder<AsSort, AbstractAsSortConfiguration>>)resultSorts.orderedValues())
            {
                searchQuery.getAvailableSorts().add(createSort((AsConfigurationHolder)resultSort));
            }
        }
    }


    protected SnSort createSort(AsConfigurationHolder<? extends AbstractAsSortConfiguration, AbstractAsSortConfiguration> sortHolder)
    {
        if(sortHolder.getData() instanceof SnSort)
        {
            return (SnSort)sortHolder.getData();
        }
        LanguageModel currentLanguage = this.commonI18NService.getCurrentLanguage();
        AbstractAsSortConfiguration source = (AbstractAsSortConfiguration)sortHolder.getConfiguration();
        SnSort sort = new SnSort();
        sort.setId(source.getCode());
        sort.setName((String)source.getName().get(currentLanguage.getIsocode()));
        sort.setApplyPromotedHits(Boolean.valueOf(source.isApplyPromotedItems()));
        sort.setHighlightPromotedHits(Boolean.valueOf(source.isHighlightPromotedItems()));
        sort.setExpressions(ConverterUtils.convertAll(source.getExpressions(), this::createSortExpression));
        return sort;
    }


    protected SnSortExpression createSortExpression(AsSortExpression source)
    {
        SnSortExpression sortExpression = new SnSortExpression();
        sortExpression.setExpression(source.getExpression());
        sortExpression.setAscending(Boolean.valueOf(Objects.equals(source.getOrder(), AsSortOrder.ASCENDING)));
        return sortExpression;
    }


    protected void updateSearchQueryGroup(SnSearchContext context, AsSearchProfileResult result)
    {
        SnSearchQuery searchQuery = context.getSearchRequest().getSearchQuery();
        searchQuery.setGroup(null);
        AsConfigurationHolder<AsGroup, AsGroup> groupHolder = result.getGroup();
        if(groupHolder != null)
        {
            SnGroupRequest group = createGroup(groupHolder);
            if(group != null && StringUtils.isNotBlank(group.getExpression()))
            {
                searchQuery.setGroup(group);
            }
        }
    }


    protected SnGroupRequest createGroup(AsConfigurationHolder<AsGroup, AsGroup> groupHolder)
    {
        if(groupHolder.getData() instanceof SnGroupRequest)
        {
            return (SnGroupRequest)groupHolder.getData();
        }
        AsGroup source = (AsGroup)groupHolder.getConfiguration();
        SnGroupRequest group = new SnGroupRequest();
        group.setExpression(source.getExpression());
        group.setLimit(source.getLimit());
        return group;
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


    public SnAsCatalogVersionResolver getSnAsCatalogVersionResolver()
    {
        return this.snAsCatalogVersionResolver;
    }


    @Required
    public void setSnAsCatalogVersionResolver(SnAsCatalogVersionResolver snAsCatalogVersionResolver)
    {
        this.snAsCatalogVersionResolver = snAsCatalogVersionResolver;
    }


    public SnAsCategoryPathResolver getSnAsCategoryPathResolver()
    {
        return this.snAsCategoryPathResolver;
    }


    @Required
    public void setSnAsCategoryPathResolver(SnAsCategoryPathResolver snAsCategoryPathResolver)
    {
        this.snAsCategoryPathResolver = snAsCategoryPathResolver;
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


    public SnQualifierTypeFactory getSnQualifierTypeFactory()
    {
        return this.snQualifierTypeFactory;
    }


    @Required
    public void setSnQualifierTypeFactory(SnQualifierTypeFactory snQualifierTypeFactory)
    {
        this.snQualifierTypeFactory = snQualifierTypeFactory;
    }


    public ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }
}
