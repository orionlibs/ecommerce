package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import org.apache.commons.lang.BooleanUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class InlineItemRenderer implements CellRenderer
{
    private static final String MANDATORY_FIELD_SIGN = " *";
    private Button button;


    public Button getCreateItemButton()
    {
        return this.button;
    }


    public void render(TableModel model, int colIndex, int rowIndex, Component parent)
    {
        if(model == null || parent == null)
        {
            throw new IllegalArgumentException("Model and parent can not be null");
        }
        ColumnDescriptor columnDescriptor = model.getColumnComponentModel().getVisibleColumns().get(colIndex);
        Div div = new Div();
        div.setStyle("height: 100%;");
        if(columnDescriptor instanceof InlineItemColumnDescriptor)
        {
            if(!BooleanUtils.toBoolean(UITools.getCockpitParameter("default.contextarea.list.hideNewItemButton",
                            Executions.getCurrent())))
            {
                this.button = new Button(Labels.getLabel("contextarea.button.createnewitem"));
                this.button.setSclass("create_new_line_button");
                this.button.setDisabled(false);
                div.appendChild((Component)this.button);
                div.setTooltiptext(Labels.getLabel("contextarea.button.createnewitem.tooltip"));
                if(UISessionUtils.getCurrentSession().isUsingTestIDs())
                {
                    String id = "Context_Area_Create_new_item_button";
                    UITools.applyTestID((Component)this.button, "Context_Area_Create_new_item_button");
                }
            }
        }
        else
        {
            Label label = new Label(getLabelValue(model, colIndex, rowIndex));
            label.setStyle("color: #777777 !important; font-style: italic;");
            div.appendChild((Component)label);
        }
        parent.appendChild((Component)div);
    }


    private String getLabelValue(TableModel model, int colIndex, int rowIndex)
    {
        ColumnDescriptor columnDescriptor = model.getColumnComponentModel().getColumn(colIndex);
        String value = "";
        if(model.getValueAt(colIndex, rowIndex) != null)
        {
            value = TypeTools.getValueAsString(UISessionUtils.getCurrentSession().getLabelService(), model
                            .getValueAt(colIndex, rowIndex));
        }
        else if(columnDescriptor.isVisible())
        {
            value = columnDescriptor.getName();
            PropertyDescriptor propertyDescriptor = model.getColumnComponentModel().getPropertyDescriptor(columnDescriptor);
            if(propertyDescriptor != null && TypeTools.isMandatory(propertyDescriptor, true))
            {
                value = value.concat(" *");
            }
        }
        return value;
    }
}
