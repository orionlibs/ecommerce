package de.hybris.platform.jalo;

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
