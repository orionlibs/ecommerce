package de.hybris.platform.adaptivesearchsolr.strategies.impl;

import de.hybris.platform.adaptivesearch.strategies.impl.AbstractAsCategoryPathResolver;
import de.hybris.platform.adaptivesearchsolr.strategies.SolrAsCategoryPathResolver;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.solrfacetsearch.search.FacetValueField;
import de.hybris.platform.solrfacetsearch.search.QueryField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSolrAsCategoryPathResolver extends AbstractAsCategoryPathResolver implements SolrAsCategoryPathResolver
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSolrAsCategoryPathResolver.class);


    public List<CategoryModel> resolveCategoryPath(SearchQuery searchQuery, List<CatalogVersionModel> catalogVersions)
    {
        Optional<List<CategoryModel>> currentCategoryPath = getAsCategoryService().getCurrentCategoryPath();
        if(currentCategoryPath.isPresent())
        {
            return currentCategoryPath.get();
        }
        if(CollectionUtils.isEmpty(catalogVersions))
        {
            return Collections.emptyList();
        }
        List<String> categoryCodes = resolveCategoryCodes(searchQuery);
        if(CollectionUtils.isEmpty(categoryCodes))
        {
            return Collections.emptyList();
        }
        List<CategoryModel> categoryPath = getAsCategoryService().buildCategoryPath(categoryCodes, catalogVersions, true);
        if(LOG.isDebugEnabled())
        {
            LOG.debug(categoryPath.stream().map(CategoryModel::getName).collect(Collectors.joining(",")));
        }
        return categoryPath;
    }


    protected List<String> resolveCategoryCodes(SearchQuery searchQuery)
    {
        List<String> categoryCodes = new ArrayList<>();
        resolveCategoryCodesFromFilterQueries(searchQuery, categoryCodes);
        resolveCategoryCodesFromFacetValues(searchQuery, categoryCodes);
        return categoryCodes;
    }


    protected void resolveCategoryCodesFromFilterQueries(SearchQuery searchQuery, List<String> categoryCodes)
    {
        List<QueryField> filterQueries = searchQuery.getFilterQueries();
        if(CollectionUtils.isEmpty(filterQueries))
        {
            return;
        }
        String filterIndexProperty = resolveFilterIndexProperty();
        for(QueryField queryField : searchQuery.getFilterQueries())
        {
            if(StringUtils.equals(queryField.getField(), filterIndexProperty) && CollectionUtils.isNotEmpty(queryField.getValues()))
            {
                addCategoryCodes(categoryCodes, queryField.getValues());
            }
        }
    }


    protected void resolveCategoryCodesFromFacetValues(SearchQuery searchQuery, List<String> categoryCodes)
    {
        List<FacetValueField> facetValues = searchQuery.getFacetValues();
        if(CollectionUtils.isEmpty(facetValues))
        {
            return;
        }
        String facetFilterIndexProperty = resolveFacetFilterIndexProperty();
        for(FacetValueField facetValueField : searchQuery.getFacetValues())
        {
            if(StringUtils.equals(facetValueField.getField(), facetFilterIndexProperty) &&
                            CollectionUtils.isNotEmpty(facetValueField.getValues()))
            {
                addCategoryCodes(categoryCodes, facetValueField.getValues());
            }
        }
    }
}
