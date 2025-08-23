package de.hybris.platform.servicelayer.search.paginated;

import de.hybris.platform.core.servicelayer.data.SearchPageData;

public interface PaginatedFlexibleSearchService
{
    <T> SearchPageData<T> search(PaginatedFlexibleSearchParameter paramPaginatedFlexibleSearchParameter);
}
