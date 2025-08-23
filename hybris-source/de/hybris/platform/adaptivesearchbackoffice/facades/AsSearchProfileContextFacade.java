package de.hybris.platform.adaptivesearchbackoffice.facades;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearchbackoffice.data.NavigationContextData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchContextData;

public interface AsSearchProfileContextFacade
{
    AsSearchProfileContext createSearchProfileContext(NavigationContextData paramNavigationContextData);


    AsSearchProfileContext createSearchProfileContext(NavigationContextData paramNavigationContextData, SearchContextData paramSearchContextData);
}
