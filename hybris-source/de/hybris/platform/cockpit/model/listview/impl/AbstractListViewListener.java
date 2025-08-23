package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.ListViewListener;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractListViewListener implements ListViewListener
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractListViewListener.class);
    protected final MutableTableModel model;


    public AbstractListViewListener(MutableTableModel model)
    {
        this.model = model;
    }


    public void activate(List<Integer> indexes)
    {
        MutableListModel listComponentModel = this.model.getListComponentModel();
        List<? extends TypedObject> elements = listComponentModel.getListModel().getElements();
        List<TypedObject> activeItems = new ArrayList<>();
        for(Integer index : indexes)
        {
            if(index.intValue() >= 0 && index.intValue() < elements.size())
            {
                activeItems.add(elements.get(index.intValue()));
            }
        }
        if(!activeItems.isEmpty())
        {
            UISessionUtils.getCurrentSession().getCurrentPerspective().activateItemInEditor(activeItems.iterator().next());
        }
    }


    public void changeCellValue(int columnIndex, int rowIndex, Object data)
    {
        try
        {
            this.model.setValueAt(columnIndex, rowIndex, data);
        }
        catch(Exception e)
        {
            LOG.error("Could not set the value '" + data + "' for the cell in column '" + columnIndex + "' (Reason: " + e
                            .getMessage() + ").", e);
        }
    }


    public void changeSelection(int columnIndex, int rowIndex)
    {
        this.model.setSelectedCell(columnIndex, rowIndex);
    }


    public void changeSelection(List<Integer> columnIndexes, List<Integer> rowIndexes)
    {
        this.model.setSelectedCells(columnIndexes, rowIndexes);
    }


    public void sortColumn(int columnIndex, boolean asc)
    {
        this.model.getColumnComponentModel().setSortedColumnIndex(columnIndex, asc);
    }


    public void hideColumn(int colIndex)
    {
        this.model.getColumnComponentModel().hideColumn(colIndex);
    }


    public void moveColumn(int fromIndex, int toIndex)
    {
        this.model.getColumnComponentModel().moveColumn(fromIndex, toIndex);
    }


    public void showColumn(ColumnDescriptor columnDescr, Integer colIndex)
    {
        this.model.getColumnComponentModel().showColumn(columnDescr, colIndex);
    }
}
