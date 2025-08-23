package de.hybris.platform.warehousing.daos.impl;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.warehousing.daos.WarehousingConsignmentEntryQuantityDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultWarehousingConsignmentEntryQuantityDao extends AbstractItemDao implements WarehousingConsignmentEntryQuantityDao
{
    protected static final String shippedQuery = "SELECT SUM({consignmentEntry:quantity}) FROM {" + GeneratedBasecommerceConstants.TC.CONSIGNMENTENTRY + " as consignmentEntry JOIN " + GeneratedBasecommerceConstants.TC.CONSIGNMENT
                    + " as consignment ON {consignmentEntry:consignment}={consignment:pk}} WHERE {consignmentEntry.pk}=?consignmentEntry AND ({consignment.status}=?shippedStatus OR {consignment.status}=?pickedupStatus)";
    protected static final String declinedQuery = "SELECT SUM({dcee:quantity}) FROM {DeclineConsignmentEntryEvent as dcee} WHERE {dcee:consignmentEntry}=?consignmentEntry";


    public Long getQuantityShipped(ConsignmentEntryModel consignmentEntry)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("consignmentEntry", consignmentEntry);
        params.put("shippedStatus", ConsignmentStatus.SHIPPED);
        params.put("pickedupStatus", ConsignmentStatus.PICKUP_COMPLETE);
        Long quantity = processRequestWithParams(shippedQuery, params);
        return quantity;
    }


    public Long getQuantityDeclined(ConsignmentEntryModel consignmentEntry)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("consignmentEntry", consignmentEntry);
        return processRequestWithParams("SELECT SUM({dcee:quantity}) FROM {DeclineConsignmentEntryEvent as dcee} WHERE {dcee:consignmentEntry}=?consignmentEntry", params);
    }


    public Long processRequestWithParams(String queryString, Map<String, Object> params)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryString);
        params.keySet().forEach(key -> fQuery.addQueryParameter(key, params.get(key)));
        List<Class<Long>> resultClassList = new ArrayList<>();
        resultClassList.add(Long.class);
        fQuery.setResultClassList(resultClassList);
        SearchResult<Long> result = getFlexibleSearchService().search(fQuery);
        return result.getResult().stream().filter(res -> (res != null)).findFirst().orElse(Long.valueOf(0L));
    }
}
