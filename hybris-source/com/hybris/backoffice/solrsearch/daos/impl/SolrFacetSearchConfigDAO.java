package com.hybris.backoffice.solrsearch.daos.impl;

import com.hybris.backoffice.search.daos.impl.AbstractFacetSearchConfigDAO;
import com.hybris.backoffice.solrsearch.model.BackofficeIndexedTypeToSolrFacetSearchConfigModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrFacetSearchConfig;
import java.util.List;

public class SolrFacetSearchConfigDAO extends AbstractFacetSearchConfigDAO<BackofficeIndexedTypeToSolrFacetSearchConfigModel>
{
    private static final String FIND_SEARCH_CFG_FOR_TYPES_QUERY = String.format("select {b:%s} from {%s as b} where {b:%s} in (?%s)", new Object[] {"pk", "BackofficeIndexedTypeToSolrFacetSearchConfig", "indexedType", "indexedType"});
    private static final String FIND_SEARCH_CFG_FOR_NAME_QUERY = String.format("SELECT {bfc:%s} FROM {%s AS bfc LEFT JOIN %s AS fc ON {bfc:%s} = {fc:%s} } WHERE { fc:%s } = ?%s",
                    new Object[] {"pk", "BackofficeIndexedTypeToSolrFacetSearchConfig", "solrFacetSearchConfig", "solrFacetSearchConfig", SolrFacetSearchConfig.PK, "name", "name"});
    private static final String FIND_ALL_SEARCH_CFG_QUERY = String.format("select {b:%s} from {%s as b}", new Object[] {"pk", "BackofficeIndexedTypeToSolrFacetSearchConfig"});


    protected FlexibleSearchQuery getQuery4FindSearchCfgForTypes(List types)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_SEARCH_CFG_FOR_TYPES_QUERY);
        query.addQueryParameter("indexedType", types);
        return query;
    }


    protected FlexibleSearchQuery getQuery4FindSearchCfgForName(String facetSearchConfigName)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_SEARCH_CFG_FOR_NAME_QUERY);
        query.addQueryParameter("name", facetSearchConfigName);
        return query;
    }


    protected FlexibleSearchQuery getQuery4FindAllSearchCfg()
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_ALL_SEARCH_CFG_QUERY);
        return query;
    }


    protected boolean isFacetSearchConfigModelCreated()
    {
        try
        {
            this.typeService.getComposedTypeForClass(BackofficeIndexedTypeToSolrFacetSearchConfigModel.class);
        }
        catch(UnknownIdentifierException exception)
        {
            return false;
        }
        return true;
    }
}
