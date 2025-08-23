package de.hybris.platform.cms2.servicelayer.services.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import org.apache.log4j.Logger;

public abstract class AbstractCMSService extends AbstractBusinessService
{
    private static final Logger LOG = Logger.getLogger(AbstractCMSService.class.getName());
    protected static final String CURRENTSITE = "currentSite";
    protected static final String CURRENTCATALOGVERSION = "currentCatalogVersion";
    private SearchRestrictionService searchRestrictionService;
    private UserService userService;
    private BaseSiteService baseSiteService;


    public CatalogVersionModel getCurrentCatalogVersion()
    {
        CatalogVersionModel version = (CatalogVersionModel)getSessionService().getAttribute("currentCatalogVersion");
        if(LOG.isDebugEnabled() && version == null)
        {
            LOG.debug("No current catalog version set for user [" + this.userService.getCurrentUser().getUid() + "]");
        }
        return version;
    }


    public CMSSiteModel getCurrentSite()
    {
        BaseSiteModel site = getBaseSiteService().getCurrentBaseSite();
        if(site == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("No current site set for user [" + this.userService.getCurrentUser().getUid() + "]");
            }
            return null;
        }
        if(site instanceof CMSSiteModel)
        {
            return (CMSSiteModel)site;
        }
        throw new IllegalStateException("Current site (" + site + ") is not of type CMSSite.");
    }


    public SearchRestrictionService getSearchRestrictionService()
    {
        return this.searchRestrictionService;
    }


    public void setSearchRestrictionService(SearchRestrictionService searchRestrictionService)
    {
        this.searchRestrictionService = searchRestrictionService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }


    public BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }
}
