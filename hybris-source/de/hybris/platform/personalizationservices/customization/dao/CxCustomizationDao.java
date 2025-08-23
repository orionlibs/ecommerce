package de.hybris.platform.personalizationservices.customization.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CxCustomizationDao extends Dao
{
    Optional<CxCustomizationModel> findCustomizationByCode(String paramString, CatalogVersionModel paramCatalogVersionModel);


    List<CxCustomizationModel> findCustomizations(CatalogVersionModel paramCatalogVersionModel);


    SearchPageData<CxCustomizationModel> findCustomizations(CatalogVersionModel paramCatalogVersionModel, Map<String, String> paramMap, SearchPageData<?> paramSearchPageData);
}
