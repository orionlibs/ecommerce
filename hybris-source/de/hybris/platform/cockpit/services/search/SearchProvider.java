package de.hybris.platform.cockpit.services.search;

import de.hybris.platform.cockpit.model.search.ExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchType;

public interface SearchProvider
{
    public static final String SELECTED_OBJECT_TEMPLATE = "objectTemplate";


    SearchType getDefaultRootType();


    void setDefaultRootType(SearchType paramSearchType);


    ExtendedSearchResult search(Query paramQuery);
}
