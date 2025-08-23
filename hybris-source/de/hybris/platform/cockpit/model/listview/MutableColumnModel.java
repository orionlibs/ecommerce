package de.hybris.platform.cockpit.model.listview;

import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import java.util.List;

public interface MutableColumnModel extends ColumnModel
{
    boolean showColumn(ColumnDescriptor paramColumnDescriptor, Integer paramInteger);


    boolean hideColumn(int paramInt);


    boolean moveColumn(int paramInt1, int paramInt2);


    void setSelectedIndex(int paramInt);


    boolean setSelectedIndexDirectly(int paramInt);


    void setSelectedIndexes(List<Integer> paramList);


    boolean setSelectedIndexesDirectly(List<Integer> paramList);


    void setSortAscending(boolean paramBoolean);


    void setSortedColumnIndex(int paramInt, boolean paramBoolean);


    void setValueAt(int paramInt, TypedObject paramTypedObject, Object paramObject) throws ValueHandlerException;


    void setVisibleColumns(List<ColumnDescriptor> paramList, boolean paramBoolean);


    void updateDynamicColumns(ListModel paramListModel);
}
