package de.hybris.platform.genericsearch;

import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

public interface GenericSearchService
{
    <T> SearchResult<T> search(GenericQuery paramGenericQuery);


    <T> SearchResult<T> search(GenericSearchQuery paramGenericSearchQuery);
}
