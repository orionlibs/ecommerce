package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;

public interface CMSBrowserArea extends UIBrowserArea
{
    AdvancedBrowserModel getWelcomeBrowserModel();


    void setWelcomeBrowserModel(AdvancedBrowserModel paramAdvancedBrowserModel);
}
