package de.hybris.platform.webservicescommons.util.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.webservicescommons.util.LocalViewExecutor;
import java.util.function.Supplier;

public class DefaultLocalViewExecutor implements LocalViewExecutor
{
    private SessionService sessionService;
    private CatalogVersionService catalogVersionService;
    private SearchRestrictionService searchRestrictionService;


    public <T> T executeInLocalView(Supplier<T> action)
    {
        return (T)this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, action));
    }


    public <T> T executeWithAllCatalogs(Supplier<T> action)
    {
        return executeInLocalView(() -> {
            setAllCatalogs();
            return action.get();
        });
    }


    protected void setAllCatalogs()
    {
        try
        {
            this.searchRestrictionService.disableSearchRestrictions();
            this.catalogVersionService.setSessionCatalogVersions(this.catalogVersionService.getAllCatalogVersions());
        }
        finally
        {
            this.searchRestrictionService.enableSearchRestrictions();
        }
    }


    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    public void setSearchRestrictionService(SearchRestrictionService searchRestrictionService)
    {
        this.searchRestrictionService = searchRestrictionService;
    }


    public SearchRestrictionService getSearchRestrictionService()
    {
        return this.searchRestrictionService;
    }


    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public SessionService getSessionService()
    {
        return this.sessionService;
    }
}
