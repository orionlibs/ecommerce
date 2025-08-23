package de.hybris.platform.personalizationservices.strategies;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationservices.enums.CxCatalogLookupType;
import java.util.List;

public interface CxCatalogLookupStrategy
{
    List<CatalogVersionModel> getCatalogVersionsForCalculation();


    CxCatalogLookupType getType();
}
