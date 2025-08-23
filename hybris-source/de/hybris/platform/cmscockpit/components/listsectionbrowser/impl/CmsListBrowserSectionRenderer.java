package de.hybris.platform.cmscockpit.components.listsectionbrowser.impl;

import de.hybris.platform.cockpit.components.contentbrowser.BrowserComponent;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.BrowserSectionRenderer;
import de.hybris.platform.cockpit.session.ListBrowserSectionModel;

public class CmsListBrowserSectionRenderer implements BrowserSectionRenderer
{
    public BrowserComponent createSectionView(BrowserSectionModel sectionModel)
    {
        if(sectionModel instanceof ListBrowserSectionModel)
        {
            CmsListBrowserSectionComponent cmsListBrowserSectionComponent = new CmsListBrowserSectionComponent((ListBrowserSectionModel)sectionModel);
            cmsListBrowserSectionComponent.setWidth("100%");
            return (BrowserComponent)cmsListBrowserSectionComponent;
        }
        throw new IllegalArgumentException("Section model must be a list browser section model.");
    }
}
