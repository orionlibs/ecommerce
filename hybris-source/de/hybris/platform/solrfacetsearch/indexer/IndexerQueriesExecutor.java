package de.hybris.platform.solrfacetsearch.indexer;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IndexerQueriesExecutor
{
    List<PK> getPks(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, String paramString, Map<String, Object> paramMap) throws IndexerException;


    List<ItemModel> getItems(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, Collection<PK> paramCollection) throws IndexerException;
}
