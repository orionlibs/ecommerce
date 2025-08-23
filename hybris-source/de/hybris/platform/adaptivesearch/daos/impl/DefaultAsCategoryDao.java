package de.hybris.platform.adaptivesearch.daos.impl;

import de.hybris.platform.adaptivesearch.daos.AsCategoryDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultAsCategoryDao extends AbstractAsGenericDao<CategoryModel> implements AsCategoryDao
{
    public DefaultAsCategoryDao()
    {
        super("Category");
    }


    public List<CategoryModel> findCategoriesByCatalogVersion(CatalogVersionModel catalogVersion)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("catalogVersion", catalogVersion);
        return find(parameters);
    }


    public List<List<PK>> findCategoryRelationsByCatalogVersion(CatalogVersionModel catalogVersion)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {sc.pk}, {tc.pk} ");
        query.append("FROM { ");
        query.append("CategoryCategoryRelation AS c2cr ");
        query.append("JOIN Category AS sc ON {sc:pk} = {c2cr:source}");
        query.append("JOIN Category AS tc ON {tc:pk} = {c2cr:target}");
        query.append("}");
        query.append("WHERE {sc.catalogVersion} = ?catalogVersion AND {tc.catalogVersion} = ?catalogVersion");
        Map<String, Object> parameters = new HashMap<>(2);
        parameters.put("catalogVersion", catalogVersion);
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString(), parameters);
        searchQuery.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {PK.class, PK.class}));
        SearchResult<List<PK>> searchResult = getFlexibleSearchService().search(searchQuery);
        return searchResult.getResult();
    }
}
