package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.BrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.ListBrowserSectionComponent;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.BrowserSectionRenderer;
import de.hybris.platform.cockpit.session.ListBrowserSectionModel;

public class ListBrowserSectionRenderer implements BrowserSectionRenderer
{
    public BrowserComponent createSectionView(BrowserSectionModel sectionModel)
    {
        if(sectionModel instanceof ListBrowserSectionModel)
        {
            ListBrowserSectionComponent listBrowserSectionComponent = new ListBrowserSectionComponent((ListBrowserSectionModel)sectionModel);
            listBrowserSectionComponent.setWidth("100%");
            return (BrowserComponent)listBrowserSectionComponent;
        }
        throw new IllegalArgumentException("Section model must be a list browser section model.");
    }
}
