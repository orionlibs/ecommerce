package com.hybris.backoffice.solrsearch.events;

import de.hybris.platform.core.PK;
import java.util.List;

public interface SolrIndexSynchronizationStrategy
{
    void updateItem(String paramString, long paramLong);


    void updateItems(String paramString, List<PK> paramList);


    void removeItem(String paramString, long paramLong);


    void removeItems(String paramString, List<PK> paramList);
}
