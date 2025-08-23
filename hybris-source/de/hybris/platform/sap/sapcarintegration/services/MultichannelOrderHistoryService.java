/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapcarintegration.services;

import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.sap.sapcarintegration.data.CarMultichannelOrderHistoryData;
import java.util.List;

/**
 * Interface to provide Order History data from a Multichannel configured CAR instance
 */
public interface MultichannelOrderHistoryService extends CarOrderHistoryService
{
    /**
     * reads the all transactions (hybris + pos + SD) for a given customer
     *
     * @param customerNumber
     * @param paginationData
     * @return {@link List<CarMultichannelOrderHistoryData>}
     */
    abstract List<CarMultichannelOrderHistoryData> readMultiChannelTransactionsForCustomer(String customerNumber,
                    PaginationData paginationData);


    /**
     * read detail for sales document
     *
     * @param customerNumber
     * @param transactionNumber
     * @return {@link CarMultichannelOrderHistoryData}
     */
    abstract CarMultichannelOrderHistoryData readSalesDocumentDetails(String customerNumber, String transactionNumber);
}
