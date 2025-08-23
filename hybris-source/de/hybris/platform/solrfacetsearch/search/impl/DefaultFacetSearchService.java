package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SearchConfig;
import de.hybris.platform.solrfacetsearch.config.SearchQueryProperty;
import de.hybris.platform.solrfacetsearch.config.SearchQuerySort;
import de.hybris.platform.solrfacetsearch.config.SearchQueryTemplate;
import de.hybris.platform.solrfacetsearch.provider.IndexedTypeFieldsValuesProvider;
import de.hybris.platform.solrfacetsearch.search.FacetField;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.FacetSearchStrategy;
import de.hybris.platform.solrfacetsearch.search.FacetSearchStrategyFactory;
import de.hybris.platform.solrfacetsearch.search.OrderField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Required;

public class DefaultFacetSearchService implements FacetSearchService, BeanFactoryAware
{
    public static final String DEFAULT_QUERY_TEMPLATE_NAME = "DEFAULT";
    private FacetSearchStrategyFactory facetSearchStrategyFactory;
    private BeanFactory beanFactory;


    public FacetSearchStrategyFactory getFacetSearchStrategyFactory()
    {
        return this.facetSearchStrategyFactory;
    }


    @Required
    public void setFacetSearchStrategyFactory(FacetSearchStrategyFactory facetSearchStrategyFactory)
    {
        this.facetSearchStrategyFactory = facetSearchStrategyFactory;
    }


    public BeanFactory getBeanFactory()
    {
        return this.beanFactory;
    }


    public void setBeanFactory(BeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }


    public SearchResult search(SearchQuery query) throws FacetSearchException
    {
        return search(query, Collections.emptyMap());
    }


    public SearchResult search(SearchQuery query, Map<String, String> searchHints) throws FacetSearchException
    {
        FacetSearchConfig facetSearchConfig = query.getFacetSearchConfig();
        IndexedType indexedType = query.getIndexedType();
        FacetSearchStrategy facetSearchStrategy = getFacetSearchStrategy(facetSearchConfig, indexedType);
        return facetSearchStrategy.search(query, searchHints);
    }


    protected FacetSearchStrategy getFacetSearchStrategy(FacetSearchConfig facetSearchConfig, IndexedType indexedType)
    {
        return this.facetSearchStrategyFactory.createStrategy(facetSearchConfig, indexedType);
    }


    public SearchQuery createSearchQuery(FacetSearchConfig facetSearchConfig, IndexedType indexedType)
    {
        return new SearchQuery(facetSearchConfig, indexedType);
    }


    public SearchQuery createPopulatedSearchQuery(FacetSearchConfig facetSearchConfig, IndexedType indexedType)
    {
        return createFreeTextSearchQuery(facetSearchConfig, indexedType, null);
    }


    public SearchQuery createFreeTextSearchQuery(FacetSearchConfig facetSearchConfig, IndexedType indexedType, String userQuery)
    {
        SearchQuery searchQuery = createSearchQuery(facetSearchConfig, indexedType);
        populateGroupCommandFields(facetSearchConfig, indexedType, searchQuery);
        populateFacetFields(facetSearchConfig, indexedType, searchQuery);
        populateFields(facetSearchConfig, indexedType, searchQuery);
        populateHighlightingFields(facetSearchConfig, indexedType, searchQuery);
        populateSortFields(facetSearchConfig, indexedType, searchQuery);
        if(StringUtils.isNotBlank(userQuery))
        {
            populateFreeTextQuery(facetSearchConfig, indexedType, searchQuery, userQuery);
        }
        return searchQuery;
    }


    protected void populateGroupCommandFields(FacetSearchConfig facetSearchConfig, IndexedType indexedType, SearchQuery searchQuery)
    {
        if(indexedType.isGroup())
        {
            searchQuery.addGroupCommand(indexedType.getGroupFieldName(), Integer.valueOf(indexedType.getGroupLimit()));
            searchQuery.setGroupFacets(indexedType.isGroupFacets());
        }
    }


    protected void populateFacetFields(FacetSearchConfig facetSearchConfig, IndexedType indexedType, SearchQuery searchQuery)
    {
        if(indexedType.getIndexedProperties() != null)
        {
            for(IndexedProperty indexedProperty : indexedType.getIndexedProperties().values())
            {
                if(indexedProperty.isFacet())
                {
                    FacetField facetField = new FacetField(indexedProperty.getName(), indexedProperty.getFacetType());
                    facetField.setPriority(Integer.valueOf(indexedProperty.getPriority()));
                    facetField.setDisplayNameProvider(indexedProperty.getFacetDisplayNameProvider());
                    facetField.setSortProvider(indexedProperty.getFacetSortProvider());
                    facetField.setTopValuesProvider(indexedProperty.getTopValuesProvider());
                    searchQuery.addFacet(facetField);
                }
            }
        }
        IndexedTypeFieldsValuesProvider provider = getFieldsValuesProvider(indexedType);
        if(provider != null)
        {
            for(String facet : provider.getFacets())
            {
                searchQuery.addFacet(facet);
            }
        }
    }


