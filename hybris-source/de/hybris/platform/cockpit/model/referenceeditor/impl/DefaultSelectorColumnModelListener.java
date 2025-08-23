package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.listview.ColumnModelListener;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.referenceeditor.AbstractReferenceSelectorModel;
import java.util.Collections;
import java.util.List;

public class DefaultSelectorColumnModelListener implements ColumnModelListener
{
    private final AbstractReferenceSelectorModel referenceSelectorModel;
    private final UIListView view;


    public DefaultSelectorColumnModelListener(AbstractReferenceSelectorModel referenceSelectorModel, UIListView view)
    {
        this.referenceSelectorModel = referenceSelectorModel;
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
            this.referenceSelectorModel.getTableModel().getListComponentModel().setSelectedIndexesDirectly(Collections.EMPTY_LIST);
        }
        this.view.updateSelection();
    }


    public void sortChanged(int columnIndex, boolean ascending)
    {
        this.view.updateVisibleColumns();
    }
}
