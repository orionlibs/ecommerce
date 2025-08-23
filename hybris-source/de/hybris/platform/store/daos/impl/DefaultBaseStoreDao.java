package de.hybris.platform.store.daos.impl;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.daos.BaseStoreDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultBaseStoreDao extends AbstractItemDao implements BaseStoreDao
{
    public List<BaseStoreModel> findBaseStoresByUid(String uid)
    {
        String query = "SELECT {pk} FROM {BaseStore} WHERE {uid} =?uid";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {BaseStore} WHERE {uid} =?uid");
        Map<String, Object> params = new HashMap<>();
        params.put("uid", uid);
        fQuery.addQueryParameters(params);
        SearchResult<BaseStoreModel> result = search(fQuery);
        return result.getResult();
    }


    public List<BaseStoreModel> findAllBaseStores()
    {
        String query = "SELECT {pk} FROM {BaseStore}";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {BaseStore}");
        SearchResult<BaseStoreModel> result = search(fQuery);
        return result.getResult();
    }
}
