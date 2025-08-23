/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.cpiorderexchange.cps.service.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.sap.productconfig.cpiorderexchange.cps.service.MappingDecisionStrategy;
import de.hybris.platform.sap.productconfig.runtime.cps.enums.SAPProductConfigOrderOutboundMappingMode;
import de.hybris.platform.store.BaseStoreModel;

public class DefaultMappingDecisionStrategy implements MappingDecisionStrategy
{
    @Override
    public boolean isA2AServiceMappingActive(final AbstractOrderModel order)
    {
        Preconditions.checkNotNull(order, "We expect a non-null order");
        final BaseStoreModel baseStore = order.getStore();
        Preconditions.checkNotNull(baseStore, "Order must carry a base store");
        final SAPConfigurationModel sapConfiguration = baseStore.getSAPConfiguration();
        Preconditions.checkNotNull(sapConfiguration, "Base store must carry an SAP configuration");
        return SAPProductConfigOrderOutboundMappingMode.A2ASERVICEFORMAT
                        .equals(sapConfiguration.getSapproductconfig_order_outbound_mapping_mode());
    }
}
