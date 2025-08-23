/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.sapomsreturnprocess.outbound.impl;

import com.sap.hybris.returnsexchange.outbound.impl.DefaultReturnOrderContributor;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.sapmodel.model.SAPSalesOrganizationModel;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class OmsDefaultReturnOrderContributor extends DefaultReturnOrderContributor
{
    @Override
    public Set<String> getColumns()
    {
        final Set<String> columns = super.getColumns();
        columns.addAll(Arrays.asList(OrderCsvColumns.LOGICAL_SYSTEM, OrderCsvColumns.SALES_ORGANIZATION,
                        OrderCsvColumns.DISTRIBUTION_CHANNEL, OrderCsvColumns.DIVISION));
        return columns;
    }


    @Override
    public List<Map<String, Object>> createRows(final ReturnRequestModel returnRequest)
    {
        final List<Map<String, Object>> rows = super.createRows(returnRequest);
        return enhanceRowForOmsReturn(returnRequest, rows);
    }


    /**
     * @param returnRequest
     *
     */
    private List<Map<String, Object>> enhanceRowForOmsReturn(final ReturnRequestModel returnRequest,
                    final List<Map<String, Object>> rows)
    {
        // There is only one row on order level
        final Map<String, Object> row = rows.get(0);
        final SAPSalesOrganizationModel sapSalesOrganization = returnRequest.getSapSalesOrganization();
        if(sapSalesOrganization != null)
        {
            row.put(OrderCsvColumns.LOGICAL_SYSTEM, returnRequest.getSapLogicalSystem());
            row.put(OrderCsvColumns.SALES_ORGANIZATION, sapSalesOrganization.getSalesOrganization());
            row.put(OrderCsvColumns.DISTRIBUTION_CHANNEL, sapSalesOrganization.getDistributionChannel());
            row.put(OrderCsvColumns.DIVISION, sapSalesOrganization.getDivision());
        }
        return rows;
    }
}
