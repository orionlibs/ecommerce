package com.hybris.backoffice.searchservices.events;

import de.hybris.platform.core.PK;
import java.util.List;

public interface SearchservicesIndexSynchronizationStrategy
{
    void updateItem(String paramString, long paramLong);


    void updateItems(String paramString, List<PK> paramList);


    void removeItem(String paramString, long paramLong);


    void removeItems(String paramString, List<PK> paramList);
}
