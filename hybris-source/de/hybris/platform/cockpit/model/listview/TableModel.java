package de.hybris.platform.cockpit.model.listview;

import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.general.ListComponentModel;

public interface TableModel
{
    ListComponentModel getListComponentModel();


    ColumnModel getColumnComponentModel();


    Object getValueAt(int paramInt1, int paramInt2);


    boolean isCellSelected(int paramInt1, int paramInt2);


    boolean isCellEditable(int paramInt1, int paramInt2);


    boolean isCellSelectable(int paramInt1, int paramInt2);


    UIEditor getCellEditor(int paramInt1, int paramInt2);


    CellRenderer getCellRenderer(int paramInt);


    CellRenderer getNewInlineItemRenderer(int paramInt);


    void addTableModelListener(TableModelListener paramTableModelListener);


    void removeTableModelListener(TableModelListener paramTableModelListener);


    void fireEvent(String paramString, Object paramObject);
}
