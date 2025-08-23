package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.events.impl.SectionModelEvent;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.SectionBrowserModel;

public abstract class AbstractBrowserSectionModel extends AbstractSectionModel implements BrowserSectionModel
{
    private SectionBrowserModel browserModel = null;
    protected boolean modified = false;


    public AbstractBrowserSectionModel(SectionBrowserModel browserModel)
    {
        this(browserModel, null);
    }


    AbstractBrowserSectionModel(SectionBrowserModel browserModel, Object rootItem)
    {
        super(rootItem);
        this.browserModel = browserModel;
    }


    public SectionBrowserModel getSectionBrowserModel()
    {
        return this.browserModel;
    }


    public void setSectionBrowserModel(SectionBrowserModel browserModel)
    {
        this.browserModel = browserModel;
        this.modified = true;
        fireEvent(new SectionModelEvent(this, "changed"));
    }
}
