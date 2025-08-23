package de.hybris.platform.solrfacetsearch.indexer.listeners;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchListener;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerListener;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueryContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueryListener;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import org.springframework.beans.factory.annotation.Required;

public class SessionInitializationListener implements IndexerQueryListener, IndexerListener, IndexerBatchListener
{
    private UserService userService;
    private I18NService i18nService;
    private CatalogVersionService catalogVersionService;


    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    public I18NService getI18nService()
    {
        return this.i18nService;
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public void beforeQuery(IndexerQueryContext queryContext) throws IndexerException
    {
        initializeSession(queryContext.getFacetSearchConfig(), queryContext.getIndexedType());
    }


    public void afterQuery(IndexerQueryContext queryContext) throws IndexerException
    {
    }


    public void afterQueryError(IndexerQueryContext queryContext) throws IndexerException
    {
    }


    public void beforeIndex(IndexerContext context) throws IndexerException
    {
        initializeSession(context.getFacetSearchConfig(), context.getIndexedType());
    }


    public void afterIndex(IndexerContext context) throws IndexerException
    {
    }


    public void afterIndexError(IndexerContext context) throws IndexerException
    {
    }


    public void beforeBatch(IndexerBatchContext batchContext) throws IndexerException
    {
        initializeSession(batchContext.getFacetSearchConfig(), batchContext.getIndexedType());
    }


    public void afterBatch(IndexerBatchContext batchContext) throws IndexerException
    {
    }


    public void afterBatchError(IndexerBatchContext batchContext) throws IndexerException
    {
    }


    protected void initializeSession(FacetSearchConfig facetSearchConfig, IndexedType indexedType)
    {
        IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
        this.i18nService.setLocalizationFallbackEnabled(indexConfig.isEnabledLanguageFallbackMechanism());
        this.catalogVersionService.setSessionCatalogVersions(indexConfig.getCatalogVersions());
    }
}
