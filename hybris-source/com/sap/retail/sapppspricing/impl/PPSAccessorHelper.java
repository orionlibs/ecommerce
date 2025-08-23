/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.retail.sapppspricing.impl;

import com.sap.retail.opps.v1.dto.LineItemDomainSpecific;
import com.sap.retail.opps.v1.dto.SaleBase;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
//new


/**
 * Helper class to access parts of a PPS request / response
 */
public class PPSAccessorHelper extends DefaultPPSClientBeanAccessor
{
    /**
     * Look up next regular line item
     *
     * @param lineItems
     *           all line items to consider
     * @param startIndex
     *           start index for searching
     * @return corresponding line item and its position in the total list of items
     */
    public Pair<Integer, SaleBase> nextNonDiscountItem(final List<LineItemDomainSpecific> lineItems, final int startIndex)
    {
        for(int i = startIndex; i < lineItems.size(); i++)
        {
            final SaleBase content = getHelper().getLineItemContent(lineItems.get(i));
            if(content != null)
            {
                return Pair.of(Integer.valueOf(i), content);
            }
        }
        return null;
    }
}
