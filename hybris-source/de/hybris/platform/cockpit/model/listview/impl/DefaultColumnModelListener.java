package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.listview.ColumnModelListener;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.UIListView;
import java.util.Collections;
import java.util.List;

public class DefaultColumnModelListener implements ColumnModelListener
{
    private final UIListView view;
    private final MutableTableModel model;


    public DefaultColumnModelListener(MutableTableModel model, UIListView view)
    {
        this.model = model;
        this.view = view;
    }


    public void changed()
    {
        this.view.update();
    }


    public void columnMoved(int fromIndex, int toIndex)
    {
        this.view.updateVisibleColumns();
    }


    public void columnVisibilityChanged()
    {
        this.view.updateColumns();
    }


    public void columnVisibilityChanged(Integer colIndex)
    {
        this.view.updateColumns(colIndex);
    }


    public void selectionChanged(List<? extends Object> columnIndexes)
    {
        if(columnIndexes == null || columnIndexes.isEmpty())
        {
            this.model.getListComponentModel().setSelectedIndexesDirectly(Collections.EMPTY_LIST);
        }
        this.view.updateSelection();
    }


    public void sortChanged(int columnIndex, boolean ascending)
    {
        this.view.updateVisibleColumns();
    }
}
