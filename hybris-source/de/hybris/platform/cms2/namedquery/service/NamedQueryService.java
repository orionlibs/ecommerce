package de.hybris.platform.cms2.namedquery.service;

import de.hybris.platform.cms2.exceptions.InvalidNamedQueryException;
import de.hybris.platform.cms2.exceptions.SearchExecutionNamedQueryException;
import de.hybris.platform.cms2.namedquery.NamedQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;

public interface NamedQueryService
{
    <T> List<T> search(NamedQuery paramNamedQuery) throws InvalidNamedQueryException, SearchExecutionNamedQueryException;


    <T> SearchResult<T> getSearchResult(NamedQuery paramNamedQuery) throws InvalidNamedQueryException, SearchExecutionNamedQueryException;
}
