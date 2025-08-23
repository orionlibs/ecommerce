package de.hybris.platform.solrfacetsearch.indexer.impl;

import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.indexer.SolrIndexedTypeCodeResolver;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;

public class DefaultSolrIndexedTypeCodeResolver implements SolrIndexedTypeCodeResolver
{
    public String resolveIndexedTypeCode(SolrIndexedTypeModel solrIndexedType)
    {
        ServicesUtil.validateParameterNotNull(solrIndexedType, "solrIndexedType must not be null");
        String uniqueCode = solrIndexedType.getType().getCode();
        if(solrIndexedType.getIndexName() != null)
        {
            uniqueCode = uniqueCode.concat("_").concat(solrIndexedType.getIndexName());
        }
        return uniqueCode;
    }
}
