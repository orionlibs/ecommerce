package de.hybris.platform.solrfacetsearch.search.context.listeners;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchListener;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class SessionInitializationListener implements FacetSearchListener
{
    private CommonI18NService commonI18NService;
    private CatalogVersionService catalogVersionService;


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
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


    public void beforeSearch(FacetSearchContext facetSearchContext) throws FacetSearchException
    {
        SearchQuery searchQuery = facetSearchContext.getSearchQuery();
        if(searchQuery.getLanguage() != null)
        {
            LanguageModel language = this.commonI18NService.getLanguage(searchQuery.getLanguage());
            this.commonI18NService.setCurrentLanguage(language);
        }
        if(searchQuery.getCurrency() != null)
        {
            CurrencyModel currency = this.commonI18NService.getCurrency(searchQuery.getCurrency());
            this.commonI18NService.setCurrentCurrency(currency);
        }
        if(CollectionUtils.isNotEmpty(searchQuery.getCatalogVersions()))
        {
            this.catalogVersionService.setSessionCatalogVersions(searchQuery.getCatalogVersions());
        }
    }


    public void afterSearch(FacetSearchContext facetSearchContext) throws FacetSearchException
    {
    }


    public void afterSearchError(FacetSearchContext facetSearchContext) throws FacetSearchException
    {
    }
}
