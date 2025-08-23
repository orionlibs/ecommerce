package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.components.contentbrowser.BrowserComponent;

public interface BrowserSectionRenderer
{
    BrowserComponent createSectionView(BrowserSectionModel paramBrowserSectionModel);
}
