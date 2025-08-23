package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.general.impl.AbstractItemView;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.ListViewListener;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.zkoss.zk.ui.event.Event;

public abstract class AbstractListView extends AbstractItemView implements UIListView
{
    protected List<ListViewListener> listeners = new ArrayList<>();


    public void addListViewListener(ListViewListener listener)
    {
        if(!this.listeners.contains(listener))
        {
            this.listeners.add(listener);
        }
    }


    public void removeListViewListener(ListViewListener listener)
    {
        this.listeners.remove(listener);
    }


    protected void firePageRequest(int offset)
    {
        List<ListViewListener> lvListeners = new LinkedList<>(this.listeners);
        for(ListViewListener listener : lvListeners)
        {
            listener.requestPaging(offset);
        }
    }


    protected void fireMove(int fromIndex, int toIndex)
    {
        List<ListViewListener> lvListeners = new LinkedList<>(this.listeners);
        for(ListViewListener listener : lvListeners)
        {
            listener.move(fromIndex, toIndex);
        }
    }


    protected void fireDroped(Event event, int fromIndex, int toIndex, DragAndDropContext ddContext)
    {
        List<ListViewListener> lvListeners = new LinkedList<>(this.listeners);
        for(ListViewListener listener : lvListeners)
        {
            listener.drop(fromIndex, toIndex, ddContext);
        }
    }


    protected void fireRemove(Collection<Integer> indexes)
    {
        List<ListViewListener> lvListeners = new LinkedList<>(this.listeners);
        for(ListViewListener listener : lvListeners)
        {
            listener.remove(indexes);
        }
    }


    protected void fireBlacklist(Collection<Integer> indexes)
    {
        List<ListViewListener> lvListeners = new LinkedList<>(this.listeners);
        for(ListViewListener listener : lvListeners)
        {
            listener.remove(indexes);
        }
    }


    protected void fireActivate(List<Integer> indexes)
    {
        List<ListViewListener> lvListeners = new LinkedList<>(this.listeners);
        for(ListViewListener listener : lvListeners)
        {
            listener.activate(indexes);
        }
    }


    protected void fireChangeSelection(int colIndex, int rowIndex)
    {
        List<ListViewListener> lvListeners = new LinkedList<>(this.listeners);
        for(ListViewListener listener : lvListeners)
        {
            listener.changeSelection(colIndex, rowIndex);
        }
    }


    protected void fireChangeSelection(List<Integer> colIndexes, List<Integer> rowIndexes)
    {
        List<ListViewListener> lvListeners = new LinkedList<>(this.listeners);
        for(ListViewListener listener : lvListeners)
        {
            listener.changeSelection(colIndexes, rowIndexes);
        }
    }


    protected void fireChangeCellValue(int colIndex, int rowIndex, Object data)
    {
        List<ListViewListener> lvListeners = new LinkedList<>(this.listeners);
        for(ListViewListener listener : lvListeners)
        {
            listener.changeCellValue(colIndex, rowIndex, data);
        }
    }


    protected void fireOpenExternal(int rowIndex, UIEditor editor, PropertyDescriptor propertyDescriptor)
    {
        List<ListViewListener> lvListeners = new LinkedList<>(this.listeners);
        for(ListViewListener listener : lvListeners)
        {
            listener.openInContextEditor(rowIndex, editor, propertyDescriptor);
        }
    }


    protected void fireMoveColumn(int fromIndex, int toIndex)
    {
        List<ListViewListener> lvListeners = new LinkedList<>(this.listeners);
        for(ListViewListener listener : lvListeners)
        {
            listener.moveColumn(fromIndex, toIndex);
        }
    }


    protected void fireHideColumn(int colIndex)
    {
        List<ListViewListener> lvListeners = new LinkedList<>(this.listeners);
        for(ListViewListener listener : lvListeners)
        {
            listener.hideColumn(colIndex);
        }
    }


    public void fireShowColumn(ColumnDescriptor colDescr, Integer colIndex)
    {
        List<ListViewListener> lvListeners = new LinkedList<>(this.listeners);
        for(ListViewListener listener : lvListeners)
        {
            listener.showColumn(colDescr, colIndex);
        }
    }


    protected void fireSortColumn(int columnIndex, boolean asc)
    {
        List<ListViewListener> lvListeners = new LinkedList<>(this.listeners);
        for(ListViewListener listener : lvListeners)
        {
            listener.sortColumn(columnIndex, asc);
        }
    }


    protected void fireMarkAllAsSelected(List<Integer> colIndexes, List<Integer> rowIndexes)
    {
        List<ListViewListener> lvListeners = new LinkedList<>(this.listeners);
        for(ListViewListener listener : lvListeners)
        {
            listener.markAllAsSelected(colIndexes, rowIndexes);
        }
    }


    protected void fireMultiEdit(int colIndex, List<Integer> rowIndexes, Object data)
    {
        List<ListViewListener> lvListeners = new LinkedList<>(this.listeners);
        for(ListViewListener listener : lvListeners)
        {
            listener.multiEdit(colIndex, rowIndexes, data);
        }
    }
}
