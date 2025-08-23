package de.hybris.platform.adaptivesearch.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AsSearchProfileDao
{
    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> List<T> findAllSearchProfiles();


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> List<T> findSearchProfilesByIndexTypesAndCatalogVersions(List<String> paramList, List<CatalogVersionModel> paramList1);


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> List<T> findSearchProfilesByCatalogVersion(CatalogVersionModel paramCatalogVersionModel);


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> Optional<T> findSearchProfileByCode(CatalogVersionModel paramCatalogVersionModel, String paramString);


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> List<T> getSearchProfiles(String paramString, Map<String, Object> paramMap);


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel> SearchPageData<T> getSearchProfiles(String paramString, Map<String, Object> paramMap, SearchPageData<?> paramSearchPageData);
}
