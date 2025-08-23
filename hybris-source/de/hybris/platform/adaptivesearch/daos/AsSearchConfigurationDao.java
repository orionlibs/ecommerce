package de.hybris.platform.adaptivesearch.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AsSearchConfigurationDao
{
    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel> List<T> findAllSearchConfigurations();


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel> List<T> findSearchConfigurationsByCatalogVersion(CatalogVersionModel paramCatalogVersionModel);


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel> Optional<T> findSearchConfigurationByUid(CatalogVersionModel paramCatalogVersionModel, String paramString);


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel> List<T> findSearchConfigurations(Class<T> paramClass, Map<String, Object> paramMap);
}
