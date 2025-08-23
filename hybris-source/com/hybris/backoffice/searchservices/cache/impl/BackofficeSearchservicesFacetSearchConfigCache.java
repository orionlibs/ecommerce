package com.hybris.backoffice.searchservices.cache.impl;

import com.hybris.backoffice.search.cache.impl.AbstractBackofficeFacetSearchConfigCache;
import com.hybris.backoffice.searchservices.model.BackofficeIndexedTypeToSearchservicesIndexConfigModel;
import de.hybris.platform.core.PK;
import java.util.Objects;
import java.util.concurrent.locks.Lock;

public class BackofficeSearchservicesFacetSearchConfigCache extends AbstractBackofficeFacetSearchConfigCache<BackofficeIndexedTypeToSearchservicesIndexConfigModel>
{
    public void putSearchConfigForTypeCode(String typeCode, Object facetSearchConfig)
    {
        Lock writeLock = this.cacheLock.writeLock();
        writeLock.lock();
        try
        {
            if(Objects.nonNull(facetSearchConfig) && facetSearchConfig instanceof BackofficeIndexedTypeToSearchservicesIndexConfigModel)
            {
                PK facetSearchConfigPK = ((BackofficeIndexedTypeToSearchservicesIndexConfigModel)facetSearchConfig).getPk();
                this.cache.put(typeCode, facetSearchConfigPK);
            }
        }
        finally
        {
            writeLock.unlock();
        }
    }
}
