package de.hybris.platform.basecommerce.strategies.impl;

import de.hybris.platform.basecommerce.exceptions.BaseSiteActivationException;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.basecommerce.strategies.ActivateBaseSiteInSessionStrategy;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultActivateBaseSiteInSessionStrategy<T extends BaseSiteModel> implements ActivateBaseSiteInSessionStrategy<T>
{
    private static final Logger LOG = Logger.getLogger(DefaultActivateBaseSiteInSessionStrategy.class);
    private CatalogVersionService catalogVersionService;


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    public void activate(T site)
    {
        try
        {
            getCatalogVersionService().setSessionCatalogVersions(collectCatalogVersions(site));
        }
        catch(Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Set session catalog version for site " + site + " failed " + e.getMessage(), e);
            }
            throw new BaseSiteActivationException(e);
        }
    }


    protected Collection<CatalogVersionModel> collectCatalogVersions(T site)
    {
        Set<CatalogModel> ret = collectContentCatalogs(site);
        Set<CatalogVersionModel> catalogVersions = new HashSet<>();
        for(CatalogModel catalog : ret)
        {
            CatalogVersionModel activeVersion = catalog.getActiveCatalogVersion();
            if(activeVersion == null)
            {
                throw new ModelNotFoundException("catalog [" + catalog.getId() + "] " + catalog.getName() + " has no active catalog version.");
            }
            catalogVersions.add(catalog.getActiveCatalogVersion());
        }
        return catalogVersions;
    }


    protected Set<CatalogModel> collectContentCatalogs(T site)
    {
        Set<CatalogModel> ret = new HashSet<>();
        if(site == null)
        {
            throw new IllegalArgumentException("No site specified.");
        }
        for(BaseStoreModel baseStore : site.getStores())
        {
            ret.addAll(baseStore.getCatalogs());
        }
        return ret;
    }
}
