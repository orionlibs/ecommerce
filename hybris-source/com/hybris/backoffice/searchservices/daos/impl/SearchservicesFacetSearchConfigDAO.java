package com.hybris.backoffice.searchservices.daos.impl;

import com.hybris.backoffice.search.daos.impl.AbstractFacetSearchConfigDAO;
import com.hybris.backoffice.searchservices.model.BackofficeIndexedTypeToSearchservicesIndexConfigModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.searchservices.jalo.SnIndexConfiguration;
import de.hybris.platform.searchservices.jalo.SnIndexType;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.List;

public class SearchservicesFacetSearchConfigDAO extends AbstractFacetSearchConfigDAO<BackofficeIndexedTypeToSearchservicesIndexConfigModel>
{
    private static final String FIND_SEARCH_CFG_FOR_TYPES_QUERY = String.format("select {b:%s} from {%s as b} where {b:%s} in (?%s)", new Object[] {"pk", "BackofficeIndexedTypeToSearchservicesIndexConfig", "indexedType", "indexedType"});
    private static final String FIND_SEARCH_CFG_FOR_NAME_QUERY = String.format("SELECT {bfc:%s} FROM {%s AS bfc}, {%s AS sic}, {%s AS sit} WHERE {bfc:%s} = {sic:%s} AND {bfc:%s} = {sit:%s} AND {sic:%s} = {sit:%s} AND { sit:%s } = ?%s", new Object[] {
                    "pk", "BackofficeIndexedTypeToSearchservicesIndexConfig", "snIndexConfiguration", "snIndexType", "snIndexConfiguration", SnIndexConfiguration.PK, "snIndexType", SnIndexType.PK, SnIndexConfiguration.PK, "indexConfiguration",
                    "id", "id"});
    private static final String FIND_ALL_SEARCH_CFG_QUERY = String.format("select {b:%s} from {%s as b}", new Object[] {"pk", "BackofficeIndexedTypeToSearchservicesIndexConfig"});


    protected FlexibleSearchQuery getQuery4FindSearchCfgForTypes(List<ComposedTypeModel> types)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_SEARCH_CFG_FOR_TYPES_QUERY);
        query.addQueryParameter("indexedType", types);
        return query;
    }


    protected FlexibleSearchQuery getQuery4FindSearchCfgForName(String facetSearchConfigName)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_SEARCH_CFG_FOR_NAME_QUERY);
        query.addQueryParameter("id", facetSearchConfigName);
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
            this.typeService.getComposedTypeForClass(BackofficeIndexedTypeToSearchservicesIndexConfigModel.class);
        }
        catch(UnknownIdentifierException exception)
        {
            return false;
        }
        return true;
    }
}
