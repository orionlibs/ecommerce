package com.hybris.backoffice.solrsearch.services.impl;

import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import com.hybris.backoffice.solrsearch.converters.SearchConditionDataConverter;
import com.hybris.backoffice.solrsearch.converters.impl.DefaultSearchQueryConditionsConverter;
import com.hybris.backoffice.solrsearch.dataaccess.BackofficeSearchQuery;
import com.hybris.backoffice.solrsearch.dataaccess.SearchConditionData;
import com.hybris.backoffice.solrsearch.dataaccess.SolrSearchCondition;
import com.hybris.backoffice.solrsearch.decorators.SearchConditionDecorator;
import com.hybris.backoffice.solrsearch.services.BackofficeFacetSearchService;
import com.hybris.backoffice.solrsearch.utils.BackofficeSolrUtil;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.FacetField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.DefaultFacetSearchService;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.OrderComparator;

public class DefaultBackofficeFacetSearchService extends DefaultFacetSearchService implements BackofficeFacetSearchService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBackofficeFacetSearchService.class);
    private SearchConditionDataConverter searchConditionDataConverter;
    private DefaultSearchQueryConditionsConverter searchQueryConditionsConverter;
    private BackofficeFacetSearchConfigService facetSearchConfigService;
    private List<SearchConditionDecorator> conditionsDecorators;
    private TypeService typeService;
    private CatalogVersionService catalogVersionService;
    private CatalogTypeService catalogTypeService;
    private UserService userService;
    private Map<String, String> indexedTypeToCatalogVersionPropertyMapping;


    public BackofficeSearchQuery createSearchQuery(FacetSearchConfig facetSearchConfig, IndexedType indexedType)
    {
        return new BackofficeSearchQuery(facetSearchConfig, indexedType);
    }


    public BackofficeSearchQuery createBackofficeSolrSearchQuery(SearchQueryData queryData)
    {
        try
        {
            FacetSearchConfig facetSearchConfig = (FacetSearchConfig)this.facetSearchConfigService.getFacetSearchConfig(queryData.getSearchType());
            if(facetSearchConfig == null)
            {
                return null;
            }
            IndexedType indexedType = (IndexedType)this.facetSearchConfigService.getIndexedType(facetSearchConfig, queryData.getSearchType());
            if(indexedType == null)
            {
                return null;
            }
            BackofficeSearchQuery searchQuery = createSearchQuery(facetSearchConfig, indexedType);
            populateGroupCommandFields(facetSearchConfig, indexedType, (SearchQuery)searchQuery);
            populateFields(facetSearchConfig, indexedType, (SearchQuery)searchQuery);
            populateSortFields(facetSearchConfig, indexedType, (SearchQuery)searchQuery);
            populateFacetFields(facetSearchConfig, indexedType, (SearchQuery)searchQuery);
            populateSelectedFacets(queryData.getSelectedFacets(), (SearchQuery)searchQuery);
            if(StringUtils.isNotBlank(queryData.getSearchQueryText()))
            {
                populateFreeTextQuery(facetSearchConfig, indexedType, (SearchQuery)searchQuery, queryData.getSearchQueryText());
                searchQuery.setUserQuery(queryData.getSearchQueryText());
                searchQuery.setEnableSpellcheck(true);
            }
            searchQuery.setSearchConditionData(prepareSearchConditionData(queryData, indexedType));
            searchQuery.setDefaultOperator(BackofficeSolrUtil.convertToSolrOperator(queryData.getGlobalComparisonOperator()));
            searchQuery.setPageSize(queryData.getPageSize());
            return searchQuery;
        }
        catch(Exception e)
        {
            LOG.error("Cannot create solr search pageable", e);
            return null;
        }
    }


    @Deprecated(since = "2005", forRemoval = true)
    protected void populateSelectedFacets(Map<String, Set<String>> selectedFacets, SearchQuery searchQuery)
    {
        Set<String> availableFacets = (Set<String>)searchQuery.getFacets().stream().map(FacetField::getField).collect(Collectors.toSet());
        populateSelectedFacets(selectedFacets, availableFacets, searchQuery);
    }


    protected void populateSelectedFacets(Map<String, Set<String>> selectedFacets, Set<String> availableFacets, SearchQuery searchQuery)
    {
        if(MapUtils.isNotEmpty(selectedFacets))
        {
            selectedFacets.forEach((selectedFacetField, selectedFacetValues) -> {
                if(availableFacets.contains(selectedFacetField))
                {
                    searchQuery.addFacetValue(selectedFacetField, selectedFacetValues);
                }
            });
        }
    }


    protected SearchConditionData prepareSearchConditionData(SearchQueryData queryData, IndexedType indexedType)
    {
        SearchQuery.Operator globalOperator = BackofficeSolrUtil.convertToSolrOperator(queryData.getGlobalComparisonOperator());
        List<SolrSearchCondition> conditions = this.searchQueryConditionsConverter.convert(queryData.getConditions(), globalOperator, indexedType);
        SearchConditionData searchConditionData = this.searchConditionDataConverter.convertConditions(conditions, globalOperator);
        Objects.requireNonNull(searchConditionData);
        prepareTypeCondition(indexedType, queryData).ifPresent(searchConditionData::addFilterQueryCondition);
        if(isCatalogVersionAware(indexedType))
        {
            UserModel currentUser = getUserService().getCurrentUser();
            if(!getUserService().isAdmin(currentUser))
            {
                Collection<CatalogVersionModel> readableCVs = getCatalogVersionService().getAllReadableCatalogVersions((PrincipalModel)currentUser);
                if(CollectionUtils.isNotEmpty(readableCVs))
                {
                    prepareCatalogVersionCondition(indexedType, searchConditionData, queryData, readableCVs);
                }
            }
        }
        if(CollectionUtils.isNotEmpty(getConditionsDecorators()))
        {
            getConditionsDecorators().forEach(decorator -> decorator.decorate(searchConditionData, queryData, indexedType));
        }
        return searchConditionData;
    }


    protected boolean isCatalogVersionAware(IndexedType indexedType)
    {
        ComposedTypeModel type = indexedType.getComposedType();
        if(getCatalogTypeService().isCatalogVersionAwareType(type))
        {
            String catalogVersionProperty = resolveCatalogVersionProperty(indexedType);
            boolean hasCatalogVersion = indexedType.getIndexedProperties().entrySet().stream().anyMatch(entry -> catalogVersionProperty.equals(entry.getKey()));
            if(!hasCatalogVersion)
            {
                LOG.debug("Catalog Version data not found for type: {}", indexedType.getCode());
            }
            return hasCatalogVersion;
        }
        return false;
    }


    private String resolveCatalogVersionProperty(IndexedType indexedType)
    {
        if(getIndexedTypeToCatalogVersionPropertyMapping().containsKey(indexedType.getIdentifier()))
        {
            return (String)StringUtils.defaultIfBlank(getIndexedTypeToCatalogVersionPropertyMapping().get(indexedType.getIdentifier()), "catalogVersionPk");
        }
        return "catalogVersionPk";
    }


    protected Optional<SolrSearchCondition> prepareTypeCondition(IndexedType indexedType, SearchQueryData queryData)
    {
        if(!indexedType.getIndexedProperties().containsKey("itemtype"))
        {
            LOG.warn("No '{}' field found for '{}' indexed type. Too many results may be returned.", "itemtype", indexedType
                            .getIdentifier());
            return Optional.empty();
        }
        return Optional.of(prepareTypeCondition(queryData));
    }


    @Deprecated(since = "1808", forRemoval = true)
    protected SolrSearchCondition prepareTypeCondition(SearchQueryData queryData)
    {
        ComposedTypeModel composedTypeForCode = this.typeService.getComposedTypeForCode(queryData.getSearchType());
        SolrSearchCondition typeCondition = new SolrSearchCondition("itemtype", null, SearchQuery.Operator.OR);
        typeCondition.addConditionValue(queryData.getSearchType(), ValueComparisonOperator.EQUALS);
        if(queryData.isIncludeSubtypes())
        {
            composedTypeForCode.getAllSubTypes()
                            .forEach(ct -> typeCondition.addConditionValue(ct.getCode(), ValueComparisonOperator.EQUALS));
        }
        return typeCondition;
    }


    protected void prepareCatalogVersionCondition(IndexedType indexedType, SearchConditionData searchConditionData, SearchQueryData queryData, Collection<CatalogVersionModel> readableCatalogVersions)
    {
        String catalogVersionProperty = resolveCatalogVersionProperty(indexedType);
        Set<PK> cvPKs = (Set<PK>)readableCatalogVersions.stream().map(AbstractItemModel::getPk).collect(Collectors.toSet());
        SolrSearchCondition cvCondition = new SolrSearchCondition(catalogVersionProperty, null, SearchQuery.Operator.OR);
        if(cvPKs.isEmpty())
        {
            cvCondition.addConditionValue(Integer.valueOf(-1), ValueComparisonOperator.EQUALS);
        }
        else if(CollectionUtils.isNotEmpty(cvPKs))
        {
            cvPKs.forEach(pk -> cvCondition.addConditionValue(pk.getLong(), ValueComparisonOperator.EQUALS));
        }
        searchConditionData.addFilterQueryCondition(cvCondition);
    }


    @Required
    public void setSearchQueryConditionsConverter(DefaultSearchQueryConditionsConverter searchQueryConditionsConverter)
    {
        this.searchQueryConditionsConverter = searchQueryConditionsConverter;
    }


    @Required
    public void setFacetSearchConfigService(BackofficeFacetSearchConfigService facetSearchConfigService)
    {
        this.facetSearchConfigService = facetSearchConfigService;
    }


    @Required
    public void setSearchConditionDataConverter(SearchConditionDataConverter searchConditionDataConverter)
    {
        this.searchConditionDataConverter = searchConditionDataConverter;
    }


    public List<SearchConditionDecorator> getConditionsDecorators()
    {
        return this.conditionsDecorators;
    }


    public void setConditionsDecorators(List<SearchConditionDecorator> conditionsDecorators)
    {
        conditionsDecorators.sort((Comparator<? super SearchConditionDecorator>)OrderComparator.INSTANCE);
        this.conditionsDecorators = conditionsDecorators;
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


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public CatalogTypeService getCatalogTypeService()
    {
        return this.catalogTypeService;
    }


    @Required
    public void setCatalogTypeService(CatalogTypeService catalogTypeService)
    {
        this.catalogTypeService = catalogTypeService;
    }


    public Map<String, String> getIndexedTypeToCatalogVersionPropertyMapping()
    {
        return this.indexedTypeToCatalogVersionPropertyMapping;
    }


    @Required
    public void setIndexedTypeToCatalogVersionPropertyMapping(Map<String, String> indexedTypeToCatalogVersionPropertyMapping)
    {
        this.indexedTypeToCatalogVersionPropertyMapping = indexedTypeToCatalogVersionPropertyMapping;
    }
}
