package com.hybris.backoffice.solrsearch.daos;

import com.hybris.backoffice.solrsearch.model.BackofficeIndexedTypeToSolrFacetSearchConfigModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.Collection;
import java.util.List;

@Deprecated(since = "2105", forRemoval = true)
public interface SolrFacetSearchConfigDAO
{
    Collection<BackofficeIndexedTypeToSolrFacetSearchConfigModel> findSearchConfigurationsForTypes(List<ComposedTypeModel> paramList);


    Collection<BackofficeIndexedTypeToSolrFacetSearchConfigModel> findSearchConfigurationsForName(String paramString);


    Collection<BackofficeIndexedTypeToSolrFacetSearchConfigModel> findAllSearchConfigs();
}
