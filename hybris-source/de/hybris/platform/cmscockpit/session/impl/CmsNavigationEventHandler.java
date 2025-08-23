package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.events.impl.CmsNavigationEvent;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractRequestEventHandler;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;

public class CmsNavigationEventHandler extends AbstractRequestEventHandler
{
    private static final Logger LOG = Logger.getLogger(CmsNavigationEventHandler.class);
    public static final String SITE_KEY = "site";
    public static final String CATALOG_KEY = "catalog";
    public static final String PAGE_KEY = "page";
    private CMSAdminSiteService cmsAdminSiteService;
    private ModelService modelService;


    public void handleEvent(UICockpitPerspective perspective, Map<String, String[]> params)
    {
        CMSSiteModel site = (CMSSiteModel)getObject(params, "site");
        CatalogVersionModel catalog = (CatalogVersionModel)getObject(params, "catalog");
        AbstractPageModel page = (AbstractPageModel)getObject(params, "page");
        if(site != null && catalog != null)
        {
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new CmsNavigationEvent(perspective, site, catalog, page));
        }
        else
        {
            LOG.warn("Can not activate site. Reason: No valid site or catalog specified.");
        }
    }


    protected ItemModel getObject(Map<String, String[]> params, String key) throws IllegalStateException
    {
        if(StringUtils.isBlank(getParameter(params, key)))
        {
            LOG.warn("Can not activate object " + key + " since no " + key + " has been specified.");
        }
        else
        {
            try
            {
                return (ItemModel)getModelService().get(PK.parse(getParameter(params, key)));
            }
            catch(IllegalArgumentException iae)
            {
                LOG.warn("Can not activate " + key + ". Reason: No valid " + key + " specified.", iae);
            }
            catch(Exception e)
            {
                LOG.error("An error occurred while retrieving " + key, e);
            }
        }
        return null;
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        return this.cmsAdminSiteService;
    }


    protected ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)SpringUtil.getBean("modelService");
        }
        return this.modelService;
    }
}
