package de.hybris.platform.cockpit.model.listview;

import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import java.util.List;

public interface MutableTableModel extends TableModel
{
    void setValueAt(int paramInt1, int paramInt2, Object paramObject) throws ValueHandlerException;


    void setSelectedCell(int paramInt1, int paramInt2);


    void setSelectedCells(List<Integer> paramList1, List<Integer> paramList2);


    MutableListModel getListComponentModel();


    void setListComponentModel(MutableListModel paramMutableListModel);


    MutableColumnModel getColumnComponentModel();


    void setColumnComponentModel(MutableColumnModel paramMutableColumnModel);


    void updateDynamicColumns();
}
