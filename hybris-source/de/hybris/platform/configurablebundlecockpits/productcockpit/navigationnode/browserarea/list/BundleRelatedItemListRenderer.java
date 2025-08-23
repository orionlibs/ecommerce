package de.hybris.platform.configurablebundlecockpits.productcockpit.navigationnode.browserarea.list;

import de.hybris.platform.cockpit.components.mvc.listbox.listeners.DeleteListener;
import de.hybris.platform.cockpit.components.mvc.listbox.view.DefaultNodeWithActionsRenderer;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import org.apache.commons.lang.StringUtils;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Toolbarbutton;

public class BundleRelatedItemListRenderer extends DefaultNodeWithActionsRenderer
{
    protected static final String EDIT_PRODUCT_TOOLTIP = "configurablebundlecockpits.product.edit";
    protected static final String REMOVE_PRODUCT_TOOLTIP = "configurablebundlecockpits.product.remove";
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
            Toolbarbutton itemIcon = new Toolbarbutton("", UITools.getAdjustedUrl(personalizedIconUrl));
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
        labelCell.addEventListener("onClick", (EventListener)new Object(this, data));
    }


    protected void addActions(Listcell actionsCell)
    {
        appendEditButton(actionsCell);
        appendDeleteButton(actionsCell);
    }


    protected void appendEditButton(Listcell actionsCell)
    {
        Toolbarbutton editButton = new Toolbarbutton("", "/cockpit/images/item_edit_action.png");
        editButton.setTooltiptext(Labels.getLabel("configurablebundlecockpits.product.edit"));
        editButton.addEventListener("onClick", event -> {
            TypedObject value = (TypedObject)((Listitem)actionsCell.getParent()).getValue();
            UISessionUtils.getCurrentSession().getCurrentPerspective().activateItemInEditor(value);
        });
        actionsCell.appendChild((Component)editButton);
    }


    protected void appendDeleteButton(Listcell actionsCell)
    {
        Toolbarbutton deleteButton = new Toolbarbutton("", "/productcockpit/images/cnt_elem_remove_action.png");
        deleteButton.setTooltiptext(Labels.getLabel("general.remove"));
        deleteButton.addEventListener("onClick", (EventListener)new DeleteListener());
        actionsCell.appendChild((Component)deleteButton);
    }


    protected BundleRelatedItemListController getRelatedItemListController()
    {
        return (BundleRelatedItemListController)SpringUtil.getBean("bundleRelatedItemListController", BundleRelatedItemListController.class);
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
