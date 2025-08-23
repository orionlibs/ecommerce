package de.hybris.platform.cockpit.components.listeditor;

import java.util.List;

public interface ListSelectionModel
{
    boolean isSelectedIndex(int paramInt);


    void clearSelection();


    void setSelectionInterval(int paramInt1, int paramInt2);


    void addSelectionInterval(int paramInt1, int paramInt2);


    void toggleSelectionInterval(int paramInt1, int paramInt2);


    void insertIndexRange(int paramInt1, int paramInt2);


    void removeIndexRange(int paramInt1, int paramInt2);


    int getMinSelectionIndex();


    int getMaxSelectionIndex();


    List<Integer> getAllSelectedIndexes();
}
