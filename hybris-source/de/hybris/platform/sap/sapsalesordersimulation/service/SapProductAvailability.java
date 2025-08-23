/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapsalesordersimulation.service;

import de.hybris.platform.commerceservices.model.FutureStockModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import java.util.List;

public interface SapProductAvailability
{
    Long getCurrentStockLevel();


    List<FutureStockModel> getFutureAvailability();


    StockLevelModel getStockLevelModel();
}
