package de.hybris.platform.cockpit.util;

import java.util.List;

public interface ListProvider<T>
{
    List<T> getList();


    T getFirst();


    ListInfo getListInfo();


    int getListSize();
}
