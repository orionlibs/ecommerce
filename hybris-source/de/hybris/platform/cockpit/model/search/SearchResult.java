package de.hybris.platform.cockpit.model.search;

import java.util.List;

public interface SearchResult
{
    List<ResultObject> getResult();


    int getTotalCount();


    int getStart();
}
