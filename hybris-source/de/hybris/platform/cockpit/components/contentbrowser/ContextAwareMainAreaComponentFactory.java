package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.session.BrowserModel;

public interface ContextAwareMainAreaComponentFactory extends MainAreaComponentFactory
{
    boolean isAvailable(BrowserModel paramBrowserModel);


    boolean hasOwnModel();
}
