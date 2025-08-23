/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.returnsexchange.outbound.impl;

import com.sap.hybris.returnsexchange.constants.ReturnOrderEntryCsvColumns;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.outbound.RawItemContributor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultReturnOrderContributor implements RawItemContributor<ReturnRequestModel>
{
    private final Set<String> columns = new HashSet<>(
                    Arrays.asList(OrderCsvColumns.ORDER_ID, OrderCsvColumns.DATE, OrderCsvColumns.ORDER_CURRENCY_ISO_CODE,
                                    OrderCsvColumns.PAYMENT_MODE, OrderCsvColumns.DELIVERY_MODE, OrderCsvColumns.BASE_STORE, ReturnOrderEntryCsvColumns.RMA));


    @Override
    public Set<String> getColumns()
    {
        return columns;
    }


    @Override
    public List<Map<String, Object>> createRows(ReturnRequestModel returnRequest)
    {
        final Map<String, Object> row = new HashMap<>();
        row.put(OrderCsvColumns.ORDER_ID, returnRequest.getCode());
        row.put(OrderCsvColumns.DATE, returnRequest.getCreationtime());
        row.put(OrderCsvColumns.ORDER_CURRENCY_ISO_CODE, returnRequest.getOrder().getCurrency().getIsocode());
        final DeliveryModeModel deliveryMode = returnRequest.getOrder().getDeliveryMode();
        row.put(OrderCsvColumns.DELIVERY_MODE, deliveryMode != null ? deliveryMode.getCode() : "");
        row.put(OrderCsvColumns.BASE_STORE, returnRequest.getOrder().getStore().getUid());
        row.put(ReturnOrderEntryCsvColumns.RMA, returnRequest.getRMA());
        return Arrays.asList(row);
    }
}
