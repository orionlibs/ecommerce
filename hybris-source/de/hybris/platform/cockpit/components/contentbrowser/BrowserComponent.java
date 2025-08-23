package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.session.BrowserModel;

public interface BrowserComponent extends CockpitComponent
{
    BrowserModel getModel();


    void setModel(BrowserModel paramBrowserModel);
}
