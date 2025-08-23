package de.hybris.platform.cmscockpit.navigationnode.browserarea.list;

import de.hybris.platform.cockpit.components.mvc.listbox.view.DefaultNodeWithActionsRenderer;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Toolbarbutton;

public class RelatedItemListRenderer extends DefaultNodeWithActionsRenderer
{
    protected static final String EDIT_NN_TOOLTIP = "cmscockpit.navigationnodes.edit";
    private TypeService typeService;
    private LabelService labelService;


    public Listitem renderListitem(Listitem item, Object data)
    {
        String labelText = getLabelService().getObjectTextLabelForTypedObject((TypedObject)data);
        String personalizedIconUrl = getLabelService().getObjectIconPathForTypedObject((TypedObject)data);
        Listcell labelCell = new Listcell();
        labelCell.setSclass("relatedItemLabel");
        if(StringUtils.isNotBlank(personalizedIconUrl))
        {
            Toolbarbutton itemIcon = new Toolbarbutton("", personalizedIconUrl);
            itemIcon.setParent((Component)labelCell);
        }
        Label textLabel = new Label(labelText);
        textLabel.setParent((Component)labelCell);
        Listcell actionsCell = new Listcell();
        actionsCell.setSclass("navigationNodesRelItemActions");
        addActions(actionsCell);
        item.setValue(data);
        labelCell.setParent((Component)item);
        actionsCell.setParent((Component)item);
        return item;
    }


    protected void createLabelActions(Listcell labelCell, TypedObject data)
    {
        labelCell.addEventListener("onDoubleClick", (EventListener)new Object(this, data));
    }


    protected void addActions(Listcell actionsCell)
    {
        Toolbarbutton editButton = new Toolbarbutton("", "/cockpit/images/item_edit_action.png");
        editButton.setTooltiptext(Labels.getLabel("cmscockpit.navigationnodes.edit"));
        editButton.addEventListener("onClick", (EventListener)new Object(this, actionsCell));
        actionsCell.appendChild((Component)editButton);
        super.addActions(actionsCell);
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    protected LabelService getLabelService()
    {
        if(this.labelService == null)
        {
            this.labelService = UISessionUtils.getCurrentSession().getLabelService();
        }
        return this.labelService;
    }
}
