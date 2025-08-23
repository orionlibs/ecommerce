package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import org.zkoss.util.resource.Labels;

public class CommentMainAreaComponentFactory implements MainAreaComponentFactory
{
    protected static final String COMMENT_VIEW_BTN_IMG = "/cockpit/images/icon_func_comment_available.png";


    public AbstractMainAreaBrowserComponent createInstance(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        return (AbstractMainAreaBrowserComponent)new CommentMainAreaBrowserComponent(model, contentBrowser);
    }


    public String getActiveButtonImage()
    {
        return "/cockpit/images/icon_func_comment_available.png";
    }


    public String getButtonTooltip()
    {
        return Labels.getLabel("browserarea.comment.button.tooltip");
    }


    public String getInactiveButtonImage()
    {
        return "/cockpit/images/icon_func_comment_available.png";
    }


    public String getViewModeID()
    {
        return "COMMENT";
    }


    public String getButtonLabel()
    {
        return null;
    }
}
