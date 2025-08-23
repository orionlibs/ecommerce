package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.listview.TableModelListener;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractTableModel implements TableModel
{
    protected List<TableModelListener> listeners = new ArrayList<>();


    public void addTableModelListener(TableModelListener listener)
    {
        if(!this.listeners.contains(listener))
        {
            this.listeners.add(listener);
        }
    }


    public void removeTableModelListener(TableModelListener listener)
    {
        this.listeners.remove(listener);
    }


    protected void fireSelectionChanged(List<Integer> colIndexes, List<Integer> rowIndexes)
    {
        List<TableModelListener> tmListeners = new LinkedList<>(this.listeners);
        for(TableModelListener listener : tmListeners)
        {
            listener.selectionChanged(colIndexes, rowIndexes);
        }
    }


    protected void fireCellChanged(int colIndex, int rowIndex)
    {
        List<TableModelListener> tmListeners = new LinkedList<>(this.listeners);
        for(TableModelListener listener : tmListeners)
        {
            listener.cellChanged(colIndex, rowIndex);
        }
    }


    public void fireEvent(String eventName, Object value)
    {
        List<TableModelListener> tmListeners = new LinkedList<>(this.listeners);
        for(TableModelListener listener : tmListeners)
        {
            listener.onEvent(eventName, value);
        }
    }


    protected UIAccessRightService getUIAccessRightService()
    {
        return UISessionUtils.getCurrentSession().getUiAccessRightService();
    }
}
