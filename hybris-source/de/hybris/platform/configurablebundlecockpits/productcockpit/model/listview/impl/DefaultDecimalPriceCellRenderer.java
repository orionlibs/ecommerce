package de.hybris.platform.configurablebundlecockpits.productcockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.math.BigDecimal;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class DefaultDecimalPriceCellRenderer implements CellRenderer
{
    private static final Logger LOGGER = Logger.getLogger(DefaultDecimalPriceCellRenderer.class);


    public void render(TableModel model, int colIndex, int rowIndex, Component parent)
    {
        if(model == null || parent == null)
        {
            throw new IllegalArgumentException("Model and parent can not be null.");
        }
        String strValue = "";
        Object value = null;
        try
        {
            value = model.getValueAt(colIndex, rowIndex);
        }
        catch(IllegalArgumentException iae)
        {
            LOGGER.warn("Could not render cell (Reason: '" + iae.getMessage() + "').", iae);
        }
        Div div = new Div();
        div.setStyle("overflow: hidden;height: 100%;");
        if(ValueHandler.NOT_READABLE_VALUE.equals(value))
        {
            div.setSclass("listview_notreadable_cell");
            strValue = Labels.getLabel("listview.cell.readprotected");
        }
        else if(strValue.isEmpty())
        {
            BigDecimal price = (BigDecimal)value;
            ServicesUtil.validateParameterNotNullStandardMessage("price", price);
            if(BigDecimal.ZERO.compareTo(price) == 0)
            {
                strValue = BigDecimal.ZERO.toString();
            }
            else
            {
                strValue = String.valueOf(price);
            }
        }
        Label label = new Label(strValue);
        div.appendChild((Component)label);
        parent.appendChild((Component)div);
    }
}
