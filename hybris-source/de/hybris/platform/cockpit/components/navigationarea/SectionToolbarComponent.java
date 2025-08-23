package de.hybris.platform.cockpit.components.navigationarea;

import de.hybris.platform.cockpit.util.UITools;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Toolbarbutton;

public class SectionToolbarComponent extends Div
{
    private final AbstractNavigationAreaModel abstractNavigationAreaModel;
    private final Div buttons = new Div();


    public SectionToolbarComponent(AbstractNavigationAreaModel abstractNavigationAreaModel)
    {
        this.abstractNavigationAreaModel = abstractNavigationAreaModel;
        setSclass("section-toolbar");
        this.buttons.setParent((Component)this);
        this.buttons.setClass("toolbar-buttons");
    }


    public final void addButton(Toolbarbutton toolbarbutton)
    {
        this.buttons.appendChild((Component)toolbarbutton);
    }


    public void createButton(QueryTypeSectionModel model)
    {
        Toolbarbutton button = new Toolbarbutton(null, this.abstractNavigationAreaModel.getSelectedQueryType().getQueryTypeId().equals(model.getQueryTypeId()) ? model.getActiveButtonUrl() : model.getInactiveButtonUrl());
        UITools.addBusyListener((Component)button, "onClick", (EventListener)new Object(this, model), null, null);
        button.setTooltiptext(model.getButtonTooltip());
        addButton(button);
    }
}
