package de.hybris.platform.adaptivesearch.searchservices.strategies.impl;

import de.hybris.platform.adaptivesearch.searchservices.strategies.SnAsCategoryPathResolver;
import de.hybris.platform.adaptivesearch.strategies.impl.AbstractAsCategoryPathResolver;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.searchservices.search.data.AbstractSnFacetFilter;
import de.hybris.platform.searchservices.search.data.SnBucketsFacetFilter;
import de.hybris.platform.searchservices.search.data.SnFilter;
import de.hybris.platform.searchservices.search.data.SnMatchTermQuery;
import de.hybris.platform.searchservices.search.data.SnMatchTermsQuery;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import de.hybris.platform.searchservices.search.service.SnSearchContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSnAsCategoryPathResolver extends AbstractAsCategoryPathResolver implements SnAsCategoryPathResolver
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSnAsCategoryPathResolver.class);


    public List<CategoryModel> resolveCategoryPath(SnSearchContext searchContext, List<CatalogVersionModel> catalogVersions)
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
        SnSearchQuery searchQuery = searchContext.getSearchRequest().getSearchQuery();
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


    protected List<String> resolveCategoryCodes(SnSearchQuery searchQuery)
    {
        List<String> categoryCodes = new ArrayList<>();
        resolveCategoryCodesFromFilters(searchQuery, categoryCodes);
        resolveCategoryCodesFromFacetFilters(searchQuery, categoryCodes);
        return categoryCodes;
    }


    protected void resolveCategoryCodesFromFilters(SnSearchQuery searchQuery, List<String> categoryCodes)
    {
        List<SnFilter> filters = searchQuery.getFilters();
        if(CollectionUtils.isEmpty(filters))
        {
            return;
        }
        String filterIndexProperty = resolveFilterIndexProperty();
        for(SnFilter filter : filters)
        {
            if(filter.getQuery() instanceof SnMatchTermQuery)
            {
                SnMatchTermQuery matchTermQuery = (SnMatchTermQuery)filter.getQuery();
                if(StringUtils.equals(matchTermQuery.getExpression(), filterIndexProperty) && matchTermQuery.getValue() != null)
                {
                    addCategoryCode(categoryCodes, String.valueOf(matchTermQuery.getValue()));
                }
                continue;
            }
            if(filter.getQuery() instanceof SnMatchTermsQuery)
            {
                SnMatchTermsQuery matchTermsQuery = (SnMatchTermsQuery)filter.getQuery();
                if(StringUtils.equals(matchTermsQuery.getExpression(), filterIndexProperty) &&
                                CollectionUtils.isNotEmpty(matchTermsQuery.getValues()))
                {
                    List<String> values = (List<String>)matchTermsQuery.getValues().stream().filter(Objects::nonNull).map(String::valueOf).collect(Collectors.toList());
                    addCategoryCodes(categoryCodes, values);
                }
            }
        }
    }


    protected void resolveCategoryCodesFromFacetFilters(SnSearchQuery searchQuery, List<String> categoryCodes)
    {
        List<AbstractSnFacetFilter> facetFilters = searchQuery.getFacetFilters();
        if(CollectionUtils.isEmpty(facetFilters))
        {
            return;
        }
        String facetFilterIndexProperty = resolveFacetFilterIndexProperty();
        for(AbstractSnFacetFilter facetFilter : facetFilters)
        {
            if(facetFilter instanceof SnBucketsFacetFilter)
            {
                SnBucketsFacetFilter bucketsFacetFilter = (SnBucketsFacetFilter)facetFilter;
                if(StringUtils.equals(bucketsFacetFilter.getFacetId(), facetFilterIndexProperty) &&
                                CollectionUtils.isNotEmpty(bucketsFacetFilter.getBucketIds()))
                {
                    addCategoryCodes(categoryCodes, bucketsFacetFilter.getBucketIds());
                }
            }
        }
    }
}
