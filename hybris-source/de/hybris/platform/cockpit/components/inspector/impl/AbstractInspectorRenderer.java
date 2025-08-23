package de.hybris.platform.cockpit.components.inspector.impl;

import de.hybris.platform.cockpit.components.inspector.InspectorRenderer;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ValueService;
import de.hybris.platform.cockpit.util.UITools;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;

public abstract class AbstractInspectorRenderer implements InspectorRenderer
{
    public static final String INSPECTOR_TOOLBAR_BUTTON = "inspectorToolbarButton";
    public static final String INFO_TOOLBAR_ACTION = "infoToolbarAction";
    private ValueService valueService;


    protected void prepareEditActionButton(Component parent, TypedObject item)
    {
        Div editBtnCnt = new Div();
        editBtnCnt.setSclass("inspectorToolbarButton roundedCorners");
        UITools.applyTestID((Component)editBtnCnt, "inspectorToolbarEditButton");
        Image editButton = new Image("/cockpit/images/item_edit_action.png");
        editButton.setSclass("infoToolbarAction");
        editBtnCnt.setTooltiptext(Labels.getLabel("cockpit.inspector.edit"));
        editBtnCnt.addEventListener("onClick", (EventListener)new Object(this, item, editBtnCnt));
        editBtnCnt.addEventListener("onCloseInspectorLater", (EventListener)new Object(this, item));
        editBtnCnt.appendChild((Component)editButton);
        parent.appendChild((Component)editBtnCnt);
    }


    public ValueService getValueService()
    {
        return this.valueService;
    }


    @Required
    public void setValueService(ValueService valueService)
    {
        this.valueService = valueService;
    }
}
