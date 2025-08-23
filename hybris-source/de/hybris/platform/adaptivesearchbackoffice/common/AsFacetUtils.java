package de.hybris.platform.adaptivesearchbackoffice.common;

import de.hybris.platform.adaptivesearch.data.AsFacetData;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractFacetConfigurationEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.NavigationContextData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchContextData;
import java.util.List;

public interface AsFacetUtils
{
    boolean isOpen(AsFacetData paramAsFacetData);


    void localizeFacets(NavigationContextData paramNavigationContextData, SearchContextData paramSearchContextData, List<? extends AbstractFacetConfigurationEditorData> paramList);
}
