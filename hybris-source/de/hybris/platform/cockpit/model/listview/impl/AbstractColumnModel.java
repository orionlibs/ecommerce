package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.listview.ColumnModelListener;
import de.hybris.platform.cockpit.model.listview.MutableColumnModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractColumnModel implements MutableColumnModel
{
    protected List<ColumnModelListener> listeners = new ArrayList<>();


    public void addColumnModelListener(ColumnModelListener listener)
    {
        if(!this.listeners.contains(listener))
        {
            this.listeners.add(listener);
        }
    }


    public void removeColumnModelListener(ColumnModelListener listener)
    {
        this.listeners.remove(listener);
    }


    protected void fireChanged()
    {
        List<ColumnModelListener> cmListeners = new LinkedList<>(this.listeners);
        for(ColumnModelListener listener : cmListeners)
        {
            listener.changed();
        }
    }


    protected void fireColumnMoved(int fromIndex, int toIndex)
    {
        List<ColumnModelListener> cmListeners = new LinkedList<>(this.listeners);
        for(ColumnModelListener listener : cmListeners)
        {
            listener.columnMoved(fromIndex, toIndex);
        }
    }


    protected void fireColumnVisibilityChanged()
    {
        List<ColumnModelListener> cmListeners = new LinkedList<>(this.listeners);
        for(ColumnModelListener listener : cmListeners)
        {
            listener.columnVisibilityChanged();
        }
    }


    protected void fireColumnVisibilityChanged(Integer colIndex)
    {
        List<ColumnModelListener> cmListeners = new LinkedList<>(this.listeners);
        for(ColumnModelListener listener : cmListeners)
        {
            listener.columnVisibilityChanged(colIndex);
        }
    }


    protected void fireColumnSelectionChanged(List colIndexes)
    {
        List<ColumnModelListener> cmListeners = new LinkedList<>(this.listeners);
        for(ColumnModelListener listener : cmListeners)
        {
            listener.selectionChanged(colIndexes);
        }
    }


    protected void fireSortChanged(int columnIndex, boolean ascending)
    {
        List<ColumnModelListener> cmListeners = new LinkedList<>(this.listeners);
        for(ColumnModelListener listener : cmListeners)
        {
            listener.sortChanged(columnIndex, ascending);
        }
    }
}
