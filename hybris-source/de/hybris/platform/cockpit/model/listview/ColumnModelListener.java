package de.hybris.platform.cockpit.model.listview;

import java.util.List;

public interface ColumnModelListener
{
    void columnMoved(int paramInt1, int paramInt2);


    void columnVisibilityChanged();


    void columnVisibilityChanged(Integer paramInteger);


    void selectionChanged(List<? extends Object> paramList);


    void sortChanged(int paramInt, boolean paramBoolean);


    void changed();
}
