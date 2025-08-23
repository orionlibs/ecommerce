package de.hybris.platform.solrfacetsearch.search.impl.populators;

import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.springframework.beans.factory.annotation.Required;

public class FacetSearchQueryCatalogVersionsFilterPopulator implements Populator<SearchQueryConverterData, SolrQuery>
{
    public static final String CATALOG_ID_FIELD = "catalogId";
    public static final String CATALOG_VERSION_FIELD = "catalogVersion";
    private CatalogTypeService catalogTypeService;


    public void populate(SearchQueryConverterData source, SolrQuery target)
    {
        SearchQuery searchQuery = source.getSearchQuery();
        IndexedType indexedType = searchQuery.getIndexedType();
        ComposedTypeModel composedType = indexedType.getComposedType();
        if(this.catalogTypeService.isCatalogVersionAwareType(composedType) &&
                        CollectionUtils.isNotEmpty(searchQuery.getCatalogVersions()))
        {
            StringBuilder rawQuery = new StringBuilder();
            boolean isFirst = true;
            for(CatalogVersionModel catalogVersion : searchQuery.getCatalogVersions())
            {
                CatalogModel catalog = catalogVersion.getCatalog();
                if(isFirst)
                {
                    isFirst = false;
                }
                else
                {
                    rawQuery.append(" OR ");
                }
                rawQuery.append('(');
                rawQuery.append("catalogId").append(":\"").append(ClientUtils.escapeQueryChars(catalog.getId())).append('"');
                rawQuery.append(" AND ");
                rawQuery.append("catalogVersion").append(":\"").append(ClientUtils.escapeQueryChars(catalogVersion.getVersion()))
                                .append('"');
                rawQuery.append(')');
            }
            target.addFilterQuery(new String[] {rawQuery.toString()});
        }
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
}
