package de.hybris.platform.adaptivesearch.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AsSearchProfileService
{
    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> List<T> getAllSearchProfiles();


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> List<T> getSearchProfilesForIndexTypesAndCatalogVersions(List<String> paramList, List<CatalogVersionModel> paramList1);


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> List<T> getSearchProfilesForCatalogVersion(CatalogVersionModel paramCatalogVersionModel);


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> Optional<T> getSearchProfileForCode(CatalogVersionModel paramCatalogVersionModel, String paramString);


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> List<T> getSearchProfiles(String paramString, Map<String, Object> paramMap);


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> SearchPageData<T> getSearchProfiles(String paramString, Map<String, Object> paramMap, SearchPageData<?> paramSearchPageData);


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> T cloneSearchProfile(T paramT);
}
