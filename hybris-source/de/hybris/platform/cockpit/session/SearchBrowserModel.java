package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModel;
import de.hybris.platform.cockpit.model.search.ExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.Query;

public interface SearchBrowserModel extends PageableBrowserModel
{
    void setLastQuery(Query paramQuery);


    Query getLastQuery();


    void updateItems(Query paramQuery);


    void setSimpleQuery(String paramString);


    String getSimpleQuery();


    void setResult(ExtendedSearchResult paramExtendedSearchResult);


    ExtendedSearchResult getResult();


    AdvancedSearchModel getAdvancedSearchModel();


    boolean isAdvancedSearchVisible();


    void setAdvancedSearchVisible(boolean paramBoolean);


    boolean isAdvancedSearchSticky();


    void setAdvancedSearchSticky(boolean paramBoolean);
}
