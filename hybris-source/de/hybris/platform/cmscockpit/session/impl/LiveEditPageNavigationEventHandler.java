package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Map;

public class LiveEditPageNavigationEventHandler extends CmsNavigationEventHandler
{
    public void handleEvent(UICockpitPerspective perspective, Map<String, String[]> params)
    {
        if(perspective instanceof LiveEditPerspective)
        {
            super.handleEvent(perspective, params);
            CMSSiteModel site = (CMSSiteModel)getObject(params, "site");
            AbstractPageModel page = (AbstractPageModel)getObject(params, "page");
            CatalogVersionModel catalog = (CatalogVersionModel)getObject(params, "catalog");
            LiveEditPageNavigationEvent liveEditPageNavigationEvent = new LiveEditPageNavigationEvent(this, perspective, site, page, catalog);
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)liveEditPageNavigationEvent);
        }
    }
}