    protected void populateFields(FacetSearchConfig facetSearchConfig, IndexedType indexedType, SearchQuery searchQuery)
    {
        SearchConfig searchConfig = facetSearchConfig.getSearchConfig();
        if(searchConfig != null && searchConfig.isRestrictFieldsInResponse())
        {
            if(MapUtils.isNotEmpty(indexedType.getIndexedProperties()))
            {
                indexedType.getIndexedProperties().values().stream().filter(indexedProperty -> indexedProperty.isIncludeInResponse())
                                .forEach(fieldIncluded -> searchQuery.addField(fieldIncluded.getName()));
            }
            IndexedTypeFieldsValuesProvider provider = getFieldsValuesProvider(indexedType);
            if(provider != null)
            {
                Objects.requireNonNull(searchQuery);
                provider.getFieldNamesMapping().keySet().forEach(searchQuery::addField);
            }
        }
    }


    protected void populateHighlightingFields(FacetSearchConfig facetSearchConfig, IndexedType indexedType, SearchQuery searchQuery)
    {
        SearchConfig searchConfig = facetSearchConfig.getSearchConfig();
        if(searchConfig != null && searchConfig.isEnableHighlighting() && MapUtils.isNotEmpty(indexedType.getIndexedProperties()))
        {
            indexedType.getIndexedProperties().values().stream().filter(indexedProperty -> indexedProperty.isHighlight())
                            .forEach(highlightingField -> searchQuery.addHighlightingField(highlightingField.getName()));
        }
    }


    protected void populateSortFields(FacetSearchConfig facetSearchConfig, IndexedType indexedType, SearchQuery searchQuery)
    {
        SearchConfig searchConfig = facetSearchConfig.getSearchConfig();
        if(searchConfig != null && searchConfig.getDefaultSortOrder() != null)
        {
            for(String order : searchConfig.getDefaultSortOrder())
            {
                if("score".equals(order))
                {
                    searchQuery.addSort(order, OrderField.SortOrder.DESCENDING);
                    continue;
                }
                searchQuery.addSort(order, OrderField.SortOrder.ASCENDING);
            }
        }
    }


    protected void populateFreeTextQuery(FacetSearchConfig facetSearchConfig, IndexedType indexedType, SearchQuery searchQuery, String userQuery)
    {
        searchQuery.setFreeTextQueryBuilder(indexedType.getFtsQueryBuilder());
        if(MapUtils.isNotEmpty(indexedType.getFtsQueryBuilderParameters()))
        {
            searchQuery.getFreeTextQueryBuilderParameters().putAll(indexedType.getFtsQueryBuilderParameters());
        }
        searchQuery.setUserQuery(userQuery);
        for(IndexedProperty indexedProperty : indexedType.getIndexedProperties().values())
        {
            if(indexedProperty.isFtsQuery())
            {
                searchQuery.addFreeTextQuery(indexedProperty.getName(), indexedProperty.getFtsQueryMinTermLength(), indexedProperty
                                .getFtsQueryBoost());
            }
            if(indexedProperty.isFtsFuzzyQuery())
            {
                searchQuery.addFreeTextFuzzyQuery(indexedProperty.getName(), indexedProperty.getFtsFuzzyQueryMinTermLength(), indexedProperty
                                .getFtsFuzzyQueryFuzziness(), indexedProperty.getFtsFuzzyQueryBoost());
            }
            if(indexedProperty.isFtsWildcardQuery())
            {
                searchQuery.addFreeTextWildcardQuery(indexedProperty.getName(), indexedProperty.getFtsWildcardQueryMinTermLength(), indexedProperty
                                .getFtsWildcardQueryType(), indexedProperty.getFtsWildcardQueryBoost());
            }
            if(indexedProperty.isFtsPhraseQuery())
            {
                searchQuery.addFreeTextPhraseQuery(indexedProperty.getName(), indexedProperty.getFtsPhraseQuerySlop(), indexedProperty
                                .getFtsPhraseQueryBoost());
            }
        }
    }


