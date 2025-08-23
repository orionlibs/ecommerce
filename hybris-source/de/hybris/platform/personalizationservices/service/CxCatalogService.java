package de.hybris.platform.personalizationservices.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import java.util.List;

public interface CxCatalogService
{
    List<CatalogVersionModel> getConfiguredCatalogVersions();


    boolean isPersonalizationInCatalog(CatalogVersionModel paramCatalogVersionModel);
}
