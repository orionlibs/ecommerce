package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.model.gridview.GridItemRenderer;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import org.zkoss.util.resource.Labels;

public class GridMainAreaComponentFactory implements MainAreaComponentFactory
{
    protected static final String GRID_VIEW_BTN_IMG_INACTIVE = "/cockpit/images/button_view_grid_available_i.png";
    protected static final String GRID_VIEW_BTN_IMG_ACTIVE = "/cockpit/images/button_view_grid_available_a.png";
    private transient GridItemRenderer gridItemRenderer = null;


    public GridMainAreaComponentFactory()
    {
    }


    public GridMainAreaComponentFactory(GridItemRenderer gridItemRenderer)
    {
        this.gridItemRenderer = gridItemRenderer;
    }


    public AbstractMainAreaBrowserComponent createInstance(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        MainAreaGridviewBrowserComponent ret = new MainAreaGridviewBrowserComponent(model, contentBrowser);
        ret.setGridItemRenderer(this.gridItemRenderer);
        return (AbstractMainAreaBrowserComponent)ret;
    }


    public String getActiveButtonImage()
    {
        return "/cockpit/images/button_view_grid_available_a.png";
    }


    public String getButtonTooltip()
    {
        return Labels.getLabel("browserarea.gridview.button.tooltip");
    }


    public String getInactiveButtonImage()
    {
        return "/cockpit/images/button_view_grid_available_i.png";
    }


    public String getViewModeID()
    {
        return "GRID";
    }


    public String getButtonLabel()
    {
        return null;
    }
}