    public SearchQuery createSearchQueryFromTemplate(FacetSearchConfig facetSearchConfig, IndexedType indexedType, String queryTemplateName)
    {
        return createFreeTextSearchQueryFromTemplate(facetSearchConfig, indexedType, queryTemplateName, null);
    }


    public SearchQuery createFreeTextSearchQueryFromTemplate(FacetSearchConfig facetSearchConfig, IndexedType indexedType, String queryTemplateName, String userQuery)
    {
        ServicesUtil.validateParameterNotNull(facetSearchConfig, "FacetSearchConfig cannot be null");
        ServicesUtil.validateParameterNotNull(indexedType, "IndexedType cannot be null");
        ServicesUtil.validateParameterNotNull(queryTemplateName, "QueryTemplateName cannot be null");
        SearchQueryTemplate queryTemplate = findQueryTemplateForName(indexedType, queryTemplateName);
        if(queryTemplate != null)
        {
            SearchQuery searchQuery = createSearchQuery(facetSearchConfig, indexedType);
            populateGroupCommandFields(facetSearchConfig, indexedType, queryTemplate, searchQuery);
            populateFacetFields(facetSearchConfig, indexedType, queryTemplate, searchQuery);
            populateFields(facetSearchConfig, indexedType, queryTemplate, searchQuery);
            populateHighlightingFields(facetSearchConfig, indexedType, queryTemplate, searchQuery);
            populateSortFields(facetSearchConfig, indexedType, queryTemplate, searchQuery);
            if(StringUtils.isNotBlank(userQuery))
            {
                populateFreeTextQuery(facetSearchConfig, indexedType, queryTemplate, searchQuery, userQuery);
            }
            return searchQuery;
        }
        return createFreeTextSearchQuery(facetSearchConfig, indexedType, userQuery);
    }


    protected SearchQueryTemplate findQueryTemplateForName(IndexedType indexedType, String queryTemplateName)
    {
        Map<String, SearchQueryTemplate> searchQueryTemplates = indexedType.getSearchQueryTemplates();
        if(searchQueryTemplates == null)
        {
            return null;
        }
        SearchQueryTemplate queryTemplate = searchQueryTemplates.get(queryTemplateName);
        if(queryTemplate != null)
        {
            return queryTemplate;
        }
        return searchQueryTemplates.get("DEFAULT");
    }


    protected void populateGroupCommandFields(FacetSearchConfig facetSearchConfig, IndexedType indexedType, SearchQueryTemplate searchQueryTemplate, SearchQuery searchQuery)
    {
        if(searchQueryTemplate.isGroup() && searchQueryTemplate.getGroupProperty() != null)
        {
            searchQuery.addGroupCommand(searchQueryTemplate.getGroupProperty().getName(), searchQueryTemplate.getGroupLimit());
            searchQuery.setGroupFacets(searchQueryTemplate.isGroupFacets());
        }
    }


    protected void populateFacetFields(FacetSearchConfig facetSearchConfig, IndexedType indexedType, SearchQueryTemplate searchQueryTemplate, SearchQuery searchQuery)
    {
        if(!searchQueryTemplate.isShowFacets())
        {
            return;
        }
        if(searchQueryTemplate.getSearchQueryProperties() != null)
        {
            searchQueryTemplate.getSearchQueryProperties().values().stream().filter(SearchQueryProperty::isFacet)
                            .forEach(facetProperty -> {
                                FacetField facetField = new FacetField(facetProperty.getIndexedProperty(), facetProperty.getFacetType());
                                facetField.setPriority(facetProperty.getPriority());
                                facetField.setDisplayNameProvider(facetProperty.getFacetDisplayNameProvider());
                                facetField.setSortProvider(facetProperty.getFacetSortProvider());
                                facetField.setTopValuesProvider(facetProperty.getFacetTopValuesProvider());
                                searchQuery.addFacet(facetField);
                            });
        }
        IndexedTypeFieldsValuesProvider provider = getFieldsValuesProvider(indexedType);
        if(provider != null)
        {
            Objects.requireNonNull(searchQuery);
            provider.getFacets().forEach(searchQuery::addFacet);
        }
    }


    protected void populateFields(FacetSearchConfig facetSearchConfig, IndexedType indexedType, SearchQueryTemplate searchQueryTemplate, SearchQuery searchQuery)
    {
        if(searchQueryTemplate.isRestrictFieldsInResponse())
        {
            if(MapUtils.isNotEmpty(searchQueryTemplate.getSearchQueryProperties()))
            {
                searchQueryTemplate.getSearchQueryProperties().values().stream()
                                .filter(searchQueryProperty -> searchQueryProperty.isIncludeInResponse())
                                .forEach(fieldIncluded -> searchQuery.addField(fieldIncluded.getIndexedProperty()));
            }
            IndexedTypeFieldsValuesProvider provider = getFieldsValuesProvider(indexedType);
            if(provider != null)
            {
                Objects.requireNonNull(searchQuery);
                provider.getFieldNamesMapping().keySet().forEach(searchQuery::addField);
            }
        }
    }


