package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cmscockpit.components.contentbrowser.CMSContentEditorSectionComponent;
import de.hybris.platform.cockpit.components.contentbrowser.BrowserComponent;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.BrowserSectionRenderer;

public class CMSContentEditorSectionRenderer implements BrowserSectionRenderer
{
    public BrowserComponent createSectionView(BrowserSectionModel sectionModel)
    {
        if(sectionModel instanceof ContentEditorBrowserSectionModel)
        {
            return ((ContentEditorBrowserSectionModel)sectionModel).isEnabled() ? (BrowserComponent)new CMSContentEditorSectionComponent((ContentEditorBrowserSectionModel)sectionModel) :
                            null;
        }
        throw new IllegalArgumentException("Section model not of type 'ContentEditorBrowserSectionModel'.");
    }
}
