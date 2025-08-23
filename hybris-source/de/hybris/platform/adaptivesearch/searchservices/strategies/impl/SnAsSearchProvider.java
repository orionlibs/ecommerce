package de.hybris.platform.adaptivesearch.searchservices.strategies.impl;

import de.hybris.platform.adaptivesearch.AsException;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsDocumentData;
import de.hybris.platform.adaptivesearch.data.AsExpressionData;
import de.hybris.platform.adaptivesearch.data.AsFacetData;
import de.hybris.platform.adaptivesearch.data.AsFacetSortData;
import de.hybris.platform.adaptivesearch.data.AsFacetValueData;
import de.hybris.platform.adaptivesearch.data.AsIndexConfigurationData;
import de.hybris.platform.adaptivesearch.data.AsIndexPropertyData;
import de.hybris.platform.adaptivesearch.data.AsIndexTypeData;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.data.AsSearchQueryData;
import de.hybris.platform.adaptivesearch.data.AsSearchResultData;
import de.hybris.platform.adaptivesearch.data.AsSortData;
import de.hybris.platform.adaptivesearch.enums.AsBoostOperator;
import de.hybris.platform.adaptivesearch.enums.AsBoostType;
import de.hybris.platform.adaptivesearch.enums.AsFacetType;
import de.hybris.platform.adaptivesearch.strategies.AsFeatureFlag;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.searchservices.admin.data.SnExpressionInfo;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.admin.data.SnFieldTypeInfo;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnIndexType;
import de.hybris.platform.searchservices.admin.service.SnCommonConfigurationService;
import de.hybris.platform.searchservices.admin.service.SnFieldTypeRegistry;
import de.hybris.platform.searchservices.admin.service.SnIndexConfigurationService;
import de.hybris.platform.searchservices.admin.service.SnIndexTypeService;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.core.service.SnContextFactory;
import de.hybris.platform.searchservices.core.service.SnQualifier;
import de.hybris.platform.searchservices.core.service.SnQualifierType;
import de.hybris.platform.searchservices.core.service.SnQualifierTypeFactory;
import de.hybris.platform.searchservices.core.service.SnQueryContextFactory;
import de.hybris.platform.searchservices.core.service.SnSessionService;
import de.hybris.platform.searchservices.search.data.AbstractSnBucketResponse;
import de.hybris.platform.searchservices.search.data.AbstractSnFacetFilter;
import de.hybris.platform.searchservices.search.data.AbstractSnFacetResponse;
import de.hybris.platform.searchservices.search.data.AbstractSnQuery;
import de.hybris.platform.searchservices.search.data.SnBucketsFacetFilter;
import de.hybris.platform.searchservices.search.data.SnFilter;
import de.hybris.platform.searchservices.search.data.SnMatchTermQuery;
import de.hybris.platform.searchservices.search.data.SnNamedSort;
import de.hybris.platform.searchservices.search.data.SnRangeBucketsFacetResponse;
import de.hybris.platform.searchservices.search.data.SnSearchHit;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import de.hybris.platform.searchservices.search.data.SnSearchResult;
import de.hybris.platform.searchservices.search.data.SnSort;
import de.hybris.platform.searchservices.search.data.SnTermBucketsFacetResponse;
import de.hybris.platform.searchservices.search.service.SnSearchRequest;
import de.hybris.platform.searchservices.search.service.SnSearchResponse;
import de.hybris.platform.searchservices.search.service.SnSearchService;
import de.hybris.platform.searchservices.util.ConverterUtils;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class SnAsSearchProvider implements AsSearchProvider, InitializingBean
{
    protected static final Set<AsFeatureFlag> SUPPORTED_FEATURES = EnumSet.of(AsFeatureFlag.FACET_TOP_VALUES, AsFeatureFlag.FACET_SORT, AsFeatureFlag.FACET_VALUE_RANGES);
    protected static final List<AsFacetType> SUPPORTED_FACET_TYPES = List.of(AsFacetType.REFINE, AsFacetType.MULTISELECT);
    protected static final String FACET_SORT_KEY_PREFIX = "searchservices.search.termbucketsfacet.sort.";
    private TypeService typeService;
    private I18NService i18nService;
    private L10NService l10nService;
    private CommonI18NService commonI18NService;
    private CatalogVersionService catalogVersionService;
    private SnSessionService snSessionService;
    private SnQueryContextFactory snQueryContextFactory;
    private SnFieldTypeRegistry snFieldTypeRegistry;
    private SnIndexConfigurationService snIndexConfigurationService;
    private SnIndexTypeService snIndexTypeService;
    private SnCommonConfigurationService snCommonConfigurationService;
    private SnQualifierTypeFactory snQualifierTypeFactory;
    private SnContextFactory snContextFactory;
    private SnSearchService snSearchService;
    private final Map<String, AsBoostOperator> boostOperatorMapping = new HashMap<>();


    public void afterPropertiesSet() throws Exception
    {
        populateBoostOperatorMapping();
    }


    protected void populateBoostOperatorMapping()
    {
        this.boostOperatorMapping.put("match", AsBoostOperator.MATCH);
        this.boostOperatorMapping.put("eq", AsBoostOperator.EQUAL);
        this.boostOperatorMapping.put("gt", AsBoostOperator.GREATER_THAN);
        this.boostOperatorMapping.put("ge", AsBoostOperator.GREATER_THAN_OR_EQUAL);
        this.boostOperatorMapping.put("lt", AsBoostOperator.LESS_THAN);
        this.boostOperatorMapping.put("le", AsBoostOperator.LESS_THAN_OR_EQUAL);
    }


    protected Map<String, AsBoostOperator> getBoostOperatorMapping()
    {
        return this.boostOperatorMapping;
    }


    public Set<AsFeatureFlag> getSupportedFeatures(String indexType)
    {
        return SUPPORTED_FEATURES;
    }


    public List<String> getSupportedQueryContexts(String indexType)
    {
        return this.snQueryContextFactory.getQueryContexts();
    }


    public List<AsIndexConfigurationData> getIndexConfigurations()
    {
        List<SnIndexConfiguration> indexConfigurations = this.snIndexConfigurationService.getAllIndexConfigurations();
        return ConverterUtils.convertAll(indexConfigurations, this::convertIndexConfiguration);
    }


    public Optional<AsIndexConfigurationData> getIndexConfigurationForCode(String id)
    {
        Optional<SnIndexConfiguration> indexConfigurationOptional = this.snIndexConfigurationService.getIndexConfigurationForId(id);
        return indexConfigurationOptional.map(this::convertIndexConfiguration);
    }


    public List<AsIndexTypeData> getIndexTypes()
    {
        List<SnIndexType> indexTypes = this.snIndexTypeService.getAllIndexTypes();
        return ConverterUtils.convertAll(indexTypes, this::convertIndexType);
    }


    public List<AsIndexTypeData> getIndexTypes(String indexConfigurationId)
    {
        List<SnIndexType> indexTypes = this.snIndexTypeService.getIndexTypesForIndexConfiguration(indexConfigurationId);
        return ConverterUtils.convertAll(indexTypes, this::convertIndexType);
    }


    public Optional<AsIndexTypeData> getIndexTypeForCode(String id)
    {
        Optional<SnIndexType> indexTypeOptional = this.snIndexTypeService.getIndexTypeForId(id);
        return indexTypeOptional.map(this::convertIndexType);
    }


    public List<AsIndexPropertyData> getIndexProperties(String indexTypeId)
    {
        SnIndexType indexType = this.snIndexTypeService.getIndexTypeForId(indexTypeId).orElseThrow();
        return ConverterUtils.convertAll(indexType.getFields().values(), this::convertIndexProperty);
    }


    public Optional<AsIndexPropertyData> getIndexPropertyForCode(String indexTypeId, String id)
    {
        SnIndexType indexType = this.snIndexTypeService.getIndexTypeForId(indexTypeId).orElseThrow();
        return Optional.<SnField>ofNullable((SnField)indexType.getFields().get(id)).map(this::convertIndexProperty);
    }


    public List<CatalogVersionModel> getSupportedCatalogVersions(String indexConfigurationId, String indexTypeId)
    {
        return this.snCommonConfigurationService.getCatalogVersions(indexTypeId);
    }


    public List<LanguageModel> getSupportedLanguages(String indexConfigurationId, String indexTypeId)
    {
        return this.snCommonConfigurationService.getLanguages(indexTypeId);
    }


    public List<CurrencyModel> getSupportedCurrencies(String indexConfigurationId, String indexTypeId)
    {
        return this.snCommonConfigurationService.getCurrencies(indexTypeId);
    }


    public List<AsExpressionData> getSupportedFacetExpressions(String indexTypeId)
    {
        return (List<AsExpressionData>)this.snCommonConfigurationService.getFacetExpressions(indexTypeId).stream().map(this::convertExpression)
                        .sorted(this::compareExpressions).collect(Collectors.toList());
    }


    public boolean isValidFacetExpression(String indexTypeId, String expression)
    {
        return this.snCommonConfigurationService.isValidFacetExpression(indexTypeId, expression);
    }


    public List<AsFacetType> getSupportedFacetTypes(String indexType)
    {
        return SUPPORTED_FACET_TYPES;
    }


    public boolean isValidFacetType(String indexType, AsFacetType facetType)
    {
        return getSupportedFacetTypes(indexType).contains(facetType);
    }


    public List<AsFacetSortData> getSupportedFacetSorts(String indexType)
    {
        List<AsFacetSortData> facetSorts = new ArrayList<>();
        facetSorts.add(createFacetSort("_count_desc_"));
        facetSorts.add(createFacetSort("_id_asc_"));
        facetSorts.add(createFacetSort("_id_desc_"));
        facetSorts.add(createFacetSort("_name_asc_"));
        facetSorts.add(createFacetSort("_name_desc_"));
        return facetSorts;
    }


    protected AsFacetSortData createFacetSort(String sortId)
    {
        AsFacetSortData facetSort = new AsFacetSortData();
        facetSort.setCode(sortId);
        facetSort.setName(this.l10nService.getLocalizedString("searchservices.search.termbucketsfacet.sort." + sortId));
        return facetSort;
    }


    public boolean isValidFacetSort(String indexType, String sort)
    {
        return SearchservicesConstants.TERM_BUCKETS_FACET_SORTS.contains(sort);
    }


    public List<AsBoostType> getSupportedBoostTypes(String indexType)
    {
        return List.of(AsBoostType.MULTIPLICATIVE);
    }


    public boolean isValidBoostType(String indexType, AsBoostType boostType)
    {
        return getSupportedBoostTypes(indexType).contains(boostType);
    }


    public List<AsExpressionData> getSupportedSortExpressions(String indexTypeId)
    {
        return (List<AsExpressionData>)this.snCommonConfigurationService.getSortExpressions(indexTypeId).stream().map(this::convertExpression)
                        .sorted(this::compareExpressions).collect(Collectors.toList());
    }


    public boolean isValidSortExpression(String indexTypeId, String expression)
    {
        return this.snCommonConfigurationService.isValidSortExpression(indexTypeId, expression);
    }


    public List<AsExpressionData> getSupportedGroupExpressions(String indexTypeId)
    {
        return (List<AsExpressionData>)this.snCommonConfigurationService.getGroupExpressions(indexTypeId).stream().map(this::convertExpression)
                        .sorted(this::compareExpressions).collect(Collectors.toList());
    }


    public boolean isValidGroupExpression(String indexTypeId, String expression)
    {
        return this.snCommonConfigurationService.isValidGroupExpression(indexTypeId, expression);
    }


    public List<String> getAvailableQualifiers(String indexType, String indexPropertyId) throws AsException
    {
        try
        {
            SnContext context = this.snContextFactory.createContext(indexType);
            SnField field = (SnField)context.getIndexType().getFields().get(indexPropertyId);
            Optional<SnQualifierType> qualifierType = this.snQualifierTypeFactory.getQualifierType(context, field);
            if(qualifierType.isPresent())
            {
                List<SnQualifier> availableQualifiers = ((SnQualifierType)qualifierType.get()).getQualifierProvider().getAvailableQualifiers(context);
                return (List<String>)((availableQualifiers == null) ? Stream.<T>empty() : (Stream)availableQualifiers.stream())
                                .map(SnQualifier::getId).collect(Collectors.toList());
            }
            return Collections.emptyList();
        }
        catch(SnException e)
        {
            throw new AsException(e);
        }
    }


    public AsSearchResultData search(AsSearchProfileContext context, AsSearchQueryData searchQuery) throws AsException
    {
        try
        {
            this.snSessionService.initializeSession();
            if(context.getLanguage() != null)
            {
                this.commonI18NService.setCurrentLanguage(context.getLanguage());
            }
            if(context.getCurrency() != null)
            {
                this.commonI18NService.setCurrentCurrency(context.getCurrency());
            }
            if(context.getCatalogVersions() != null)
            {
                this.catalogVersionService.setSessionCatalogVersions(context.getCatalogVersions());
            }
            return performSearch(context, searchQuery);
        }
        finally
        {
            this.snSessionService.destroySession();
        }
    }


    public AsSearchResultData performSearch(AsSearchProfileContext context, AsSearchQueryData query) throws AsException
    {
        try
        {
            SnSearchQuery searchQuery = convertSearchQuery(context, query);
            SnSearchRequest searchRequest = this.snSearchService.createSearchRequest(context.getIndexType(), searchQuery);
            SnSearchResponse searchResponse = this.snSearchService.search(searchRequest);
            return convertSearchResult(searchResponse.getSearchResult());
        }
        catch(SnException e)
        {
            throw new AsException(e);
        }
    }


    protected SnSearchQuery convertSearchQuery(AsSearchProfileContext context, AsSearchQueryData source)
    {
        SnSearchQuery target = new SnSearchQuery();
        target.setQuery(source.getQuery());
        target.setOffset(Integer.valueOf(source.getActivePage() * source.getPageSize()));
        target.setLimit(Integer.valueOf(source.getPageSize()));
        if(MapUtils.isNotEmpty(source.getFacetValues()))
        {
            List<AbstractSnFacetFilter> facetFilters = new ArrayList<>();
            for(Map.Entry<String, Set<String>> entry : (Iterable<Map.Entry<String, Set<String>>>)source.getFacetValues().entrySet())
            {
                String expression = entry.getKey();
                Set<String> facetValues = entry.getValue();
                if(StringUtils.isNotBlank(expression) && CollectionUtils.isNotEmpty(facetValues))
                {
                    SnBucketsFacetFilter facetFilter = new SnBucketsFacetFilter();
                    facetFilter.setFacetId(expression);
                    facetFilter.setBucketIds(new ArrayList<>(facetValues));
                    facetFilters.add(facetFilter);
                }
            }
            target.setFacetFilters(facetFilters);
        }
        CategoryModel selectedCategory = resolveSelectedCategory(context.getCategoryPath());
        if(selectedCategory != null)
        {
            if(target.getFilters() == null)
            {
                target.setFilters(new ArrayList());
            }
            SnMatchTermQuery categoryQuery = new SnMatchTermQuery();
            categoryQuery.setExpression("allCategories");
            categoryQuery.setValue(selectedCategory.getCode());
            SnFilter categoryFilter = new SnFilter();
            categoryFilter.setQuery((AbstractSnQuery)categoryQuery);
            target.getFilters().add(categoryFilter);
        }
        if(StringUtils.isNotBlank(source.getSort()))
        {
            SnSort sort = new SnSort();
            sort.setId(source.getSort());
            target.setSort(sort);
        }
        return target;
    }


    protected CategoryModel resolveSelectedCategory(List<CategoryModel> categoryPath)
    {
        if(CollectionUtils.isEmpty(categoryPath))
        {
            return null;
        }
        return categoryPath.get(categoryPath.size() - 1);
    }


    protected AsSearchResultData convertSearchResult(SnSearchResult source)
    {
        AsSearchResultData target = new AsSearchResultData();
        target.setActivePage((source.getLimit().intValue() <= 0) ? 0 : (source.getOffset().intValue() / source.getLimit().intValue()));
        target.setPageCount((source.getLimit().intValue() <= 0) ? 0 : ((source.getTotalSize().intValue() + source.getLimit().intValue() - 1) / source.getLimit().intValue()));
        target.setPageSize(source.getLimit().intValue());
        target.setResultCount(source.getTotalSize().intValue());
        target.setResults(ConverterUtils.convertAll(source.getSearchHits(), this::convertDocument));
        target.setFacets(ConverterUtils.convertAll(source.getFacets(), this::convertFacetResponse));
        target.setCurrentSort((source.getSort() != null) ? convertNamedSort(source.getSort()) : null);
        target.setAvailableSorts(ConverterUtils.convertAll(source.getAvailableSorts(), this::convertNamedSort));
        Map<String, Object> debug = source.getDebug();
        AsSearchProfileContext searchProfileContext = (AsSearchProfileContext)debug.get("adaptiveSearchContext");
        AsSearchProfileResult searchProfileResult = (AsSearchProfileResult)debug.get("adaptiveSearchResult");
        target.setSearchProfileResult(searchProfileResult);
        if(searchProfileContext != null)
        {
            target.setCatalogVersions(searchProfileContext.getCatalogVersions());
            target.setCategoryPath(searchProfileContext.getCategoryPath());
        }
        return target;
    }


    protected AsIndexConfigurationData convertIndexConfiguration(SnIndexConfiguration source)
    {
        Locale locale = this.i18nService.getCurrentLocale();
        AsIndexConfigurationData target = new AsIndexConfigurationData();
        target.setCode(source.getId());
        target.setName((source.getName() != null) ? (String)source.getName().get(locale) : null);
        return target;
    }


    protected AsIndexTypeData convertIndexType(SnIndexType source)
    {
        Locale locale = this.i18nService.getCurrentLocale();
        AsIndexTypeData target = new AsIndexTypeData();
        target.setCode(source.getId());
        target.setName((source.getName() != null) ? (String)source.getName().get(locale) : null);
        ComposedTypeModel itemType = this.typeService.getComposedTypeForCode(source.getItemComposedType());
        target.setItemType(itemType.getCode());
        target.setCatalogVersionAware(BooleanUtils.isTrue(itemType.getCatalogItemType()));
        return target;
    }


    protected AsIndexPropertyData convertIndexProperty(SnField source)
    {
        Locale locale = this.i18nService.getCurrentLocale();
        SnFieldTypeInfo fieldTypeInfo = this.snFieldTypeRegistry.getFieldTypeInfo(source.getFieldType());
        AsIndexPropertyData target = new AsIndexPropertyData();
        target.setCode(source.getId());
        target.setName((source.getName() != null) ? (String)source.getName().get(locale) : null);
        target.setType(fieldTypeInfo.getValueType());
        Objects.requireNonNull(this.boostOperatorMapping);
        target.setSupportedBoostOperators((Set)fieldTypeInfo.getSupportedQueryTypes().stream().map(this.boostOperatorMapping::get)
                        .filter(Objects::nonNull).collect(Collectors.toCollection(java.util.LinkedHashSet::new)));
        return target;
    }


    protected AsExpressionData convertExpression(SnExpressionInfo source)
    {
        Locale locale = this.i18nService.getCurrentLocale();
        AsExpressionData target = new AsExpressionData();
        target.setExpression(source.getExpression());
        target.setName((source.getName() != null) ? (String)source.getName().get(locale) : null);
        return target;
    }


    protected int compareExpressions(AsExpressionData expression1, AsExpressionData expression2)
    {
        return expression1.getExpression().compareTo(expression2.getExpression());
    }


    protected AsDocumentData convertDocument(SnSearchHit source)
    {
        HashMap<String, Object> fields = new HashMap<>(source.getFields());
        fields.put("_score_", source.getScore());
        AsDocumentData target = new AsDocumentData();
        target.setId(source.getId());
        target.setScore(source.getScore());
        target.setPk(extractPkFromSearchHit(source));
        target.setFields(fields);
        target.setTags(source.getTags());
        target.setInnerDocuments(ConverterUtils.convertAll(source.getInnerHits(), this::convertDocument));
        return target;
    }


    protected AsFacetData convertFacetResponse(AbstractSnFacetResponse source)
    {
        if(source instanceof SnTermBucketsFacetResponse)
        {
            return convertTermBucketsFacetResponse((SnTermBucketsFacetResponse)source);
        }
        if(source instanceof SnRangeBucketsFacetResponse)
        {
            return convertRangeBucketsFacetResponse((SnRangeBucketsFacetResponse)source);
        }
        return null;
    }


    protected AsFacetData convertTermBucketsFacetResponse(SnTermBucketsFacetResponse source)
    {
        Set<String> selectedBucketIds = (Set<String>)CollectionUtils.emptyIfNull(source.getSelectedBuckets()).stream().map(AbstractSnBucketResponse::getId).collect(Collectors.toSet());
        AsFacetData target = new AsFacetData();
        target.setIndexProperty(source.getExpression());
        target.setName(source.getName());
        target.setTopValues(ConverterUtils.convertAll(source.getTopBuckets(), bucket -> convertBucketResponse((AbstractSnBucketResponse)bucket, selectedBucketIds)));
        target.setValues(ConverterUtils.convertAll(source.getBuckets(), bucket -> convertBucketResponse((AbstractSnBucketResponse)bucket, selectedBucketIds)));
        target.setSelectedValues(
                        ConverterUtils.convertAll(source.getSelectedBuckets(), bucket -> convertBucketResponse((AbstractSnBucketResponse)bucket, selectedBucketIds)));
        return target;
    }


    protected AsFacetData convertRangeBucketsFacetResponse(SnRangeBucketsFacetResponse source)
    {
        Set<String> selectedBucketIds = (Set<String>)CollectionUtils.emptyIfNull(source.getSelectedBuckets()).stream().map(AbstractSnBucketResponse::getId).collect(Collectors.toSet());
        AsFacetData target = new AsFacetData();
        target.setIndexProperty(source.getExpression());
        target.setName(source.getName());
        target.setValues(ConverterUtils.convertAll(source.getBuckets(), bucket -> convertBucketResponse((AbstractSnBucketResponse)bucket, selectedBucketIds)));
        target.setSelectedValues(
                        ConverterUtils.convertAll(source.getSelectedBuckets(), bucket -> convertBucketResponse((AbstractSnBucketResponse)bucket, selectedBucketIds)));
        return target;
    }


    protected AsFacetValueData convertBucketResponse(AbstractSnBucketResponse source, Set<String> selectedBucketIds)
    {
        if(source == null)
        {
            return null;
        }
        AsFacetValueData target = new AsFacetValueData();
        target.setValue(source.getId());
        target.setName(source.getName());
        target.setCount((source.getCount() != null) ? source.getCount().intValue() : 0L);
        target.setSelected(selectedBucketIds.contains(source.getId()));
        return target;
    }


    protected AsSortData convertNamedSort(SnNamedSort source)
    {
        AsSortData target = new AsSortData();
        target.setCode(source.getId());
        target.setName(source.getName());
        return target;
    }


    protected PK extractPkFromSearchHit(SnSearchHit searchHit)
    {
        Object pk = searchHit.getFields().get("pk");
        if(pk instanceof Long)
        {
            return PK.fromLong(((Long)pk).longValue());
        }
        if(pk instanceof String)
        {
            return PK.fromLong(Long.valueOf((String)pk).longValue());
        }
        return null;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public I18NService getI18nService()
    {
        return this.i18nService;
    }


    @Required
    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    public L10NService getL10nService()
    {
        return this.l10nService;
    }


    @Required
    public void setL10nService(L10NService l10nService)
    {
        this.l10nService = l10nService;
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


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public SnSessionService getSnSessionService()
    {
        return this.snSessionService;
    }


    @Required
    public void setSnSessionService(SnSessionService snSessionService)
    {
        this.snSessionService = snSessionService;
    }


    public SnQueryContextFactory getSnQueryContextFactory()
    {
        return this.snQueryContextFactory;
    }


    @Required
    public void setSnQueryContextFactory(SnQueryContextFactory snQueryContextFactory)
    {
        this.snQueryContextFactory = snQueryContextFactory;
    }


    public SnFieldTypeRegistry getSnFieldTypeRegistry()
    {
        return this.snFieldTypeRegistry;
    }


    @Required
    public void setSnFieldTypeRegistry(SnFieldTypeRegistry snFieldTypeRegistry)
    {
        this.snFieldTypeRegistry = snFieldTypeRegistry;
    }


    public SnIndexConfigurationService getSnIndexConfigurationService()
    {
        return this.snIndexConfigurationService;
    }


    @Required
    public void setSnIndexConfigurationService(SnIndexConfigurationService snIndexConfigurationService)
    {
        this.snIndexConfigurationService = snIndexConfigurationService;
    }


    public SnIndexTypeService getSnIndexTypeService()
    {
        return this.snIndexTypeService;
    }


    @Required
    public void setSnIndexTypeService(SnIndexTypeService snIndexTypeService)
    {
        this.snIndexTypeService = snIndexTypeService;
    }


    public SnCommonConfigurationService getSnCommonConfigurationService()
    {
        return this.snCommonConfigurationService;
    }


    @Required
    public void setSnCommonConfigurationService(SnCommonConfigurationService snCommonConfigurationService)
    {
        this.snCommonConfigurationService = snCommonConfigurationService;
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


    public SnContextFactory getSnContextFactory()
    {
        return this.snContextFactory;
    }


    @Required
    public void setSnContextFactory(SnContextFactory snContextFactory)
    {
        this.snContextFactory = snContextFactory;
    }


    public SnSearchService getSnSearchService()
    {
        return this.snSearchService;
    }


    @Required
    public void setSnSearchService(SnSearchService snSearchService)
    {
        this.snSearchService = snSearchService;
    }
}
