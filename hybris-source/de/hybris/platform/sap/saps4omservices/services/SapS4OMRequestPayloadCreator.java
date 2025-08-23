/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.saps4omservices.services;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.saps4omservices.dto.SAPS4OMRequestData;
import java.util.List;

public interface SapS4OMRequestPayloadCreator
{
    public SAPS4OMRequestData getPayloadForOrderCreation(OrderModel order);


    public SAPS4OMRequestData getPayloadForOrderSimulation(AbstractOrderModel cart);


    public SAPS4OMRequestData getPayloadForOrderSimulation(List<ProductModel> products, boolean checkAvailability);
}
