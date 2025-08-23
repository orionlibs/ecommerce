package de.hybris.platform.impex.jalo.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class CSVMap<E> extends LinkedHashMap<Integer, E>
{
    public CSVMap()
    {
        super(20);
    }


    public CSVMap(Map<Integer, E> src)
    {
        super(src);
    }


    public E getCellValue(int index)
    {
        return (E)get(Integer.valueOf(index));
    }


    public E putCellValue(int index, E value)
    {
        return (E)put(Integer.valueOf(index), value);
    }


    public E removeCellValue(int index)
    {
        return (E)remove(Integer.valueOf(index));
    }
}
