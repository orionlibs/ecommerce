package de.hybris.platform.cmscockpit.events.impl;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cockpit.events.impl.AbstractCockpitEvent;

public class CmsLiveEditEvent extends AbstractCockpitEvent
{
    private final CMSSiteModel site;
    private final String url;


    public CmsLiveEditEvent(Object source, CMSSiteModel site, String url)
    {
        super(source);
        this.site = site;
        this.url = url;
    }


    public CMSSiteModel getSite()
    {
        return this.site;
    }


    public String getUrl()
    {
        return (this.url == null) ? "" : this.url;
    }
}