    protected void populateHighlightingFields(FacetSearchConfig facetSearchConfig, IndexedType indexedType, SearchQueryTemplate searchQueryTemplate, SearchQuery searchQuery)
    {
        if(searchQueryTemplate.isEnableHighlighting() && MapUtils.isNotEmpty(searchQueryTemplate.getSearchQueryProperties()))
        {
            searchQueryTemplate.getSearchQueryProperties().values().stream()
                            .filter(searchQueryProperty -> searchQueryProperty.isHighlight())
                            .forEach(highlightingField -> searchQuery.addHighlightingField(highlightingField.getIndexedProperty()));
        }
    }


    protected void populateSortFields(FacetSearchConfig facetSearchConfig, IndexedType indexedType, SearchQueryTemplate searchQueryTemplate, SearchQuery searchQuery)
    {
        if(CollectionUtils.isEmpty(searchQueryTemplate.getSearchQuerySorts()))
        {
            return;
        }
        for(SearchQuerySort sort : searchQueryTemplate.getSearchQuerySorts())
        {
            searchQuery.addSort(sort.getField(), sort.isAscending() ? OrderField.SortOrder.ASCENDING : OrderField.SortOrder.DESCENDING);
        }
    }


    protected void populateFreeTextQuery(FacetSearchConfig facetSearchConfig, IndexedType indexedType, SearchQueryTemplate searchQueryTemplate, SearchQuery searchQuery, String userQuery)
    {
        searchQuery.setFreeTextQueryBuilder(searchQueryTemplate.getFtsQueryBuilder());
        if(MapUtils.isNotEmpty(searchQueryTemplate.getFtsQueryBuilderParameters()))
        {
            searchQuery.getFreeTextQueryBuilderParameters().putAll(searchQueryTemplate.getFtsQueryBuilderParameters());
        }
        searchQuery.setUserQuery(userQuery);
        for(SearchQueryProperty searchQueryProperty : searchQueryTemplate.getSearchQueryProperties().values())
        {
            if(searchQueryProperty.isFtsQuery())
            {
                searchQuery.addFreeTextQuery(searchQueryProperty.getIndexedProperty(), searchQueryProperty.getFtsQueryMinTermLength(), searchQueryProperty
                                .getFtsQueryBoost());
            }
            if(searchQueryProperty.isFtsFuzzyQuery())
            {
                searchQuery.addFreeTextFuzzyQuery(searchQueryProperty.getIndexedProperty(), searchQueryProperty
                                .getFtsFuzzyQueryMinTermLength(), searchQueryProperty.getFtsFuzzyQueryFuzziness(), searchQueryProperty
                                .getFtsFuzzyQueryBoost());
            }
            if(searchQueryProperty.isFtsWildcardQuery())
            {
                searchQuery.addFreeTextWildcardQuery(searchQueryProperty.getIndexedProperty(), searchQueryProperty
                                .getFtsWildcardQueryMinTermLength(), searchQueryProperty.getFtsWildcardQueryType(), searchQueryProperty
                                .getFtsWildcardQueryBoost());
            }
            if(searchQueryProperty.isFtsPhraseQuery())
            {
                searchQuery.addFreeTextPhraseQuery(searchQueryProperty.getIndexedProperty(), searchQueryProperty
                                .getFtsPhraseQuerySlop(), searchQueryProperty.getFtsPhraseQueryBoost());
            }
        }
    }


    protected void populatePagination(FacetSearchConfig facetSearchConfig, IndexedType indexedType, SearchQueryTemplate searchQueryTemplate, SearchQuery searchQuery)
    {
        searchQuery.setPageSize(searchQueryTemplate.getPageSize().intValue());
    }


    protected IndexedTypeFieldsValuesProvider getFieldsValuesProvider(IndexedType indexedType)
    {
        if(!StringUtils.isEmpty(indexedType.getFieldsValuesProvider()))
        {
            Object fieldsValueProvider = this.beanFactory.getBean(indexedType.getFieldsValuesProvider());
            if(fieldsValueProvider instanceof IndexedTypeFieldsValuesProvider)
            {
                return (IndexedTypeFieldsValuesProvider)fieldsValueProvider;
            }
        }
        return null;
    }
}
