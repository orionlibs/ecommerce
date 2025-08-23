package de.hybris.platform.cms2.strategies.impl;

import de.hybris.platform.basecommerce.exceptions.BaseSiteActivationException;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.basecommerce.strategies.impl.DefaultActivateBaseSiteInSessionStrategy;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

@Deprecated(since = "1811", forRemoval = true)
public class DefaultCMSActivateBaseSiteInSessionStrategy extends DefaultActivateBaseSiteInSessionStrategy<BaseSiteModel>
{
    private static final Logger LOG = Logger.getLogger(DefaultCMSActivateBaseSiteInSessionStrategy.class);
    private CMSSiteService cmsSiteService;
    private final Lock mutex = new ReentrantLock();


    public CMSSiteService lookupCmsSiteService()
    {
        throw new UnsupportedOperationException("please override DefaultCMSActivateBaseSiteInSessionStrategy#lookupCmsSiteService() or use <lookup-method name=\"lookupCmsSiteService\" bean=\"..\">");
    }


    protected CMSSiteService getCmsPageService()
    {
        this.mutex.lock();
        try
        {
            if(this.cmsSiteService == null)
            {
                this.cmsSiteService = lookupCmsSiteService();
            }
        }
        finally
        {
            this.mutex.unlock();
        }
        return this.cmsSiteService;
    }


    public void activate(BaseSiteModel site) throws BaseSiteActivationException
    {
        super.activate(site);
        if(site instanceof CMSSiteModel)
        {
            CMSSiteModel newCmsSite = (CMSSiteModel)site;
            CatalogModel catalog = newCmsSite.getDefaultCatalog();
            if(catalog != null)
            {
                String catalogID = catalog.getId();
                String versionName = catalog.getActiveCatalogVersion().getVersion();
                CatalogVersionModel version = getCatalogVersionService().getCatalogVersion(catalogID, versionName);
                try
                {
                    getCmsPageService().setCurrentCatalogVersion(version);
                }
                catch(CMSItemNotFoundException ex)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Set session catalog version for site " + site + " failed " + ex.getMessage(), (Throwable)ex);
                    }
                    throw new BaseSiteActivationException(ex);
                }
            }
            else if(LOG.isDebugEnabled())
            {
                LOG.debug("No default catalog defined for site [" + site.getUid() + "] " + site.getName());
            }
        }
    }


    protected Set<CatalogModel> collectContentCatalogs(BaseSiteModel site)
    {
        Set<CatalogModel> ret = new HashSet<>(super.collectContentCatalogs(site));
        if(site instanceof CMSSiteModel)
        {
            ret.addAll(((CMSSiteModel)site).getContentCatalogs());
        }
        return ret;
    }
}
