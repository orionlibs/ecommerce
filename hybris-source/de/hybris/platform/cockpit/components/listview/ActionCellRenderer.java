package de.hybris.platform.cockpit.components.listview;

import de.hybris.platform.cockpit.model.listview.CellRenderer;
import java.util.List;

public interface ActionCellRenderer extends CellRenderer
{
    void setActions(List<ListViewAction> paramList);


    List<ListViewAction> getActions();
}
