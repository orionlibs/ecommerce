package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.TableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;

public class BooleanCellRenderer implements CellRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(BooleanCellRenderer.class);
    protected static final String BOOLEAN_TRUE_IMG = "/cockpit/images/bool_true.gif";
    protected static final String BOOLEAN_FALSE_IMG = "/cockpit/images/bool_false.gif";
    protected static final String BOOLEAN_NULL_IMG = "/cockpit/images/bool_null.gif";


    public void render(TableModel model, int colIndex, int rowIndex, Component parent)
    {
        if(model == null || parent == null)
        {
            throw new IllegalArgumentException("Model and parent can not be null.");
        }
        try
        {
            Object value = model.getValueAt(colIndex, rowIndex);
            Div div = new Div();
            div.setParent(parent);
            div.setStyle("overflow: hidden;height:100%");
            String imgSrc = "";
            if(value == null)
            {
                imgSrc = "/cockpit/images/bool_null.gif";
            }
            else if(Boolean.TRUE.equals(value))
            {
                imgSrc = "/cockpit/images/bool_true.gif";
            }
            else
            {
                imgSrc = "/cockpit/images/bool_false.gif";
            }
            Image img = new Image(imgSrc);
            img.setParent((Component)div);
        }
        catch(IllegalArgumentException iae)
        {
            LOG.warn("Could not render cell using " + BooleanCellRenderer.class.getSimpleName() + ".", iae);
            return;
        }
    }
}
