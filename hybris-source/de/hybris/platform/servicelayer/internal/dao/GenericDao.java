package de.hybris.platform.servicelayer.internal.dao;

import java.util.List;
import java.util.Map;

public interface GenericDao<M extends de.hybris.platform.core.model.ItemModel>
{
    List<M> find();


    List<M> find(Map<String, ? extends Object> paramMap);


    List<M> find(SortParameters paramSortParameters);


    List<M> find(Map<String, ? extends Object> paramMap, SortParameters paramSortParameters);


    List<M> find(Map<String, ? extends Object> paramMap, SortParameters paramSortParameters, int paramInt);
}
