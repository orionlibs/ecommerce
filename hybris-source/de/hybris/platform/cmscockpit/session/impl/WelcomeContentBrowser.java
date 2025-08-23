package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractBrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.DefaultAdvancedContentBrowser;

public class WelcomeContentBrowser extends DefaultAdvancedContentBrowser
{
    protected AbstractBrowserComponent createMainAreaComponent()
    {
        return (AbstractBrowserComponent)new WelcomeMainAreaBrowserComponent(getModel(), (AbstractContentBrowser)this);
    }


    public WelcomeBrowserModel getModel()
    {
        return (WelcomeBrowserModel)super.getModel();
    }
}
