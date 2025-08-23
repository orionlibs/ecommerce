package de.hybris.platform.cockpit.model.listview;

import java.util.List;

public interface TableModelListener
{
    void selectionChanged(List<Integer> paramList1, List<Integer> paramList2);


    void cellChanged(int paramInt1, int paramInt2);


    void onEvent(String paramString, Object paramObject);
}
