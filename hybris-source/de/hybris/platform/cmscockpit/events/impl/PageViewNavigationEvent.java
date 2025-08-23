package de.hybris.platform.cmscockpit.events.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;

public class PageViewNavigationEvent extends CmsNavigationEvent
{
    private final AbstractPageModel page;


    public PageViewNavigationEvent(Object source, CMSSiteModel site, CatalogVersionModel catalog, AbstractPageModel page)
    {
        super(source, site, catalog);
        this.page = page;
    }


    public AbstractPageModel getPagePk()
    {
        return this.page;
    }
}
