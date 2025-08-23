package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.MutableColumnModel;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTableModel extends AbstractTableModel implements MutableTableModel
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultTableModel.class);
    private static final String TABLE_VALUE_REQUEST_CACHE = "tableValueRequestCache";
    protected MutableListModel listComponentModel;
    protected MutableColumnModel columnComponentModel;
    protected UUID uid = null;


    public DefaultTableModel(MutableListModel listComponentModel, MutableColumnModel columnComponentModel)
    {
        this.uid = UUID.randomUUID();
        this.listComponentModel = listComponentModel;
        this.columnComponentModel = columnComponentModel;
    }


    public void setValueAt(int colIndex, int rowIndex, Object data) throws ValueHandlerException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("setValueAt: col='" + colIndex + "', row='" + rowIndex + "', data='" + data + "'.");
        }
        try
        {
            getColumnComponentModel().setValueAt(colIndex, (TypedObject)getListComponentModel().getListModel().elementAt(rowIndex), data);
            (new Object(this, "tableValueRequestCache"))
                            .updateCache(colIndex + '.' + rowIndex + "_" + this.uid, data);
        }
        finally
        {
            fireCellChanged(colIndex, rowIndex);
        }
    }


    public void setSelectedCell(int colIndex, int rowIndex)
    {
        boolean rowChanged = this.listComponentModel.setSelectedIndexDirectly(rowIndex);
        boolean colChanged = this.columnComponentModel.setSelectedIndexDirectly(colIndex);
        if(rowChanged || colChanged)
        {
            fireSelectionChanged(this.columnComponentModel.getSelectedIndexes(), this.listComponentModel.getSelectedIndexes());
        }
    }


    public void setSelectedCells(List<Integer> colIndexes, List<Integer> rowIndexes)
    {
        boolean rowChanged = this.listComponentModel.setSelectedIndexesDirectly(rowIndexes);
        boolean colChanged = this.columnComponentModel.setSelectedIndexesDirectly(colIndexes);
        if(rowChanged || colChanged)
        {
            fireSelectionChanged(this.columnComponentModel.getSelectedIndexes(), this.listComponentModel.getSelectedIndexes());
        }
    }


    public UIEditor getCellEditor(int columnIndex, int rowIndex)
    {
        return getColumnComponentModel().getCellEditor(columnIndex);
    }


    public MutableColumnModel getColumnComponentModel()
    {
        return this.columnComponentModel;
    }


    public MutableListModel getListComponentModel()
    {
        return this.listComponentModel;
    }


    public Object getValueAt(int colIndex, int rowIndex)
    {
        return (new Object(this, "tableValueRequestCache", rowIndex, colIndex))
                        .get(colIndex + '.' + rowIndex + "_" + this.uid);
    }


    protected boolean checkUserEditPermission(int colIndex, int rowIndex)
    {
        boolean ret = true;
        ListModel<? extends TypedObject> listModel = getListComponentModel().getListModel();
        if(listModel == null)
        {
            LOG.warn("Could not determine user permission. Reason: List model was null.");
            ret = false;
        }
        else
        {
            PropertyDescriptor propertyDescriptor = getColumnComponentModel().getPropertyDescriptor(
                            getColumnComponentModel().getVisibleColumn(colIndex));
            if(propertyDescriptor != null)
            {
                TypedObject typedObject = listModel.getElements().get(rowIndex);
                ret = getUIAccessRightService().isWritable((ObjectType)typedObject.getType(), typedObject, propertyDescriptor, false);
            }
            if(ValueHandler.NOT_READABLE_VALUE.equals(getValueAt(colIndex, rowIndex)))
            {
                ret = false;
            }
        }
        return ret;
    }


    public boolean isCellEditable(int colIndex, int rowIndex)
    {
        ColumnDescriptor visibleColumn = getColumnComponentModel().getVisibleColumn(colIndex);
        return (visibleColumn != null && visibleColumn.isEditable() && this.listComponentModel.isEditable() &&
                        checkUserEditPermission(colIndex, rowIndex));
    }


    public boolean isCellSelectable(int colIndex, int rowIndex)
    {
        return (this.listComponentModel.isSelectable() && getColumnComponentModel().getVisibleColumn(colIndex).isSelectable());
    }


    public boolean isCellSelected(int colIndex, int rowIndex)
    {
        return (this.columnComponentModel.isColumnSelected(colIndex) && this.listComponentModel.isSelected(rowIndex));
    }


    public CellRenderer getCellRenderer(int columnIndex)
    {
        return getColumnComponentModel().getCellRenderer(columnIndex);
    }


    public CellRenderer getNewInlineItemRenderer(int columnIndex)
    {
        return getColumnComponentModel().getNewInlineItemRenderer(columnIndex);
    }


    public void setListComponentModel(MutableListModel listModel)
    {
        this.listComponentModel = listModel;
    }


    public void setColumnComponentModel(MutableColumnModel columnModel)
    {
        this.columnComponentModel = columnModel;
        updateDynamicColumns();
    }


    public void updateDynamicColumns()
    {
        if(this.listComponentModel != null && this.columnComponentModel != null)
        {
            this.columnComponentModel.updateDynamicColumns(this.listComponentModel.getListModel());
        }
    }
}
