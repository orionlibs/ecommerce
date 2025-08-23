package de.hybris.platform.adaptivesearchbackoffice.common;

import java.util.List;
import java.util.Map;

public interface DataProvider<D, V>
{
    List<D> getData(Map<String, Object> paramMap);


    V getValue(D paramD, Map<String, Object> paramMap);


    String getLabel(D paramD, Map<String, Object> paramMap);
}
