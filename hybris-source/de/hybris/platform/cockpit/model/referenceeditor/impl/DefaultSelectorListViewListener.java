package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.ListViewListener;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.AbstractReferenceSelectorModel;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropContext;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSelectorListViewListener implements ListViewListener
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSelectorListViewListener.class);
    private final AbstractReferenceSelectorModel referenceSelectorModel;


    public DefaultSelectorListViewListener(AbstractReferenceSelectorModel referenceSelectorModel)
    {
        this.referenceSelectorModel = referenceSelectorModel;
    }


    public void activate(List<Integer> indexes)
    {
        MutableListModel listComponentModel = this.referenceSelectorModel.getTableModel().getListComponentModel();
        List<? extends TypedObject> elements = listComponentModel.getListModel().getElements();
        List<TypedObject> activeItems = new ArrayList<>();
        for(Integer index : indexes)
        {
            if(index.intValue() >= 0 && index.intValue() < elements.size())
            {
                activeItems.add(elements.get(index.intValue()));
            }
        }
        this.referenceSelectorModel.getTableModel().getListComponentModel().setActiveItems(activeItems);
    }


    public void changeCellValue(int columnIndex, int rowIndex, Object data)
    {
        try
        {
            this.referenceSelectorModel.getTableModel().setValueAt(columnIndex, rowIndex, data);
        }
        catch(ValueHandlerException e)
        {
            LOG.error("Could not set the value '" + data + "' for the cell in column '" + columnIndex + "' (Reason: " + e
                            .getMessage() + ").", (Throwable)e);
        }
    }


    public void changeSelection(int columnIndex, int rowIndex)
    {
        int removeIndex = -1;
        List<Integer> indexes = new ArrayList<>(this.referenceSelectorModel.getTableModel().getListComponentModel().getSelectedIndexes());
        List<Integer> columnIndexes = new ArrayList<>();
        columnIndexes.add(Integer.valueOf(0));
        if(this.referenceSelectorModel.getTableModel().getListComponentModel().isMultiple())
        {
            for(int indx = 0; indx < indexes.size(); indx++)
            {
                if(((Integer)indexes.get(indx)).intValue() == rowIndex)
                {
                    removeIndex = indx;
                    break;
                }
            }
            if(removeIndex == -1)
            {
                indexes.add(Integer.valueOf(rowIndex));
            }
            else
            {
                indexes.remove(removeIndex);
            }
            this.referenceSelectorModel.getTableModel().setSelectedCells(columnIndexes, indexes);
        }
        else
        {
            this.referenceSelectorModel.getTableModel().setSelectedCell(0, rowIndex);
        }
    }


    public void changeSelection(List<Integer> columnIndexes, List<Integer> rowIndexes)
    {
        List<Integer> currentlySelected = new ArrayList<>(this.referenceSelectorModel.getTableModel().getListComponentModel().getSelectedIndexes());
        List<Integer> toRemove = new ArrayList<>();
        List<Integer> toAdd = new ArrayList<>();
        if(this.referenceSelectorModel.getTableModel().getListComponentModel().isMultiple())
        {
            for(Integer selected : rowIndexes)
            {
                if(currentlySelected.contains(selected))
                {
                    toRemove.add(selected);
                    continue;
                }
                toAdd.add(selected);
            }
            currentlySelected.removeAll(toRemove);
            currentlySelected.addAll(toAdd);
            this.referenceSelectorModel.getTableModel()
                            .setSelectedCells(Collections.singletonList(Integer.valueOf(0)), currentlySelected);
        }
        else
        {
            for(Integer selected : rowIndexes)
            {
                if(currentlySelected.contains(selected))
                {
                    toRemove.add(selected);
                }
            }
            List<Integer> finalSelected = new ArrayList<>(rowIndexes);
            finalSelected.removeAll(toRemove);
            this.referenceSelectorModel.getTableModel().setSelectedCells(Collections.singletonList(Integer.valueOf(0)), finalSelected);
        }
    }


    public void move(int fromIndex, int toIndex)
    {
    }


    public void remove(Collection<Integer> indexes)
    {
    }


    public void sortColumn(int columnIndex, boolean asc)
    {
        this.referenceSelectorModel.getTableModel().getColumnComponentModel().setSortedColumnIndex(columnIndex, asc);
    }


    public void hideColumn(int colIndex)
    {
        this.referenceSelectorModel.getTableModel().getColumnComponentModel().hideColumn(colIndex);
    }


    public void moveColumn(int fromIndex, int toIndex)
    {
        this.referenceSelectorModel.getTableModel().getColumnComponentModel().moveColumn(fromIndex, toIndex);
    }


    public void showColumn(ColumnDescriptor columnDescr, Integer colIndex)
    {
        this.referenceSelectorModel.getTableModel().getColumnComponentModel().showColumn(columnDescr, colIndex);
    }


    public void requestPaging(int offset)
    {
    }


    public void openInContextEditor(int rowIndex, UIEditor editor, PropertyDescriptor propertyDescriptor)
    {
    }


    public void markAllAsSelected(List<Integer> columnIndexes, List<Integer> rowIndexes)
    {
    }


    public void multiEdit(int colIndex, List<Integer> rowIndexes, Object data)
    {
    }


    public void drop(int fromIndex, int toIndex, DragAndDropContext ddContext)
    {
    }
}
