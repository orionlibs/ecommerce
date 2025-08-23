package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.SectionBrowserModel;
import de.hybris.platform.cockpit.session.SectionSearchBrowserModel;

public class DefaultSectionSearchContentBrowser extends DefaultSearchContentBrowser
{
    public SectionSearchBrowserModel getModel()
    {
        return (SectionSearchBrowserModel)super.getModel();
    }


    public void setModel(BrowserModel model)
    {
        if(model instanceof SectionBrowserModel)
        {
            super.setModel(model);
        }
        else
        {
            throw new IllegalArgumentException("Browser model must be a section search browser model.");
        }
    }


    protected AbstractBrowserComponent createMainAreaComponent()
    {
        return (AbstractBrowserComponent)new DefaultSectionMainAreaBrowserComponent((SectionBrowserModel)getModel(), (AbstractContentBrowser)this);
    }
}
