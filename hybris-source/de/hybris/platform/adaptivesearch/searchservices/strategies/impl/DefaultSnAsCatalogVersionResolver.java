package de.hybris.platform.adaptivesearch.searchservices.strategies.impl;

import de.hybris.platform.adaptivesearch.searchservices.strategies.SnAsCatalogVersionResolver;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.searchservices.admin.service.SnCommonConfigurationService;
import de.hybris.platform.searchservices.search.service.SnSearchContext;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSnAsCatalogVersionResolver implements SnAsCatalogVersionResolver
{
    private CatalogVersionService catalogVersionService;
    private SnCommonConfigurationService snCommonConfigurationService;


    public List<CatalogVersionModel> resolveCatalogVersions(SnSearchContext searchContext)
    {
        Collection<CatalogVersionModel> sessionCatalogVersions = this.catalogVersionService.getSessionCatalogVersions();
        if(CollectionUtils.isEmpty(sessionCatalogVersions))
        {
            return Collections.emptyList();
        }
        return (List<CatalogVersionModel>)sessionCatalogVersions.stream().filter(this::isSupportedCatalogVersion).collect(Collectors.toList());
    }


    protected boolean isSupportedCatalogVersion(CatalogVersionModel catalogVersion)
    {
        return (catalogVersion != null && !(catalogVersion instanceof de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel));
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public SnCommonConfigurationService getSnCommonConfigurationService()
    {
        return this.snCommonConfigurationService;
    }


    @Required
    public void setSnCommonConfigurationService(SnCommonConfigurationService snCommonConfigurationService)
    {
        this.snCommonConfigurationService = snCommonConfigurationService;
    }
}
