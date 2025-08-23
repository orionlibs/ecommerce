package de.hybris.platform.cockpit.components.listeditor;

import de.hybris.platform.cockpit.session.AdvancedBrowserModel;

@Deprecated
public interface BrowserAwareComponent
{
    void setModel(AdvancedBrowserModel paramAdvancedBrowserModel);


    AdvancedBrowserModel getModel();
}
