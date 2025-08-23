/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.returnsexchange.outbound.impl;

import com.sap.hybris.returnsexchange.constants.ReturnOrderEntryCsvColumns;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.outbound.RawItemContributor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultPrecedingDocContributor implements RawItemContributor<ReturnRequestModel>
{
    @Override
    public Set<String> getColumns()
    {
        return new HashSet<>(Arrays.asList(OrderCsvColumns.ORDER_ID, ReturnOrderEntryCsvColumns.PRECEDING_DOCUMENT_ID));
    }


    @Override
    public List<Map<String, Object>> createRows(final ReturnRequestModel model)
    {
        final List<Map<String, Object>> result = new ArrayList<>();
        final Map<String, Object> row = new HashMap<>();
        row.put(OrderCsvColumns.ORDER_ID, model.getCode());
        row.put(ReturnOrderEntryCsvColumns.PRECEDING_DOCUMENT_ID, model.getOrder().getCode());
        result.add(row);
        return result;
    }
}
