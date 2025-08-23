package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cmscockpit.events.impl.PageViewNavigationEvent;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Map;
import org.apache.log4j.Logger;

public class PageViewNavigationEventHandler extends CmsNavigationEventHandler
{
    private static final Logger LOG = Logger.getLogger(PageViewNavigationEventHandler.class);
    public static final String PAGE_KEY = "page";


    public void handleEvent(UICockpitPerspective perspective, Map<String, String[]> params)
    {
        CMSSiteModel site = (CMSSiteModel)getObject(params, "site");
        CatalogVersionModel catalog = (CatalogVersionModel)getObject(params, "catalog");
        AbstractPageModel page = (AbstractPageModel)getObject(params, "page");
        if(site != null && catalog != null && page != null)
        {
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new PageViewNavigationEvent(perspective, site, catalog, page));
        }
        else
        {
            LOG.warn("Can not activate site. Reason: No valid site or catalog or page specified.");
        }
    }
}
