package de.hybris.platform.personalizationservices.strategies;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationservices.model.config.CxConfigModel;
import java.util.Optional;
import java.util.Set;

public interface CxConfigurationLookupStrategy
{
    Optional<CxConfigModel> getConfiguration();


    Optional<CxConfigModel> getConfiguration(BaseSiteModel paramBaseSiteModel);


    Set<CxConfigModel> getConfigurations(CatalogVersionModel paramCatalogVersionModel);
}
