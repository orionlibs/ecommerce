package de.hybris.platform.cockpit.services.dragdrop.impl;

import de.hybris.platform.cockpit.services.dragdrop.DragAndDropContext;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropWrapper;
import de.hybris.platform.cockpit.session.BrowserModel;

public class DefaultDragAndDropContext implements DragAndDropContext
{
    private BrowserModel browser;
    private DragAndDropWrapper wrapper;


    public DefaultDragAndDropContext(BrowserModel browser)
    {
        this.browser = browser;
        this.wrapper = browser.getArea().getPerspective().getDragAndDropWrapperService().getWrapper();
    }


    public void setBrowser(BrowserModel browser)
    {
        this.browser = browser;
    }


    public BrowserModel getBrowser()
    {
        return this.browser;
    }


    public void setWrapper(DragAndDropWrapper wrapper)
    {
        this.wrapper = wrapper;
    }


    public DragAndDropWrapper getWrapper()
    {
        return this.wrapper;
    }
}
