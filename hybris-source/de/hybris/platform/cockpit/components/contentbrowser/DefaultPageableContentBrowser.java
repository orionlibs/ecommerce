package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.PageableBrowserModel;

public class DefaultPageableContentBrowser extends DefaultAdvancedContentBrowser
{
    public PageableBrowserModel getModel()
    {
        return (PageableBrowserModel)super.getModel();
    }


    public void setModel(BrowserModel model)
    {
        if(model instanceof PageableBrowserModel)
        {
            super.setModel(model);
        }
        else
        {
            throw new IllegalArgumentException("Model not of type 'PageableBrowserModel'.");
        }
    }


    protected AbstractBrowserComponent createToolbarComponent()
    {
        return (AbstractBrowserComponent)new PagerToolbarBrowserComponent(getModel(), (AbstractContentBrowser)this);
    }
}
