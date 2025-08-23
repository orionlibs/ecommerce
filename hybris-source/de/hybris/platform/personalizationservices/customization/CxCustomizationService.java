package de.hybris.platform.personalizationservices.customization;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxCustomizationsGroupModel;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface CxCustomizationService
{
    Optional<CxCustomizationModel> getCustomization(String paramString, CatalogVersionModel paramCatalogVersionModel);


    List<CxCustomizationModel> getCustomizations(CatalogVersionModel paramCatalogVersionModel);


    SearchPageData<CxCustomizationModel> getCustomizations(CatalogVersionModel paramCatalogVersionModel, Map<String, String> paramMap, SearchPageData<?> paramSearchPageData);


    CxCustomizationsGroupModel getDefaultGroup(CatalogVersionModel paramCatalogVersionModel);


    CxCustomizationModel createCustomization(CxCustomizationModel paramCxCustomizationModel, CxCustomizationsGroupModel paramCxCustomizationsGroupModel, Integer paramInteger);


    default boolean isDefaultGroup(CatalogVersionModel catalogVersion)
    {
        try
        {
            getDefaultGroup(catalogVersion);
            return true;
        }
        catch(NoSuchElementException ex)
        {
            return false;
        }
    }
}
