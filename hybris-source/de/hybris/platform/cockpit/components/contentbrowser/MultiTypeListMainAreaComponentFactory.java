package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import org.zkoss.util.resource.Labels;

public class MultiTypeListMainAreaComponentFactory implements MainAreaComponentFactory
{
    public static final String MULTI_TYPE_LIST = "MULTI_TYPE_LIST";
    protected static final String LIST_VIEW_BTN_IMG_INACTIVE = "/cockpit/images/button_view_list_available_i.png";
    protected static final String LIST_VIEW_BTN_IMG_ACTIVE = "/cockpit/images/button_view_list_available_a.png";


    public AbstractMainAreaBrowserComponent createInstance(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        return (AbstractMainAreaBrowserComponent)new Object(this, model, contentBrowser);
    }


    public String getActiveButtonImage()
    {
        return "/cockpit/images/button_view_list_available_a.png";
    }


    public String getButtonTooltip()
    {
        return Labels.getLabel("browserarea.listview.button.tooltip");
    }


    public String getInactiveButtonImage()
    {
        return "/cockpit/images/button_view_list_available_i.png";
    }


    public String getViewModeID()
    {
        return "MULTI_TYPE_LIST";
    }


    public String getButtonLabel()
    {
        return null;
    }
}
