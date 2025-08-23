/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.saps4omservices.services;

import de.hybris.platform.commerceservices.model.FutureStockModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import java.util.List;

public interface SapS4OMProductAvailability
{
    Long getCurrentStockLevel();


    List<FutureStockModel> getFutureAvailability();


    StockLevelModel getStockLevelModel();
}
