package de.hybris.platform.warehousing.daos.impl;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.warehousing.daos.WarehousingOrderEntryQuantityDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultWarehousingOrderEntryQuantityDao extends AbstractItemDao implements WarehousingOrderEntryQuantityDao
{
    protected static final String cancelQuery = "SELECT SUM({ocr:cancelledQuantity}) FROM {" + GeneratedBasecommerceConstants.TC.ORDERENTRYCANCELRECORDENTRY + " as ocr} WHERE {ocr:orderEntry}=?orderEntry";
    protected static final String returnedQuery = "SELECT SUM({returnEntry:receivedQuantity}) FROM {" + GeneratedBasecommerceConstants.TC.RETURNENTRY + " as returnEntry JOIN " + GeneratedBasecommerceConstants.TC.RETURNREQUEST
                    + " as returnRequest ON {returnEntry:returnRequest}={returnRequest:pk}} WHERE {returnEntry:orderEntry}=?orderEntry AND ({returnRequest:status}=?receivedStatus OR {returnRequest:status}=?completedStatus)";


    public Long getCancelledQuantity(OrderEntryModel orderEntry)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("orderEntry", orderEntry);
        return processRequestWithParams(cancelQuery, params);
    }


    public Long getQuantityReturned(OrderEntryModel orderEntry)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("orderEntry", orderEntry);
        params.put("receivedStatus", ReturnStatus.RECEIVED);
        params.put("completedStatus", ReturnStatus.COMPLETED);
        return processRequestWithParams(returnedQuery, params);
    }


    public Long processRequestWithParams(String queryString, Map<String, Object> params)
    {
        if(params.keySet().stream().anyMatch(key -> (params.get(key) instanceof ItemModel && ((ItemModel)params.get(key)).getPk() == null)))
        {
            return Long.valueOf(0L);
        }
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryString);
        params.keySet().forEach(key -> fQuery.addQueryParameter(key, params.get(key)));
        List<Class<Long>> resultClassList = new ArrayList<>();
        resultClassList.add(Long.class);
        fQuery.setResultClassList(resultClassList);
        SearchResult<Long> result = getFlexibleSearchService().search(fQuery);
        return result.getResult().stream().filter(res -> (res != null)).findFirst().orElse(Long.valueOf(0L));
    }
}
