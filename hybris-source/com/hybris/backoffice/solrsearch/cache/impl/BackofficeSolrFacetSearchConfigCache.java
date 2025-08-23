package com.hybris.backoffice.solrsearch.cache.impl;

import com.hybris.backoffice.search.cache.impl.AbstractBackofficeFacetSearchConfigCache;
import com.hybris.backoffice.solrsearch.model.BackofficeIndexedTypeToSolrFacetSearchConfigModel;
import de.hybris.platform.core.PK;
import java.util.Objects;
import java.util.concurrent.locks.Lock;

public class BackofficeSolrFacetSearchConfigCache extends AbstractBackofficeFacetSearchConfigCache<BackofficeIndexedTypeToSolrFacetSearchConfigModel>
{
    public void putSearchConfigForTypeCode(String typeCode, Object facetSearchConfig)
    {
        Lock writeLock = this.cacheLock.writeLock();
        writeLock.lock();
        try
        {
            if(Objects.nonNull(facetSearchConfig) && facetSearchConfig instanceof BackofficeIndexedTypeToSolrFacetSearchConfigModel)
            {
                PK facetSearchConfigPk = ((BackofficeIndexedTypeToSolrFacetSearchConfigModel)facetSearchConfig).getPk();
                this.cache.put(typeCode, facetSearchConfigPk);
            }
        }
        finally
        {
            writeLock.unlock();
        }
    }
}
