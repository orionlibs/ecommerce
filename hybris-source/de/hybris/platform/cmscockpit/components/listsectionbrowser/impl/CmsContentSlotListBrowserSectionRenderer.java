package de.hybris.platform.cmscockpit.components.listsectionbrowser.impl;

import de.hybris.platform.cockpit.components.contentbrowser.BrowserComponent;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.BrowserSectionRenderer;
import de.hybris.platform.cockpit.session.ListBrowserSectionModel;

public class CmsContentSlotListBrowserSectionRenderer implements BrowserSectionRenderer
{
    public BrowserComponent createSectionView(BrowserSectionModel sectionModel)
    {
        if(sectionModel instanceof ListBrowserSectionModel)
        {
            CmsContentSlotListBrowserSectionComponent cmsContentSlotListBrowserSectionComponent = new CmsContentSlotListBrowserSectionComponent((ListBrowserSectionModel)sectionModel);
            cmsContentSlotListBrowserSectionComponent.setWidth("100%");
            return (BrowserComponent)cmsContentSlotListBrowserSectionComponent;
        }
        throw new IllegalArgumentException("Section model must be a list browser section model.");
    }
}
