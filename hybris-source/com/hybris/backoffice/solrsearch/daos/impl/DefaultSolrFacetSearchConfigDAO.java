package com.hybris.backoffice.solrsearch.daos.impl;

import com.hybris.backoffice.solrsearch.daos.SolrFacetSearchConfigDAO;
import com.hybris.backoffice.solrsearch.model.BackofficeIndexedTypeToSolrFacetSearchConfigModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrFacetSearchConfig;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2105", forRemoval = true)
public class DefaultSolrFacetSearchConfigDAO implements SolrFacetSearchConfigDAO
{
    protected static final String FIND_SEARCH_CFG_FOR_TYPES_QUERY = String.format("select {b:%s} from {%s as b} where {b:%s} in (?%s)", new Object[] {"pk", "BackofficeIndexedTypeToSolrFacetSearchConfig", "indexedType", "indexedType"});
    protected static final String FIND_SEARCH_CFG_FOR_NAME_QUERY = String.format("SELECT {bfc:%s} FROM {%s AS bfc LEFT JOIN %s AS fc ON {bfc:%s} = {fc:%s} } WHERE { fc:%s } = ?%s",
                    new Object[] {"pk", "BackofficeIndexedTypeToSolrFacetSearchConfig", "solrFacetSearchConfig", "solrFacetSearchConfig", SolrFacetSearchConfig.PK, "name", "name"});
    protected static final String FIND_ALL_SEARCH_CFG_QUERY = String.format("select {b:%s} from {%s as b}", new Object[] {"pk", "BackofficeIndexedTypeToSolrFacetSearchConfig"});
    private FlexibleSearchService flexibleSearchService;
    private TypeService typeService;


    public List<BackofficeIndexedTypeToSolrFacetSearchConfigModel> findSearchConfigurationsForTypes(List<ComposedTypeModel> types)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_SEARCH_CFG_FOR_TYPES_QUERY);
        query.addQueryParameter("indexedType", types);
        return this.flexibleSearchService.search(query).getResult();
    }


    public List<BackofficeIndexedTypeToSolrFacetSearchConfigModel> findSearchConfigurationsForName(String facetSearchConfigName)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_SEARCH_CFG_FOR_NAME_QUERY);
        query.addQueryParameter("name", facetSearchConfigName);
        return this.flexibleSearchService.search(query).getResult();
    }


    public List<BackofficeIndexedTypeToSolrFacetSearchConfigModel> findAllSearchConfigs()
    {
        if(isSolrFacetSearchConfigModelCreated())
        {
            FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_ALL_SEARCH_CFG_QUERY);
            return this.flexibleSearchService.search(query).getResult();
        }
        return Collections.emptyList();
    }


    private boolean isSolrFacetSearchConfigModelCreated()
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


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
