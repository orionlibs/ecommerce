package de.hybris.platform.cmscockpit.components.listsectionbrowser.impl;

import de.hybris.platform.cmscockpit.session.impl.CmsListBrowserSectionModel;
import de.hybris.platform.cockpit.components.contentbrowser.BrowserComponent;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.impl.ListBrowserSectionRenderer;

public class CMSStructListBrowserSectionRenderer extends ListBrowserSectionRenderer
{
    public BrowserComponent createSectionView(BrowserSectionModel sectionModel)
    {
        if(sectionModel instanceof CmsListBrowserSectionModel)
        {
            CmsStructListBrowserSectionComponent cmsStructListBrowserSectionComponent = new CmsStructListBrowserSectionComponent((CmsListBrowserSectionModel)sectionModel);
            cmsStructListBrowserSectionComponent.setWidth("100%");
            return (BrowserComponent)cmsStructListBrowserSectionComponent;
        }
        throw new IllegalArgumentException("Section model must be a list browser section model.");
    }
}
