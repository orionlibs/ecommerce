package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.session.BrowserModel;

public abstract class AbstractContextBrowserComponent extends AbstractBrowserComponent
{
    public AbstractContextBrowserComponent(BrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super(model, contentBrowser);
    }


    public abstract boolean update(boolean paramBoolean);
}
