package de.hybris.platform.adaptivesearchfacades.facades;

import de.hybris.adaptivesearchfacades.data.AsSearchProfileData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import java.util.List;
import java.util.Map;

public interface AsSearchProfileFacade
{
    List<AsSearchProfileData> getSearchProfiles(String paramString, Map<String, String> paramMap);


    SearchPageData<AsSearchProfileData> getSearchProfiles(String paramString, Map<String, String> paramMap, SearchPageData<?> paramSearchPageData);
}
