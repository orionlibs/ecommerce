package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.session.BrowserModel;

public class DefaultMultiViewToolbarComponent extends AbstractMultiViewToolbarBrowserComponent
{
    public DefaultMultiViewToolbarComponent(BrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super(model, contentBrowser);
    }


    public boolean update()
    {
        boolean success = super.update();
        updateViewModeButtons();
        return success;
    }
}
