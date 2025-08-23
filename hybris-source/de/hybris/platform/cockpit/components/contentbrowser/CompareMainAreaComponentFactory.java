package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.components.contentbrowser.browsercomponents.CompareMainAreaBrowserComponent;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.impl.AbstractAdvancedBrowserModel;
import org.zkoss.util.resource.Labels;

public class CompareMainAreaComponentFactory implements ContextAwareMainAreaComponentFactory
{
    public static final String VIEWMODE_ID = "compare";
    protected static final String COMPARE_VIEW_BTN_IMG_INACTIVE = "/cockpit/images/button_view_compare_available_i.png";
    protected static final String COMPARE_VIEW_BTN_IMG_ACTIVE = "/cockpit/images/button_view_compare_available_a.png";


    public AbstractMainAreaBrowserComponent createInstance(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        return (AbstractMainAreaBrowserComponent)new CompareMainAreaBrowserComponent((AbstractAdvancedBrowserModel)model, contentBrowser);
    }


    public String getActiveButtonImage()
    {
        return "/cockpit/images/button_view_compare_available_a.png";
    }


    public String getButtonLabel()
    {
        return null;
    }


    public String getButtonTooltip()
    {
        return Labels.getLabel("browser.viewmode.compare.tooltip");
    }


    public String getInactiveButtonImage()
    {
        return "/cockpit/images/button_view_compare_available_i.png";
    }


    public String getViewModeID()
    {
        return "compare";
    }


    public boolean isAvailable(BrowserModel browserModel)
    {
        return (browserModel.getSelectedIndexes().size() > 1);
    }


    public boolean hasOwnModel()
    {
        return true;
    }
}
