package de.hybris.platform.cockpit.model.listview;

import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.List;

public interface ColumnModel
{
    void addColumnModelListener(ColumnModelListener paramColumnModelListener);


    int findColumn(ColumnDescriptor paramColumnDescriptor);


    UIEditor getCellEditor(int paramInt);


    CellRenderer getCellRenderer(int paramInt);


    ColumnDescriptor getColumn(int paramInt) throws IndexOutOfBoundsException;


    List<ColumnDescriptor> getColumns();


    String getColumnWidth(ColumnDescriptor paramColumnDescriptor);


    String getColumnWidth(ColumnDescriptor paramColumnDescriptor, Boolean paramBoolean);


    List<ColumnDescriptor> getHiddenColumns();


    List<ColumnDescriptor> getHiddenLocalizedColumns(ColumnDescriptor paramColumnDescriptor);


    ColumnDescriptor getLocalizedColumn(ColumnDescriptor paramColumnDescriptor, LanguageModel paramLanguageModel);


    ListViewMenuPopupBuilder getMenuPopupBuilder();


    CellRenderer getNewInlineItemRenderer(int paramInt);


    PropertyDescriptor getPropertyDescriptor(ColumnDescriptor paramColumnDescriptor);


    ColumnGroup getRootColumnGroup();


    int getSelectedIndex();


    List<Integer> getSelectedIndexes();


    int getSortedByColumnIndex();


    Object getValueAt(int paramInt, TypedObject paramTypedObject);


    ColumnDescriptor getVisibleColumn(int paramInt) throws IndexOutOfBoundsException;


    List<ColumnDescriptor> getVisibleColumns();


    List<ColumnDescriptor> getVisibleLocalizedColumns(ColumnDescriptor paramColumnDescriptor);


    boolean isColumnLocalized(ColumnDescriptor paramColumnDescriptor);


    boolean isColumnSelected(int paramInt);


    boolean isSortAscending();


    void removeColumnModelListener(ColumnModelListener paramColumnModelListener);
}
