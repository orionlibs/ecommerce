package de.hybris.platform.cockpit.reports.components.contentbrowser;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractBrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Set;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;

public class ReportDashboardCaptionComponent extends AbstractBrowserComponent
{
    protected Component rightCaptionCmp;
    protected Component leftCaptionCmp;


    public ReportDashboardCaptionComponent(BrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super(model, contentBrowser);
    }


    public boolean initialize()
    {
        Hbox toolbarHbox = new Hbox();
        toolbarHbox.setWidth("100%");
        appendChild((Component)toolbarHbox);
        setSclass("dashboard_caption_container");
        this.leftCaptionCmp = (Component)new Div();
        toolbarHbox.appendChild(this.leftCaptionCmp);
        this.rightCaptionCmp = createRightCaptionComponent();
        toolbarHbox.appendChild(this.rightCaptionCmp);
        return false;
    }


    protected Component createRightCaptionComponent()
    {
        Div btnContainer = new Div();
        btnContainer.setSclass("rightCnt");
        Button addElementButton = new Button(Labels.getLabel("cockpit.report.create_item"));
        addElementButton.setSclass("btnblue createReportWidgetBtn");
        UITools.addBusyListener((Component)addElementButton, "onClick", (EventListener)new Object(this), null, "general.updating.busy");
        btnContainer.appendChild((Component)addElementButton);
        return (Component)btnContainer;
    }


    public void resize()
    {
    }


    public void setActiveItem(TypedObject activeItem)
    {
    }


    public boolean update()
    {
        return false;
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
