package de.hybris.platform.cmscockpit.components.contentbrowser;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractMainAreaBrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.MainAreaComponentFactory;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.SectionBrowserModel;
import org.zkoss.util.resource.Labels;

public class CmsPageMainAreaEditComponentFactory implements MainAreaComponentFactory
{
    public static final String VIEW_MODE_ID = "EDIT";


    public AbstractMainAreaBrowserComponent createInstance(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        Object object = null;
        if(model instanceof SectionBrowserModel)
        {
            object = new Object(this, (SectionBrowserModel)model, contentBrowser);
        }
        return (AbstractMainAreaBrowserComponent)object;
    }


    public String getActiveButtonImage()
    {
        return "/cmscockpit/images/button_view_layout_available_a.png";
    }


    public String getButtonLabel()
    {
        return null;
    }


    public String getButtonTooltip()
    {
        return Labels.getLabel("browser.page.edit");
    }


    public String getInactiveButtonImage()
    {
        return "/cmscockpit/images/button_view_layout_available_i.png";
    }


    public String getViewModeID()
    {
        return "EDIT";
    }
}
