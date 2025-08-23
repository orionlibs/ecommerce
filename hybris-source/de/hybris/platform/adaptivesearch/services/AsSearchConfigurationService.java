package de.hybris.platform.adaptivesearch.services;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsSearchConfigurationInfoData;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AsSearchConfigurationService
{
    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel> List<T> getAllSearchConfigurations();


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel> List<T> getSearchConfigurationsForCatalogVersion(CatalogVersionModel paramCatalogVersionModel);


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel> Optional<T> getSearchConfigurationForUid(CatalogVersionModel paramCatalogVersionModel, String paramString);


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel> Optional<T> getSearchConfigurationForContext(AsSearchProfileContext paramAsSearchProfileContext, AbstractAsSearchProfileModel paramAbstractAsSearchProfileModel);


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel> T getOrCreateSearchConfigurationForContext(AsSearchProfileContext paramAsSearchProfileContext, AbstractAsSearchProfileModel paramAbstractAsSearchProfileModel);


    AsSearchConfigurationInfoData getSearchConfigurationInfoForContext(AsSearchProfileContext paramAsSearchProfileContext, AbstractAsSearchProfileModel paramAbstractAsSearchProfileModel);


    <T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel> T cloneSearchConfiguration(T paramT);


    Set<String> getSearchConfigurationQualifiers(AbstractAsSearchProfileModel paramAbstractAsSearchProfileModel);
}
