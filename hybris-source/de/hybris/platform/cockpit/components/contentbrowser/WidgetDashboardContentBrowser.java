package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.components.AdvancedPortallayout;
import de.hybris.platform.cockpit.components.contentbrowser.browsercomponents.WidgetDashboardToolbarComponent;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.WidgetConfig;
import de.hybris.platform.cockpit.widgets.WidgetFactory;
import de.hybris.platform.cockpit.widgets.browsers.WidgetDashboardBrowserModel;
import de.hybris.platform.cockpit.widgets.portal.ContainerLayout;
import de.hybris.platform.cockpit.widgets.portal.PortalWidgetContainer;
import de.hybris.platform.cockpit.widgets.portal.PortalWidgetContainerListener;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;

public class WidgetDashboardContentBrowser extends AbstractSimpleContentBrowser
{
    private BrowserComponent captionComponent;
    private BrowserComponent toolbarComponent;


    protected boolean initialize()
    {
        setRealSclass("dashboardView");
        this.captionComponent = (BrowserComponent)createCaptionComponent();
        if(this.captionComponent != null)
        {
            appendChild((Component)this.captionComponent);
            this.captionComponent.initialize();
        }
        this.toolbarComponent = (BrowserComponent)createToolbarComponent();
        if(this.toolbarComponent != null)
        {
            appendChild((Component)this.toolbarComponent);
            this.toolbarComponent.initialize();
        }
        renderMainFrame();
        this.initialized = true;
        return this.initialized;
    }


    protected AbstractBrowserComponent createToolbarComponent()
    {
        return (AbstractBrowserComponent)new WidgetDashboardToolbarComponent(getModel(), (AbstractContentBrowser)this);
    }


    protected AbstractBrowserComponent createCaptionComponent()
    {
        return null;
    }


    public void updateToolbar()
    {
        if(this.toolbarComponent != null && !UITools.isFromOtherDesktop((Component)this.toolbarComponent))
        {
            this.toolbarComponent.update();
        }
    }


    public void updateCaption()
    {
        if(this.captionComponent != null && !UITools.isFromOtherDesktop((Component)this.captionComponent))
        {
            this.captionComponent.update();
        }
    }


    protected void renderMainFrame()
    {
        if(getModel() instanceof WidgetDashboardBrowserModel)
        {
            WidgetDashboardBrowserModel browserModel = (WidgetDashboardBrowserModel)getModel();
            Map<String, WidgetConfig> cockpitWidgetMap = browserModel.getWidgetMap();
            if(!cockpitWidgetMap.isEmpty())
            {
                WidgetFactory cockpitWidgetFactory = browserModel.getWidgetFactory();
                PortalWidgetContainer<Widget> portalWidgetContainer = new PortalWidgetContainer(cockpitWidgetFactory);
                portalWidgetContainer.initialize(cockpitWidgetMap);
                portalWidgetContainer.setWidgetPositions(((WidgetDashboardBrowserModel)getModel()).getWidgetPositions());
                ContainerLayout containerLayout = null;
                if(getModel() instanceof WidgetDashboardBrowserModel)
                {
                    containerLayout = ((WidgetDashboardBrowserModel)getModel()).getCurrentContainerLayout();
                }
                AdvancedPortallayout portal = portalWidgetContainer.createPortalLayout(containerLayout, (PortalWidgetContainerListener)new Object(this, portalWidgetContainer));
                Div container = new Div();
                container.setSclass("portalContainer");
                container.appendChild((Component)portal);
                appendChild((Component)container);
            }
        }
    }
}
