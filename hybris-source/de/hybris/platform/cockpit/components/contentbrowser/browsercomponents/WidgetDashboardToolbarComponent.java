package de.hybris.platform.cockpit.components.contentbrowser.browsercomponents;

import de.hybris.platform.cockpit.components.CreateNewComponent;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractBrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.widgets.browsers.WidgetDashboardBrowserModel;
import de.hybris.platform.cockpit.widgets.portal.ContainerLayout;
import java.util.Set;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class WidgetDashboardToolbarComponent extends AbstractBrowserComponent
{
    protected Component rightToolbarCmp;
    protected Component leftToolbarCmp;


    public WidgetDashboardToolbarComponent(BrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super(model, contentBrowser);
    }


    public boolean update()
    {
        return initialize();
    }


    public boolean initialize()
    {
        this.initialized = false;
        if(getModel() != null)
        {
            setSclass("dashboard_toolbar_component");
            getChildren().clear();
            appendChild((Component)createToolbar());
            this.initialized = true;
        }
        return this.initialized;
    }


    protected HtmlBasedComponent createToolbar()
    {
        Div toolbar = new Div();
        toolbar.setHeight("2.1em");
        toolbar.setSclass("query-browser-paging");
        Hbox browserToolbarHbox = new Hbox();
        toolbar.appendChild((Component)browserToolbarHbox);
        browserToolbarHbox.setWidth("100%");
        browserToolbarHbox.setAlign("center");
        browserToolbarHbox.appendChild(createLeftToolbarContent());
        browserToolbarHbox.appendChild(createRightToolbarContent());
        return (HtmlBasedComponent)toolbar;
    }


    protected Component createLeftToolbarContent()
    {
        Div leftDiv = new Div();
        leftDiv.setAlign("left");
        leftDiv.setWidth("100%");
        leftDiv.setStyle("margin-left: 10px; white-space: nowrap;");
        Toolbarbutton button = new Toolbarbutton("", "/cockpit/images/button_view_layout_available_i.png");
        button.setSclass("browser_view_mode_switch");
        button.addEventListener("onClick", (EventListener)new Object(this));
        Image splitterDummy = new Image("cockpit/images/splitter_grey_space.gif");
        leftDiv.appendChild((Component)button);
        leftDiv.appendChild((Component)splitterDummy);
        Div createNew = new Div();
        CreateNewComponent addElementButton = new CreateNewComponent();
        addElementButton.setTooltiptext(Labels.getLabel("cockpit.report.create_item"));
        UITools.addBusyListener((Component)addElementButton, "onClick", (EventListener)new Object(this), null, "general.updating.busy");
        createNew.appendChild((Component)addElementButton);
        createNew.setClass("toolbar_newitem_action");
        leftDiv.appendChild((Component)createNew);
        return (Component)leftDiv;
    }


    protected void openLayoutChooser()
    {
        Window chooserWindow = new Window();
        chooserWindow.setClosable(true);
        chooserWindow.setTitle(Labels.getLabel("browserarea.dashboard.layoutchooser.label"));
        chooserWindow.setSclass("layoutChooserWindow");
        Label label = new Label(Labels.getLabel("browserarea.dashboard.layoutchooser.description"));
        label.setSclass("layoutChooserDescription");
        chooserWindow.appendChild((Component)label);
        Div layoutsDiv = new Div();
        layoutsDiv.setSclass("layoutsCnt");
        if(getModel() instanceof WidgetDashboardBrowserModel)
        {
            WidgetDashboardBrowserModel wiModel = (WidgetDashboardBrowserModel)getModel();
            for(ContainerLayout cntLayout : wiModel.getContainerLayouts())
            {
                Div layoutBox = new Div();
                layoutBox.setSclass("dashboardLayoutBox");
                layoutBox.setTooltiptext(Labels.getLabel(cntLayout.getLabelI3Key()));
                Image img = new Image(cntLayout.getPreviewURL());
                layoutBox.appendChild((Component)img);
                layoutsDiv.appendChild((Component)layoutBox);
                if(cntLayout.equals(wiModel.getCurrentContainerLayout()))
                {
                    UITools.modifySClass((HtmlBasedComponent)layoutBox, "layoutSelected", true);
                    continue;
                }
                layoutBox.addEventListener("onClick", (EventListener)new Object(this, wiModel, cntLayout, chooserWindow));
            }
        }
        chooserWindow.appendChild((Component)layoutsDiv);
        appendChild((Component)chooserWindow);
        chooserWindow.doHighlighted();
    }


    protected Component createRightToolbarContent()
    {
        return (Component)new Div();
    }


    public void resize()
    {
    }


    public void setActiveItem(TypedObject activeItem)
    {
    }


    public void updateActiveItems()
    {
    }


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties)
    {
    }


    public void updateSelectedItems()
    {
    }
}
