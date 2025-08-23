package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.session.AdvancedBrowserModel;

public class DummyMainAreaComponentFactory implements MainAreaComponentFactory
{
    public AbstractMainAreaBrowserComponent createInstance(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        return (AbstractMainAreaBrowserComponent)new Object(this, model, contentBrowser);
    }


    public String getActiveButtonImage()
    {
        return "/cockpit/images/grid_btn.png";
    }


    public String getButtonLabel()
    {
        return "Dummy";
    }


    public String getButtonTooltip()
    {
        return null;
    }


    public String getInactiveButtonImage()
    {
        return null;
    }


    public String getViewModeID()
    {
        return "DUMMY";
    }
}
