package de.hybris.platform.adaptivesearchbackoffice.facades.impl;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.services.AsSearchConfigurationService;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileService;
import de.hybris.platform.adaptivesearchbackoffice.data.CatalogVersionData;
import de.hybris.platform.adaptivesearchbackoffice.data.NavigationContextData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchContextData;
import de.hybris.platform.adaptivesearchbackoffice.facades.AsSearchConfigurationFacade;
import de.hybris.platform.adaptivesearchbackoffice.facades.AsSearchProfileContextFacade;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import java.util.Optional;

public class DefaultAsSearchConfigurationFacade implements AsSearchConfigurationFacade
{
    private AsSearchProfileContextFacade asSearchProfileContextFacade;
    private AsSearchProfileService asSearchProfileService;
    private AsSearchConfigurationService asSearchConfigurationService;
    private CatalogVersionService catalogVersionService;


    public AbstractAsConfigurableSearchConfigurationModel getOrCreateSearchConfiguration(NavigationContextData navigationContext, SearchContextData searchContext)
    {
        AsSearchProfileContext searchProfileContext = this.asSearchProfileContextFacade.createSearchProfileContext(navigationContext, searchContext);
        CatalogVersionModel catalogVersion = resolveCatalogVersion(navigationContext.getCatalogVersion());
        Optional<AbstractAsSearchProfileModel> searchProfile = this.asSearchProfileService.getSearchProfileForCode(catalogVersion, navigationContext
                        .getCurrentSearchProfile());
        return (AbstractAsConfigurableSearchConfigurationModel)this.asSearchConfigurationService.getOrCreateSearchConfigurationForContext(searchProfileContext, searchProfile.get());
    }


    protected CatalogVersionModel resolveCatalogVersion(CatalogVersionData catalogVersion)
    {
        if(catalogVersion == null)
        {
            return null;
        }
        return this.catalogVersionService.getCatalogVersion(catalogVersion.getCatalogId(), catalogVersion.getVersion());
    }


    public AsSearchProfileContextFacade getAsSearchProfileContextFacade()
    {
        return this.asSearchProfileContextFacade;
    }


    public void setAsSearchProfileContextFacade(AsSearchProfileContextFacade asSearchProfileContextFacade)
    {
        this.asSearchProfileContextFacade = asSearchProfileContextFacade;
    }


    public AsSearchProfileService getAsSearchProfileService()
    {
        return this.asSearchProfileService;
    }


    public void setAsSearchProfileService(AsSearchProfileService asSearchProfileService)
    {
        this.asSearchProfileService = asSearchProfileService;
    }


    public AsSearchConfigurationService getAsSearchConfigurationService()
    {
        return this.asSearchConfigurationService;
    }


    public void setAsSearchConfigurationService(AsSearchConfigurationService asSearchConfigurationService)
    {
        this.asSearchConfigurationService = asSearchConfigurationService;
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }
}
