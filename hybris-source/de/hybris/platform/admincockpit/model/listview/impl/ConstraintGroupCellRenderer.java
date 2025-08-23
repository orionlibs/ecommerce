package de.hybris.platform.admincockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.listview.impl.DefaultTextCellRenderer;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class ConstraintGroupCellRenderer extends DefaultTextCellRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(ConstraintGroupCellRenderer.class);


    public void render(TableModel model, int colIndex, int rowIndex, Component parent)
    {
        if(model == null || parent == null)
        {
            throw new IllegalArgumentException("Model and parent can not be null.");
        }
        String text = "";
        Object value = null;
        try
        {
            value = model.getValueAt(colIndex, rowIndex);
        }
        catch(IllegalArgumentException iae)
        {
            LOG.warn("Could not render cell (Reason: '" + iae.getMessage() + "').", iae);
        }
        Div div = new Div();
        div.setStyle("overflow: hidden;height: 100%;");
        if(ValueHandler.NOT_READABLE_VALUE.equals(value))
        {
            div.setSclass("listview_notreadable_cell");
            text = Labels.getLabel("listview.cell.readprotected");
        }
        else if(value != null && value instanceof Collection && !((Collection)value).isEmpty())
        {
            LabelService labelService = UISessionUtils.getCurrentSession().getLabelService();
            text = TypeTools.getValueAsString(labelService, value);
        }
        else
        {
            text = Labels.getLabel("ba.constrain_group_default_label");
            div.setTooltiptext(Labels.getLabel("ba.constrain_group_default_label_desc"));
        }
        Label label = new Label(text);
        div.appendChild((Component)label);
        parent.appendChild((Component)div);
    }
}
