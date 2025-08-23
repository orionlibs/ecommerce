package de.hybris.platform.personalizationservices.customization.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationservices.model.CxCustomizationsGroupModel;
import java.util.NoSuchElementException;

public interface CxCustomizationGroupDao
{
    CxCustomizationsGroupModel getDefaultGroup(CatalogVersionModel paramCatalogVersionModel);


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
