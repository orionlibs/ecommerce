package de.hybris.platform.cockpit.model.listview;

import de.hybris.platform.cockpit.model.general.UIItemView;

public interface UIListView extends UIItemView
{
    void setModel(TableModel paramTableModel);


    TableModel getModel();


    void setValueAt(int paramInt1, int paramInt2, Object paramObject);


    Object getValueAt(int paramInt1, int paramInt2);


    void editCellAt(int paramInt1, int paramInt2);


    void setShowColumnHeaders(boolean paramBoolean);


    void addListViewListener(ListViewListener paramListViewListener);


    void removeListViewListener(ListViewListener paramListViewListener);


    void updateCell(int paramInt1, int paramInt2);


    void updateRow(int paramInt);


    void updateVisibleColumns();


    void updateColumns();


    void updateColumns(Integer paramInteger);
}
