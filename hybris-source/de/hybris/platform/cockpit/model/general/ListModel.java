package de.hybris.platform.cockpit.model.general;

import java.util.List;

public interface ListModel<T>
{
    T elementAt(int paramInt) throws IndexOutOfBoundsException;


    List<T> getElements();


    boolean isRemovable(int paramInt) throws IndexOutOfBoundsException;


    boolean isMovable(int paramInt) throws IndexOutOfBoundsException;


    boolean isEditable();


    int size();


    boolean isEmpty();


    void addListModelDataListener(ListModelDataListener paramListModelDataListener);


    void removeListModelDataListener(ListModelDataListener paramListModelDataListener);
}
