package de.hybris.platform.servicelayer.search;

import java.util.List;

public interface SearchResult<E>
{
    int getCount();


    int getTotalCount();


    List<E> getResult();


    int getRequestedStart();


    int getRequestedCount();


    default String getDataSourceId()
    {
        return null;
    }
}
