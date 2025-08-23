package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.impl.DefaultActionCellRenderer;
import de.hybris.platform.cockpit.model.listview.TableModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;

public class CmsComponentStatusActionCellRenderer extends DefaultActionCellRenderer
{
    public void render(TableModel model, int colIndex, int rowIndex, Component parent)
    {
        Div wrapper = new Div();
        parent.appendChild((Component)wrapper);
        wrapper.setSclass("componentStatusActions");
        super.render(model, colIndex, rowIndex, (Component)wrapper);
    }
}
