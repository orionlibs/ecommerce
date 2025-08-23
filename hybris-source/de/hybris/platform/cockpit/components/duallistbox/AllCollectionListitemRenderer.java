package de.hybris.platform.cockpit.components.duallistbox;

import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Collection;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Table;
import org.zkoss.zhtml.Td;
import org.zkoss.zhtml.Tr;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class AllCollectionListitemRenderer implements ListitemRenderer
{
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
        if(data instanceof TypedObject)
        {
            labelText = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject((TypedObject)data);
        }
        listItemLabel.setValue(labelText.isEmpty() ? Labels.getLabel("duallistbox.label.empty") : labelText);
        listItemLabel.setTooltiptext(labelText);
        Table cellContainer = new Table();
        cellContainer.setSclass("tableRowItem");
        Tr cellRow = new Tr();
        cellRow.setParent((Component)cellContainer);
        Td containerThrCell = new Td();
        containerThrCell.setParent((Component)cellRow);
        containerThrCell.appendChild((Component)listItemLabel);
        cellItem.appendChild((Component)cellContainer);
        item.appendChild((Component)cellItem);
        item.setDraggable("availableListItem");
    }
}
