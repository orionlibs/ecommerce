package de.hybris.platform.cmscockpit.components.listsectionbrowser.impl;

import de.hybris.platform.cockpit.session.ListBrowserSectionModel;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Toolbarbutton;

public class CmsPageBrowserSectionComponent extends CmsListBrowserSectionComponent
{
    public CmsPageBrowserSectionComponent(ListBrowserSectionModel sectionModel)
    {
        super(sectionModel);
    }


    protected EventListener getAddBtnEventListener(Div captionDiv, Toolbarbutton addElementButton)
    {
        return (EventListener)new Object(this, captionDiv);
    }
}
