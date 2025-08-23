/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.sapoaacosintegration.services.sourcing;

import com.sap.sapoaacosintegration.services.sourcing.request.CosSourcingRequest;
import de.hybris.platform.core.model.order.AbstractOrderModel;

/**
 * Request mapper for COS sourcing request
 */
public interface CosSourcingRequestMapper
{
    /**
     * @param orderModel
     * @return {@link CosSourcingRequest}
     */
    CosSourcingRequest prepareCosSourcingRequest(AbstractOrderModel orderModel);
}
