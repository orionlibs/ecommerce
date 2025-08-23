package de.hybris.platform.jalo.flexiblesearch.internal;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import java.util.Set;

public class CacheableResultHolder
{
    final JDBCValueMappings.RowFetchResult rowFetchResult;
    final int prefetchSize;
    final Set<PK> prefetchLanguages;
    final ConversionMetaInformation conversionResult;


    CacheableResultHolder(JDBCValueMappings.RowFetchResult rowFetchResult, int prefetchSize, Set<PK> prefetchLanguages, ConversionMetaInformation conversionResult)
    {
        this.conversionResult = conversionResult;
        this.prefetchLanguages = prefetchLanguages;
        this.prefetchSize = prefetchSize;
        this.rowFetchResult = rowFetchResult;
    }


    public JDBCValueMappings.RowFetchResult getRowFetchResult()
    {
        return this.rowFetchResult;
    }


    public int getPrefetchSize()
    {
        return this.prefetchSize;
    }


    public Set<PK> getPrefetchLanguages()
    {
        return this.prefetchLanguages;
    }


    public ConversionMetaInformation getConversionResult()
    {
        return this.conversionResult;
    }
}
