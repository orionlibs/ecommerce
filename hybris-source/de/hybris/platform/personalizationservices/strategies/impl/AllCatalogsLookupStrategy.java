package de.hybris.platform.personalizationservices.strategies.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationservices.enums.CxCatalogLookupType;
import de.hybris.platform.personalizationservices.service.CxCatalogService;
import de.hybris.platform.personalizationservices.strategies.CxCatalogLookupStrategy;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class AllCatalogsLookupStrategy implements CxCatalogLookupStrategy
{
    private CxCatalogService cxCatalogService;
    private CatalogVersionService catalogVersionService;


    public List<CatalogVersionModel> getCatalogVersionsForCalculation()
    {
        Objects.requireNonNull(this.cxCatalogService);
        return (List<CatalogVersionModel>)this.catalogVersionService.getSessionCatalogVersions().stream().filter(this.cxCatalogService::isPersonalizationInCatalog)
                        .collect(Collectors.toList());
    }


    public void setCxCatalogService(CxCatalogService cxCatalogService)
    {
        this.cxCatalogService = cxCatalogService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public CxCatalogLookupType getType()
    {
        return CxCatalogLookupType.ALL_CATALOGS;
    }


    protected CxCatalogService getCxCatalogService()
    {
        return this.cxCatalogService;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }
}
