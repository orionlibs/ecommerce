package de.hybris.platform.cockpit.components.duallistbox;

import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Table;
import org.zkoss.zhtml.Td;
import org.zkoss.zhtml.Tr;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class AssignedCollectionListitemRenderer implements ListitemRenderer
{
    public EventListener listener;


    public void render(Listitem item, Object data) throws Exception
    {
        if(data == null || (data instanceof Collection && ((Collection)data).isEmpty()))
        {
            return;
        }
        Listcell cellItem = new Listcell();
        Label listItemLabel = new Label();
        listItemLabel.setSclass("listItemLabel");
        String labelText = "";
        if(data instanceof ColumnDescriptor)
        {
            labelText = ((ColumnDescriptor)data).getName();
        }
        else if(data instanceof TypedObject)
        {
            labelText = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject((TypedObject)data);
        }
        listItemLabel.setValue(StringUtils.isBlank(labelText) ? Labels.getLabel("duallistbox.label.empty") : labelText);
        listItemLabel.setTooltiptext(labelText);
        Div buttonDiv = new Div();
        buttonDiv.setAlign("right");
        buttonDiv.appendChild((Component)createRemoveBtn());
        Table cellContainer = new Table();
        cellContainer.setSclass("dualcomponent_listitem_table");
        Tr cellRow = new Tr();
        cellRow.setParent((Component)cellContainer);
        Td containerLabelCell = new Td();
        containerLabelCell.setParent((Component)cellRow);
        containerLabelCell.appendChild((Component)listItemLabel);
        Td containerRemoveCell = new Td();
        containerRemoveCell.setParent((Component)cellRow);
        containerRemoveCell.appendChild((Component)buttonDiv);
        cellItem.appendChild((Component)cellContainer);
        item.appendChild((Component)cellItem);
    }


    private Image createRemoveBtn()
    {
        Image removeImage = new Image("/cockpit/images/remove.png");
        removeImage.setTooltiptext(Labels.getLabel("referenceselector.button.remove.tooltip"));
        removeImage.setClass("dualrow_remove_btn");
        removeImage.addEventListener("onClick", getListener());
        return removeImage;
    }


    public EventListener getListener()
    {
        return this.listener;
    }


    protected void setListener(EventListener listener)
    {
        this.listener = listener;
    }
}
