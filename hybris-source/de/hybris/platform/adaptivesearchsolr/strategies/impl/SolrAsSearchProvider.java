package de.hybris.platform.adaptivesearchsolr.strategies.impl;

import de.hybris.platform.adaptivesearch.AsException;
import de.hybris.platform.adaptivesearch.AsRuntimeException;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsDocumentData;
import de.hybris.platform.adaptivesearch.data.AsExpressionData;
import de.hybris.platform.adaptivesearch.data.AsFacetData;
import de.hybris.platform.adaptivesearch.data.AsFacetSortData;
import de.hybris.platform.adaptivesearch.data.AsFacetValueData;
import de.hybris.platform.adaptivesearch.data.AsFacetVisibility;
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
import de.hybris.platform.adaptivesearchsolr.strategies.SolrAsTypeMappingRegistry;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.common.SolrQueryContextFactory;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.daos.SolrFacetSearchConfigDao;
import de.hybris.platform.solrfacetsearch.daos.SolrIndexedPropertyDao;
import de.hybris.platform.solrfacetsearch.daos.SolrIndexedTypeDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.search.Document;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.FacetValue;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.search.SearchResultGroup;
import de.hybris.platform.solrfacetsearch.search.SearchResultGroupCommand;
import de.hybris.platform.solrfacetsearch.solr.IndexedPropertyTypeInfo;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexedPropertyTypeRegistry;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class SolrAsSearchProvider implements AsSearchProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(SolrAsSearchProvider.class);
    protected static final Set<AsFeatureFlag> SUPPORTED_FEATURES = EnumSet.of(AsFeatureFlag.FACET_VALUES_SORT_PROVIDER, AsFeatureFlag.FACET_VALUES_DISPLAY_NAME_PROVIDER, AsFeatureFlag.FACET_TOP_VALUES_PROVIDER);
    protected static final List<AsFacetType> SUPPORTED_FACET_TYPES = List.of(AsFacetType.REFINE, AsFacetType.MULTISELECT_OR, AsFacetType.MULTISELECT_AND);
    protected static final String INDEX_CONFIGURATION_PARAM = "indexConfiguration";
    protected static final String INDEX_TYPE_PARAM = "indexType";
    protected static final String CODE_PARAM = "code";
    protected static final String EXPRESSION_PARAM = "expression";
    protected static final String DEFAULT_QUERY_TEMPLATE = "DEFAULT";
    protected static final String ADAPTIVE_SEARCH_KEEP_EXCLUDED_VALUES_KEY = "adaptiveSearchKeepExcludedValues";
    protected static final Pattern VALID_INDEX_PROPERTY_NAME_REGEX_PATTERN = Pattern.compile("^(?![0-9_])[a-zA-Z0-9_]*(?<!_)$");
    protected static final String INDEX_PROPERTY_NOT_FOUND = "Index property not found";
    private SolrQueryContextFactory solrQueryContextFactory;
    private SolrFacetSearchConfigDao solrFacetSearchConfigDao;
    private SolrIndexedTypeDao solrIndexedTypeDao;
    private SolrIndexedPropertyDao solrIndexedPropertyDao;
    private SolrIndexedPropertyTypeRegistry solrIndexedPropertyTypeRegistry;
    private SolrAsTypeMappingRegistry solrAsTypeMappingRegistry;
    private FacetSearchService facetSearchService;
    private FacetSearchConfigService facetSearchConfigService;
    private SessionService sessionService;
    private I18NService i18nService;
    private CommonI18NService commonI18NService;


    public Set<AsFeatureFlag> getSupportedFeatures(String indexType)
    {
        return SUPPORTED_FEATURES;
    }


    public List<String> getSupportedQueryContexts(String indexType)
    {
        return this.solrQueryContextFactory.getQueryContexts();
    }


    public List<AsIndexConfigurationData> getIndexConfigurations()
    {
        List<SolrFacetSearchConfigModel> solrIndexConfigurations = this.solrFacetSearchConfigDao.findAllFacetSearchConfigs();
        return (List<AsIndexConfigurationData>)solrIndexConfigurations.stream().map(this::convertIndexConfiguration).collect(Collectors.toList());
    }


    public Optional<AsIndexConfigurationData> getIndexConfigurationForCode(String code)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("code", code);
        try
        {
            SolrFacetSearchConfigModel solrIndexConfiguration = this.solrFacetSearchConfigDao.findFacetSearchConfigByName(code);
            return Optional.of(convertIndexConfiguration(solrIndexConfiguration));
        }
        catch(UnknownIdentifierException e)
        {
            LOG.debug("Index configuration not found", (Throwable)e);
            return Optional.empty();
        }
    }


    public List<AsIndexTypeData> getIndexTypes()
    {
        List<SolrIndexedTypeModel> solrIndexTypes = this.solrIndexedTypeDao.findAllIndexedTypes();
        return (List<AsIndexTypeData>)solrIndexTypes.stream().map(this::convertIndexType).collect(Collectors.toList());
    }


    public List<AsIndexTypeData> getIndexTypes(String indexConfiguration)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("indexConfiguration", indexConfiguration);
        SolrFacetSearchConfigModel solrIndexConfiguration = this.solrFacetSearchConfigDao.findFacetSearchConfigByName(indexConfiguration);
        List<SolrIndexedTypeModel> solrIndexTypes = solrIndexConfiguration.getSolrIndexedTypes();
        if(CollectionUtils.isEmpty(solrIndexTypes))
        {
            return Collections.emptyList();
        }
        return (List<AsIndexTypeData>)solrIndexTypes.stream().map(this::convertIndexType).collect(Collectors.toList());
    }


    public Optional<AsIndexTypeData> getIndexTypeForCode(String code)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("code", code);
        try
        {
            SolrIndexedTypeModel solrIndexType = this.solrIndexedTypeDao.findIndexedTypeByIdentifier(code);
            return Optional.of(convertIndexType(solrIndexType));
        }
        catch(UnknownIdentifierException e)
        {
            LOG.debug("Index type not found", (Throwable)e);
            return Optional.empty();
        }
    }


    public List<AsIndexPropertyData> getIndexProperties(String indexType)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("indexType", indexType);
        SolrIndexedTypeModel solrIndexType = this.solrIndexedTypeDao.findIndexedTypeByIdentifier(indexType);
        List<SolrIndexedPropertyModel> solrIndexProperties = this.solrIndexedPropertyDao.findIndexedPropertiesByIndexedType(solrIndexType);
        return (List<AsIndexPropertyData>)solrIndexProperties.stream().map(this::convertIndexProperty).sorted(this::compareIndexProperties)
                        .collect(Collectors.toList());
    }


    public Optional<AsIndexPropertyData> getIndexPropertyForCode(String indexType, String code)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("indexType", indexType);
        ServicesUtil.validateParameterNotNullStandardMessage("code", code);
        try
        {
            SolrIndexedTypeModel solrIndexType = this.solrIndexedTypeDao.findIndexedTypeByIdentifier(indexType);
            SolrIndexedPropertyModel solrIndexProperty = this.solrIndexedPropertyDao.findIndexedPropertyByName(solrIndexType, code);
            return Optional.of(convertIndexProperty(solrIndexProperty));
        }
        catch(UnknownIdentifierException e)
        {
            LOG.debug("Index property not found", (Throwable)e);
            return Optional.empty();
        }
    }


    public List<CatalogVersionModel> getSupportedCatalogVersions(String indexConfiguration, String indexType)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("indexConfiguration", indexConfiguration);
        ServicesUtil.validateParameterNotNullStandardMessage("indexType", indexType);
        SolrFacetSearchConfigModel solrIndexConfiguration = this.solrFacetSearchConfigDao.findFacetSearchConfigByName(indexConfiguration);
        return solrIndexConfiguration.getCatalogVersions();
    }


    public List<LanguageModel> getSupportedLanguages(String indexConfiguration, String indexType)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("indexConfiguration", indexConfiguration);
        ServicesUtil.validateParameterNotNullStandardMessage("indexType", indexType);
        SolrFacetSearchConfigModel solrIndexConfiguration = this.solrFacetSearchConfigDao.findFacetSearchConfigByName(indexConfiguration);
        return solrIndexConfiguration.getLanguages();
    }


    public List<CurrencyModel> getSupportedCurrencies(String indexConfiguration, String indexType)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("indexConfiguration", indexConfiguration);
        ServicesUtil.validateParameterNotNullStandardMessage("indexType", indexType);
        SolrFacetSearchConfigModel solrIndexConfiguration = this.solrFacetSearchConfigDao.findFacetSearchConfigByName(indexConfiguration);
        return solrIndexConfiguration.getCurrencies();
    }


    public List<AsExpressionData> getSupportedFacetExpressions(String indexType)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("indexType", indexType);
        SolrIndexedTypeModel solrIndexType = this.solrIndexedTypeDao.findIndexedTypeByIdentifier(indexType);
        List<SolrIndexedPropertyModel> solrIndexProperties = this.solrIndexedPropertyDao.findIndexedPropertiesByIndexedType(solrIndexType);
        return (List<AsExpressionData>)solrIndexProperties.stream().filter(this::isValidFacetIndexProperty).map(this::convertExpression)
                        .sorted(this::compareExpressions).collect(Collectors.toList());
    }


    public boolean isValidFacetExpression(String indexType, String expression)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("indexType", indexType);
        ServicesUtil.validateParameterNotNullStandardMessage("expression", expression);
        try
        {
            SolrIndexedTypeModel solrIndexType = this.solrIndexedTypeDao.findIndexedTypeByIdentifier(indexType);
            SolrIndexedPropertyModel solrIndexProperty = this.solrIndexedPropertyDao.findIndexedPropertyByName(solrIndexType, expression);
            return isValidFacetIndexProperty(solrIndexProperty);
        }
        catch(UnknownIdentifierException e)
        {
            LOG.debug("Index property not found", (Throwable)e);
            return false;
        }
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
        return Collections.emptyList();
    }


    public boolean isValidFacetSort(String indexType, String sort)
    {
        return false;
    }


    protected boolean isValidFacetIndexProperty(SolrIndexedPropertyModel indexProperty)
    {
        IndexedPropertyTypeInfo indexedPropertyType = this.solrIndexedPropertyTypeRegistry.getIndexPropertyTypeInfo(indexProperty.getType().getCode());
        return indexedPropertyType.isAllowFacet();
    }


    public List<AsBoostType> getSupportedBoostTypes(String indexType)
    {
        return List.of(AsBoostType.MULTIPLICATIVE, AsBoostType.ADDITIVE);
    }


    public boolean isValidBoostType(String indexType, AsBoostType boostType)
    {
        return getSupportedBoostTypes(indexType).contains(boostType);
    }


    public List<AsExpressionData> getSupportedSortExpressions(String indexType)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("indexType", indexType);
        SolrIndexedTypeModel solrIndexType = this.solrIndexedTypeDao.findIndexedTypeByIdentifier(indexType);
        List<SolrIndexedPropertyModel> solrIndexProperties = this.solrIndexedPropertyDao.findIndexedPropertiesByIndexedType(solrIndexType);
        List<AsExpressionData> expressions = (List<AsExpressionData>)solrIndexProperties.stream().filter(this::isValidSortIndexProperty).map(this::convertExpression).sorted(this::compareExpressions).collect(Collectors.toList());
        expressions.add(0, createScoreSortExpression());
        return expressions;
    }


    public boolean isValidSortExpression(String indexType, String expression)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("indexType", indexType);
        ServicesUtil.validateParameterNotNullStandardMessage("expression", expression);
        if(StringUtils.equals("score", expression))
        {
            return true;
        }
        try
        {
            SolrIndexedTypeModel solrIndexType = this.solrIndexedTypeDao.findIndexedTypeByIdentifier(indexType);
            SolrIndexedPropertyModel solrIndexProperty = this.solrIndexedPropertyDao.findIndexedPropertyByName(solrIndexType, expression);
            return isValidSortIndexProperty(solrIndexProperty);
        }
        catch(UnknownIdentifierException e)
        {
            LOG.debug("Index property not found", (Throwable)e);
            return false;
        }
    }


    protected boolean isValidSortIndexProperty(SolrIndexedPropertyModel indexProperty)
    {
        if(indexProperty.isMultiValue())
        {
            return false;
        }
        return VALID_INDEX_PROPERTY_NAME_REGEX_PATTERN.matcher(indexProperty.getName()).matches();
    }


    public List<AsExpressionData> getSupportedGroupExpressions(String indexType)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("indexType", indexType);
        SolrIndexedTypeModel solrIndexType = this.solrIndexedTypeDao.findIndexedTypeByIdentifier(indexType);
        List<SolrIndexedPropertyModel> solrIndexProperties = this.solrIndexedPropertyDao.findIndexedPropertiesByIndexedType(solrIndexType);
        return (List<AsExpressionData>)solrIndexProperties.stream().filter(this::isValidGroupIndexProperty).map(this::convertExpression)
                        .sorted(this::compareExpressions).collect(Collectors.toList());
    }


    public boolean isValidGroupExpression(String indexType, String expression)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("indexType", indexType);
        ServicesUtil.validateParameterNotNullStandardMessage("expression", expression);
        try
        {
            SolrIndexedTypeModel solrIndexType = this.solrIndexedTypeDao.findIndexedTypeByIdentifier(indexType);
            SolrIndexedPropertyModel solrIndexProperty = this.solrIndexedPropertyDao.findIndexedPropertyByName(solrIndexType, expression);
            return isValidGroupIndexProperty(solrIndexProperty);
        }
        catch(UnknownIdentifierException e)
        {
            LOG.debug("Index property not found", (Throwable)e);
            return false;
        }
    }


    protected boolean isValidGroupIndexProperty(SolrIndexedPropertyModel indexProperty)
    {
        if(indexProperty.isMultiValue())
        {
            return false;
        }
        IndexedPropertyTypeInfo indexedPropertyType = this.solrIndexedPropertyTypeRegistry.getIndexPropertyTypeInfo(indexProperty.getType().getCode());
        return indexedPropertyType.isAllowGroup();
    }


    protected AsIndexConfigurationData convertIndexConfiguration(SolrFacetSearchConfigModel solrIndexConfiguration)
    {
        AsIndexConfigurationData indexConfiguration = new AsIndexConfigurationData();
        indexConfiguration.setCode(solrIndexConfiguration.getName());
        indexConfiguration.setName((solrIndexConfiguration.getDescription() != null) ? solrIndexConfiguration.getDescription() :
                        solrIndexConfiguration.getName());
        return indexConfiguration;
    }


    protected AsIndexTypeData convertIndexType(SolrIndexedTypeModel source)
    {
        ComposedTypeModel itemType = source.getType();
        AsIndexTypeData target = new AsIndexTypeData();
        target.setCode(source.getIdentifier());
        target.setName(source.getIdentifier());
        target.setItemType(itemType.getCode());
        target.setCatalogVersionAware(BooleanUtils.isTrue(itemType.getCatalogItemType()));
        return target;
    }


    protected AsIndexPropertyData convertIndexProperty(SolrIndexedPropertyModel source)
    {
        AsIndexPropertyData target = new AsIndexPropertyData();
        target.setCode(source.getName());
        target.setName(source.getDisplayName());
        IndexedPropertyTypeInfo indexedPropertyType = this.solrIndexedPropertyTypeRegistry.getIndexPropertyTypeInfo(source.getType().getCode());
        target.setType(indexedPropertyType.getJavaType());
        if(CollectionUtils.isNotEmpty(indexedPropertyType.getSupportedQueryOperators()))
        {
            Objects.requireNonNull(this.solrAsTypeMappingRegistry);
            EnumSet<AsBoostOperator> supportedBoostOperators = (EnumSet<AsBoostOperator>)indexedPropertyType.getSupportedQueryOperators().stream().map(this.solrAsTypeMappingRegistry::toAsBoostOperator).filter(Objects::nonNull)
                            .collect(Collectors.toCollection(() -> EnumSet.noneOf(AsBoostOperator.class)));
            target.setSupportedBoostOperators(supportedBoostOperators);
        }
        else
        {
            target.setSupportedBoostOperators(Collections.emptySet());
        }
        return target;
    }


    protected int compareIndexProperties(AsIndexPropertyData indexProperty1, AsIndexPropertyData indexProperty2)
    {
        return indexProperty1.getCode().compareTo(indexProperty2.getCode());
    }


    protected AsExpressionData createScoreSortExpression()
    {
        AsExpressionData target = new AsExpressionData();
        target.setExpression("score");
        target.setName("score");
        return target;
    }


    protected AsExpressionData convertExpression(SolrIndexedPropertyModel source)
    {
        AsExpressionData target = new AsExpressionData();
        target.setExpression(source.getName());
        target.setName(source.getDisplayName());
        return target;
    }


    protected int compareExpressions(AsExpressionData expression1, AsExpressionData expression2)
    {
        return expression1.getExpression().compareTo(expression2.getExpression());
    }


    public List<String> getAvailableQualifiers(String indexType, String indexProperty) throws AsException
    {
        throw new UnsupportedOperationException();
    }


    public AsSearchResultData search(AsSearchProfileContext context, AsSearchQueryData searchQuery) throws AsException
    {
        if(context.getLanguage() == null)
        {
            return performSearch(context, searchQuery);
        }
        Locale localeForLanguage = this.commonI18NService.getLocaleForLanguage(context.getLanguage());
        return executeInLocalViewWithLocale(localeForLanguage, () -> {
            try
            {
                return performSearch(context, searchQuery);
            }
            catch(AsException a)
            {
                throw new AsRuntimeException(a);
            }
        });
    }


    protected AsSearchResultData executeInLocalViewWithLocale(Locale localeForLanguage, Supplier<AsSearchResultData> action) throws AsException
    {
        try
        {
            return (AsSearchResultData)this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, localeForLanguage, action));
        }
        catch(RuntimeException e)
        {
            if(e.getCause() instanceof AsException)
            {
                throw (AsException)e.getCause();
            }
            throw e;
        }
    }


    public AsSearchResultData performSearch(AsSearchProfileContext context, AsSearchQueryData searchQuery) throws AsException
    {
        try
        {
            SearchQuery query = convertSearchQuery(context, searchQuery);
            SearchResult result = this.facetSearchService.search(query);
            return convertSearchResult(result);
        }
        catch(FacetConfigServiceException | de.hybris.platform.solrfacetsearch.search.FacetSearchException e)
        {
            throw new AsException(e);
        }
    }


    protected IndexedType resolveIndexedType(FacetSearchConfig facetSearchConfig, String identifier) throws AsException
    {
        Optional<IndexedType> indexedTypeOptional = facetSearchConfig.getIndexConfig().getIndexedTypes().values().stream().filter(type -> type.getIdentifier().equals(identifier)).findFirst();
        if(!indexedTypeOptional.isPresent())
        {
            throw new AsException("Cannot find IndexedType");
        }
        return indexedTypeOptional.get();
    }


    protected CategoryModel resolveSelectedCategory(List<CategoryModel> categoryPath)
    {
        if(CollectionUtils.isEmpty(categoryPath))
        {
            return null;
        }
        return categoryPath.get(categoryPath.size() - 1);
    }


    protected SearchQuery convertSearchQuery(AsSearchProfileContext context, AsSearchQueryData searchQuery) throws FacetConfigServiceException, AsException
    {
        FacetSearchConfig facetSearchConfig = this.facetSearchConfigService.getConfiguration(context.getIndexConfiguration());
        IndexedType indexedType = resolveIndexedType(facetSearchConfig, context.getIndexType());
        SearchQuery query = this.facetSearchService.createFreeTextSearchQueryFromTemplate(facetSearchConfig, indexedType, "DEFAULT", searchQuery
                        .getQuery());
        query.setOffset(searchQuery.getActivePage());
        query.setPageSize(searchQuery.getPageSize());
        if(CollectionUtils.isNotEmpty(context.getCatalogVersions()))
        {
            query.setCatalogVersions(context.getCatalogVersions());
        }
        LanguageModel language = context.getLanguage();
        if(language != null)
        {
            query.setLanguage(language.getIsocode());
        }
        CurrencyModel currency = context.getCurrency();
        if(currency != null)
        {
            query.setCurrency(currency.getIsocode());
        }
        if(MapUtils.isNotEmpty(searchQuery.getFacetValues()))
        {
            for(Map.Entry<String, Set<String>> entry : (Iterable<Map.Entry<String, Set<String>>>)searchQuery.getFacetValues().entrySet())
            {
                String indexProperty = entry.getKey();
                Set<String> facetValues = entry.getValue();
                if(StringUtils.isNotBlank(indexProperty) && CollectionUtils.isNotEmpty(facetValues))
                {
                    query.addFacetValue(indexProperty, facetValues);
                }
            }
        }
        CategoryModel selectedCategory = resolveSelectedCategory(context.getCategoryPath());
        if(selectedCategory != null)
        {
            query.addFilterQuery("allCategories", new String[] {selectedCategory.getCode()});
        }
        query.setNamedSort(searchQuery.getSort());
        return query;
    }


    protected AsSearchResultData convertSearchResult(SearchResult result)
    {
        AsSearchResultData searchResult = new AsSearchResultData();
        searchResult.setActivePage(result.getOffset());
        searchResult.setPageCount((int)result.getNumberOfPages());
        searchResult.setPageSize(result.getPageSize());
        searchResult.setResultCount((int)result.getNumberOfResults());
        if(CollectionUtils.isNotEmpty(result.getGroupCommands()))
        {
            searchResult.setResults(convertAll(((SearchResultGroupCommand)result.getGroupCommands().get(0)).getGroups(), this::createDocumentData));
        }
        else
        {
            searchResult.setResults(convertAll(result.getDocuments(), this::createDocumentData));
        }
        searchResult.setFacets(convertAll(result.getFacets(), this::createFacetData));
        searchResult.setCurrentSort((result.getCurrentNamedSort() != null) ? createSortData(result.getCurrentNamedSort()) : null);
        searchResult.setAvailableSorts(convertAll(result.getAvailableNamedSorts(), this::createSortData));
        Map<String, Object> resultAttributes = result.getAttributes();
        AsSearchProfileContext searchProfileContext = (AsSearchProfileContext)resultAttributes.get("adaptiveSearchContext");
        AsSearchProfileResult searchProfileResult = (AsSearchProfileResult)resultAttributes.get("adaptiveSearchResult");
        searchResult.setSearchProfileResult(searchProfileResult);
        if(searchProfileContext != null)
        {
            searchResult.setCatalogVersions(searchProfileContext.getCatalogVersions());
            searchResult.setCategoryPath(searchProfileContext.getCategoryPath());
        }
        return searchResult;
    }


    protected AsDocumentData createDocumentData(SearchResultGroup group)
    {
        List<AsDocumentData> innerDocuments = convertAll(group.getDocuments(), this::createDocumentData);
        AsDocumentData firstInnerDocument = innerDocuments.get(0);
        AsDocumentData newDoc = new AsDocumentData();
        newDoc.setId(firstInnerDocument.getId());
        newDoc.setScore(firstInnerDocument.getScore());
        newDoc.setPk(firstInnerDocument.getPk());
        newDoc.setFields(firstInnerDocument.getFields());
        newDoc.setTags(firstInnerDocument.getTags());
        newDoc.setInnerDocuments(innerDocuments);
        return newDoc;
    }


    protected AsDocumentData createDocumentData(Document document)
    {
        AsDocumentData newDoc = new AsDocumentData();
        newDoc.setId(extractIdFromDocument(document));
        newDoc.setScore(extractScoreFromDocument(document));
        newDoc.setPk(extractPkFromDocument(document));
        newDoc.setFields(document.getFields());
        newDoc.setTags(document.getTags());
        newDoc.setInnerDocuments(Collections.emptyList());
        return newDoc;
    }


    protected AsFacetData createFacetData(Facet facet)
    {
        AsFacetData newFacet = new AsFacetData();
        newFacet.setIndexProperty(facet.getName());
        newFacet.setName(facet.getName());
        newFacet.setTopValues(convertAll(facet.getTopFacetValues(), this::createFacetValueData));
        newFacet.setValues(convertAll(facet.getFacetValues(), this::createFacetValueData));
        newFacet.setSelectedValues(convertAll(facet.getSelectedFacetValues(), this::createFacetValueData));
        newFacet.setAllValues(convertAll(facet.getAllFacetValues(), this::createFacetValueData));
        newFacet.setVisibility(AsFacetVisibility.SHOW);
        return newFacet;
    }


    protected AsFacetValueData createFacetValueData(FacetValue facetValue)
    {
        AsFacetValueData newFacetValue = new AsFacetValueData();
        newFacetValue.setValue(facetValue.getName());
        newFacetValue.setName(facetValue.getDisplayName());
        newFacetValue.setCount(facetValue.getCount());
        newFacetValue.setSelected(facetValue.isSelected());
        newFacetValue.setTags(facetValue.getTags());
        return newFacetValue;
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


    protected AsSortData createSortData(IndexedTypeSort sort)
    {
        AsSortData newSort = new AsSortData();
        newSort.setCode(sort.getCode());
        newSort.setName(sort.getName());
        newSort.setApplyPromotedItems(sort.isApplyPromotedItems());
        newSort.setHighlightPromotedItems(sort.isHighlightPromotedItems());
        return newSort;
    }


    protected String extractIdFromDocument(Document document)
    {
        Object id = document.getFields().get("id");
        if(id instanceof String)
        {
            return (String)id;
        }
        return null;
    }


    protected Float extractScoreFromDocument(Document document)
    {
        Object score = document.getFields().get("score");
        if(score instanceof Float)
        {
            return (Float)score;
        }
        if(score instanceof Double)
        {
            return Float.valueOf(((Double)score).floatValue());
        }
        return null;
    }


    protected PK extractPkFromDocument(Document document)
    {
        Object pk = document.getFields().get("pk");
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


    public SolrQueryContextFactory getSolrQueryContextFactory()
    {
        return this.solrQueryContextFactory;
    }


    @Required
    public void setSolrQueryContextFactory(SolrQueryContextFactory solrQueryContextFactory)
    {
        this.solrQueryContextFactory = solrQueryContextFactory;
    }


    public SolrFacetSearchConfigDao getSolrFacetSearchConfigDao()
    {
        return this.solrFacetSearchConfigDao;
    }


    @Required
    public void setSolrFacetSearchConfigDao(SolrFacetSearchConfigDao solrFacetSearchConfigDao)
    {
        this.solrFacetSearchConfigDao = solrFacetSearchConfigDao;
    }


    public SolrIndexedTypeDao getSolrIndexedTypeDao()
    {
        return this.solrIndexedTypeDao;
    }


    @Required
    public void setSolrIndexedTypeDao(SolrIndexedTypeDao solrIndexedTypeDao)
    {
        this.solrIndexedTypeDao = solrIndexedTypeDao;
    }


    public SolrIndexedPropertyDao getSolrIndexedPropertyDao()
    {
        return this.solrIndexedPropertyDao;
    }


    @Required
    public void setSolrIndexedPropertyDao(SolrIndexedPropertyDao solrIndexedPropertyDao)
    {
        this.solrIndexedPropertyDao = solrIndexedPropertyDao;
    }


    public SolrIndexedPropertyTypeRegistry getSolrIndexedPropertyTypeRegistry()
    {
        return this.solrIndexedPropertyTypeRegistry;
    }


    @Required
    public void setSolrIndexedPropertyTypeRegistry(SolrIndexedPropertyTypeRegistry solrIndexedPropertyTypeRegistry)
    {
        this.solrIndexedPropertyTypeRegistry = solrIndexedPropertyTypeRegistry;
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


    public FacetSearchService getFacetSearchService()
    {
        return this.facetSearchService;
    }


    @Required
    public void setFacetSearchService(FacetSearchService facetSearchService)
    {
        this.facetSearchService = facetSearchService;
    }


    public FacetSearchConfigService getFacetSearchConfigService()
    {
        return this.facetSearchConfigService;
    }


    @Required
    public void setFacetSearchConfigService(FacetSearchConfigService facetSearchConfigService)
    {
        this.facetSearchConfigService = facetSearchConfigService;
    }


    public SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
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


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }
}
