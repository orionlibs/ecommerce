package de.hybris.platform.cms2.servicelayer.services.admin.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractCMSAdminService extends AbstractBusinessService
{
    private static final Logger LOG = Logger.getLogger(AbstractCMSAdminService.class);
    protected static final String ACTIVESITE = "activeSite";
    protected static final String ACTIVECATALOGVERSION = "activeCatalogVersion";
    protected static final String CLONE_CONTEXT = "cloneContext";
    protected static final String RESTORE_CONTEXT = "restoreContext";
    protected static final String TYPE_CONTEXT = "typeContext";
    protected static final String ORIGINAL_ITEM_CONTEXT = "originalItemContext";
    private UserService userService;
    private BaseSiteService baseSiteService;


    public CatalogVersionModel getActiveCatalogVersion()
    {
        PK versionPK = (PK)getSessionService().getAttribute("activeCatalogVersion");
        if(versionPK != null)
        {
            try
            {
                return (CatalogVersionModel)getModelService().get(versionPK);
            }
            catch(Exception e)
            {
                LOG.error("Active catalog version could not be retrieved", e);
            }
        }
        return null;
    }


    public CMSSiteModel getActiveSite()
    {
        PK sitePk = (PK)getSessionService().getAttribute("activeSite");
        if(sitePk != null)
        {
            try
            {
                return (CMSSiteModel)getModelService().get(sitePk);
            }
            catch(Exception e)
            {
                LOG.error("Active site could not be retrieved", e);
            }
        }
        else
        {
            BaseSiteModel baseSite = getBaseSiteService().getCurrentBaseSite();
            if(baseSite instanceof CMSSiteModel)
            {
                return (CMSSiteModel)baseSite;
            }
        }
        return null;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    protected BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }


    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }
}
