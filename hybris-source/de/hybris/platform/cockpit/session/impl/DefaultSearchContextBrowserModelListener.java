package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentContextBrowser;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;

public class DefaultSearchContextBrowserModelListener extends DefaultSearchBrowserModelListener
{
    private final AbstractBrowserArea area;


    public DefaultSearchContextBrowserModelListener(AbstractBrowserArea area)
    {
        super(area);
        this.area = area;
    }


    void contextItemsChanged(AdvancedBrowserModel browserModel, boolean cleanContextHeader)
    {
        if(!this.area.isBrowserMinimized((BrowserModel)browserModel))
        {
            AbstractContentBrowser contentBrowser = this.area.getCorrespondingContentBrowser((BrowserModel)browserModel);
            if(contentBrowser != null)
            {
                if(contentBrowser instanceof AbstractContentContextBrowser)
                {
                    ((AbstractContentContextBrowser)contentBrowser).updateContextArea(cleanContextHeader);
                }
                else
                {
                    contentBrowser.updateContextArea();
                }
            }
        }
        this.area.fireBrowserChanged((BrowserModel)browserModel);
    }
}
