package de.hybris.platform.personalizationservices.strategies.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationservices.model.config.CxConfigModel;
import de.hybris.platform.personalizationservices.strategies.CxConfigurationLookupStrategy;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxConfigurationLookupStrategy implements CxConfigurationLookupStrategy
{
    private static final String BASE_SITE_NULL_MESSAGE = "Base site must not be null";
    private static final String CATALOG_VERSION_NULL_MESSAGE = "Catalog version must not be null";
    private BaseSiteService baseSiteService;


    public Optional<CxConfigModel> getConfiguration()
    {
        return getCurrentBaseSite().flatMap(this::getConfiguration);
    }


    public Optional<CxConfigModel> getConfiguration(BaseSiteModel baseSite)
    {
        ServicesUtil.validateParameterNotNull(baseSite, "Base site must not be null");
        return Optional.ofNullable(baseSite.getCxConfig());
    }


    public Set<CxConfigModel> getConfigurations(CatalogVersionModel catalogVersion)
    {
        ServicesUtil.validateParameterNotNull(catalogVersion, "Catalog version must not be null");
        if(catalogVersion.getCatalog() == null || CollectionUtils.isEmpty(catalogVersion.getCatalog().getBaseStores()))
        {
            return Collections.emptySet();
        }
        Set<CxConfigModel> configSet = (Set<CxConfigModel>)catalogVersion.getCatalog().getBaseStores().stream().map(baseStore -> baseStore.getCmsSites()).flatMap(Collection::stream).map(site -> site.getCxConfig()).filter(Objects::nonNull).collect(Collectors.toSet());
        return configSet;
    }


    protected Optional<BaseSiteModel> getCurrentBaseSite()
    {
        return Optional.ofNullable(this.baseSiteService.getCurrentBaseSite());
    }


    protected BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }


    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }
}
