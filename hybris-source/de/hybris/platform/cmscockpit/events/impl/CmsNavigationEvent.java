package de.hybris.platform.cmscockpit.events.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cockpit.events.impl.AbstractCockpitEvent;

public class CmsNavigationEvent extends AbstractCockpitEvent
{
    private final CMSSiteModel site;
    private final CatalogVersionModel catalog;
    private final AbstractPageModel page;


    public CmsNavigationEvent(Object source, CMSSiteModel site, CatalogVersionModel catalog, AbstractPageModel page)
    {
        super(source);
        this.site = site;
        this.catalog = catalog;
        this.page = page;
    }


    public CmsNavigationEvent(Object source, CMSSiteModel site, CatalogVersionModel catalog)
    {
        this(source, site, catalog, null);
    }


    public CMSSiteModel getSite()
    {
        return this.site;
    }


    public CatalogVersionModel getCatalog()
    {
        return this.catalog;
    }


    public AbstractPageModel getPage()
    {
        return this.page;
    }
}
