package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.components.contentbrowser.browsercomponents.TaskMainAreaBrowserComponent;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import org.zkoss.util.resource.Labels;

public class TaskMainAreaComponentFactory implements MainAreaComponentFactory
{
    protected static final String TASK_VIEW_BTN_IMG = "/cockpit/images/message.gif";


    public AbstractMainAreaBrowserComponent createInstance(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        return (AbstractMainAreaBrowserComponent)new TaskMainAreaBrowserComponent(model, contentBrowser);
    }


    public String getActiveButtonImage()
    {
        return "/cockpit/images/message.gif";
    }


    public String getButtonTooltip()
    {
        return Labels.getLabel("browserarea.task.button.tooltip");
    }


    public String getInactiveButtonImage()
    {
        return "/cockpit/images/message.gif";
    }


    public String getViewModeID()
    {
        return "TASK";
    }


    public String getButtonLabel()
    {
        return null;
    }
}
