package de.hybris.platform.personalizationservices.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.customization.CxCustomizationService;
import de.hybris.platform.personalizationservices.enums.CxCatalogLookupType;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.service.CxCatalogService;
import de.hybris.platform.personalizationservices.strategies.CxCatalogLookupStrategy;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxCatalogService implements CxCatalogService
{
    private CxConfigurationService cxConfigurationService;
    private CxCustomizationService cxCustomizationService;
    private MultiValuedMap<CxCatalogLookupType, CxCatalogLookupStrategy> registry;


    public List<CatalogVersionModel> getConfiguredCatalogVersions()
    {
        CxCatalogLookupType lookupType = this.cxConfigurationService.getCatalogLookupType();
        return getConfiguredCatalogVersions(lookupType);
    }


    public boolean isPersonalizationInCatalog(CatalogVersionModel catalogVersion)
    {
        List<CxCustomizationModel> customizations = this.cxCustomizationService.getCustomizations(catalogVersion);
        return !customizations.isEmpty();
    }


    protected List<CatalogVersionModel> getConfiguredCatalogVersions(CxCatalogLookupType type)
    {
        return (List<CatalogVersionModel>)this.registry.get(type).stream()
                        .map(CxCatalogLookupStrategy::getCatalogVersionsForCalculation)
                        .flatMap(Collection::stream)
                        .distinct()
                        .collect(Collectors.toList());
    }


    @Required
    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
    }


    @Required
    public void setCxCustomizationService(CxCustomizationService cxCustomizationService)
    {
        this.cxCustomizationService = cxCustomizationService;
    }


    @Autowired
    public void setCxCatalogLookupStrategies(List<CxCatalogLookupStrategy> strategies)
    {
        this.registry = (MultiValuedMap<CxCatalogLookupType, CxCatalogLookupStrategy>)new ArrayListValuedHashMap();
        strategies.forEach(s -> this.registry.put(s.getType(), s));
    }


    protected CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }


    protected CxCustomizationService getCxCustomizationService()
    {
        return this.cxCustomizationService;
    }


    protected MultiValuedMap<CxCatalogLookupType, CxCatalogLookupStrategy> getRegistry()
    {
        return this.registry;
    }
}
