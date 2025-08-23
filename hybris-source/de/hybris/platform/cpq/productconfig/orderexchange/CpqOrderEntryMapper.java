/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.orderexchange;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundOrderItemModel;
import java.util.List;

/**
 * Enriches the order with the cpq details of the given order entry
 */
public interface CpqOrderEntryMapper
{
    /**
     * Enriches the order with the cpq details of the given order entry
     *
     * @param entry
     *           order entry
     * @param outboundItem
     *           associated with order entry
     * @return additional outbound order items resulting from the cpq product
     */
    List<SAPCpiOutboundOrderItemModel> mapCPQLineItems(final AbstractOrderEntryModel entry,
                    SAPCpiOutboundOrderItemModel outboundItem);


    /**
     * Provides information whether call to this mapper is required
     *
     * @param entry
     *           order entry to be checked
     * @return whether call to this mapper is necessary
     */
    boolean isMapperApplicable(final AbstractOrderEntryModel entry);


    /**
     * @param entry
     *           order entry for which line items should be retrieved
     * @param outboundItem
     *           associated with order entry
     * @param cpqLineItems
     *           list that will be filled with additional outbound order items resulting from the cpq product
     */
    void retrieveCPQLineItems(final AbstractOrderEntryModel entry, SAPCpiOutboundOrderItemModel outboundItem,
                    final List<SAPCpiOutboundOrderItemModel> cpqLineItems);
}
