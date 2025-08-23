package de.hybris.platform.adaptivesearch.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import java.util.Optional;

public interface AsConfigurationDao
{
    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel> Optional<T> findConfigurationByUid(Class<T> paramClass, CatalogVersionModel paramCatalogVersionModel, String paramString);
}
