package de.hybris.platform.personalizationcms.strategy;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.personalizationservices.model.config.CxConfigModel;
import de.hybris.platform.personalizationservices.strategies.impl.DefaultCxConfigurationLookupStrategy;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CmsCxConfigurationLookupStrategy extends DefaultCxConfigurationLookupStrategy
{
    private static final String CATALOG_VERSION_NULL_MESSAGE = "Catalog version must not be null";


    public Set<CxConfigModel> getConfigurations(CatalogVersionModel catalogVersion)
    {
        ServicesUtil.validateParameterNotNull(catalogVersion, "Catalog version must not be null");
        if(catalogVersion.getCatalog() instanceof ContentCatalogModel)
        {
            ContentCatalogModel contentCatalog = (ContentCatalogModel)catalogVersion.getCatalog();
            return (Set<CxConfigModel>)contentCatalog.getCmsSites().stream()
                            .map(site -> site.getCxConfig())
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());
        }
        return super.getConfigurations(catalogVersion);
    }
}
