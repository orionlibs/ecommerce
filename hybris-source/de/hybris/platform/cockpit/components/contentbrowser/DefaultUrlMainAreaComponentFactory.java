package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultUrlMainAreaComponentFactory extends AbstractUrlMainAreaComponentFactory
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultUrlMainAreaComponentFactory.class);
    protected static final String GRID_VIEW_BTN_IMG_INACTIVE = "/cockpit/images/button_view_grid_available_i.png";
    protected static final String GRID_VIEW_BTN_IMG_ACTIVE = "/cockpit/images/button_view_grid_available_a.png";


    public AbstractMainAreaBrowserComponent createInstance(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        return (AbstractMainAreaBrowserComponent)new UrlMainAreaBrowserComponent(this, model, contentBrowser);
    }


    public String getActiveButtonImage()
    {
        return "/cockpit/images/button_view_grid_available_a.png";
    }


    public String getButtonTooltip()
    {
        return null;
    }


    public String getInactiveButtonImage()
    {
        return "/cockpit/images/button_view_grid_available_i.png";
    }
}
