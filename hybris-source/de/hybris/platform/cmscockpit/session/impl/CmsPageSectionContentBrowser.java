package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractBrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.CaptionBrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.DefaultSectionMainAreaBrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.DefaultSectionSearchContentBrowser;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.SectionBrowserModel;

public class CmsPageSectionContentBrowser extends DefaultSectionSearchContentBrowser
{
    protected AbstractBrowserComponent createToolbarComponent()
    {
        return null;
    }


    protected AbstractBrowserComponent createMainAreaComponent()
    {
        return (AbstractBrowserComponent)new DefaultSectionMainAreaBrowserComponent((SectionBrowserModel)getModel(), (AbstractContentBrowser)this);
    }


    protected AbstractBrowserComponent createCaptionComponent()
    {
        return (AbstractBrowserComponent)new CaptionBrowserComponent((BrowserModel)getModel(), (AbstractContentBrowser)this);
    }
}
