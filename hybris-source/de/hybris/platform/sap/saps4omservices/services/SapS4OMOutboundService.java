/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.saps4omservices.services;

import de.hybris.platform.sap.saps4omservices.exceptions.OutboundServiceException;
import de.hybris.platform.sap.saps4omservices.filter.dto.FilterData;
import de.hybris.platform.saps4omservices.dto.SAPS4OMData;
import de.hybris.platform.saps4omservices.dto.SAPS4OMOrders;
import de.hybris.platform.saps4omservices.dto.SAPS4OMRequestData;
import java.util.List;
import java.util.Map;

/**
 *  Outbound order service
 *
 */
public interface SapS4OMOutboundService
{
    /**
     * Create salesOrderSimulation by passing requestData.
     * @param destinationId from Consumed Destination
     * @param destinationTargetId from Consumed Destination
     * @param requestData as request payload
     * @return salesOrderSimulationData.
     * @throws de.hybris.platform.sap.saps4omservices.exceptions.OutboundServiceException
     */
    public SAPS4OMData simulateOrder(String destinationId, String destinationTargetId, SAPS4OMRequestData requestData) throws OutboundServiceException;


    /**
     * Get the orders by filtering via filterData.
     * @param destinationId from Consumed Destination
     * @param destinationTargetId from Consumed Destination
     * @param filters provided to fetch order
     * @return orders based on filter data
     * @throws de.hybris.platform.sap.saps4omservices.exceptions.OutboundServiceException
     */
    public SAPS4OMOrders fetchOrders(String destinationId, String destinationTargetId, Map<String, List<FilterData>> filters) throws OutboundServiceException;


    /**
     * Create  order by passing requestData.
     * @param destinationId from Consumed Destination
     * @param destinationTargetId from Consumed Destination
     * @param requestData as request payload
     * @return orderData from backend
     * @throws de.hybris.platform.sap.saps4omservices.exceptions.OutboundServiceException
     */
    public SAPS4OMData createOrder(String destinationId, String destinationTargetId, SAPS4OMRequestData requestData) throws OutboundServiceException;


    /**
     * Get the order details for an orderID
     * @param destinationId from Consumed Destination
     * @param destinationTargetId from Consumed Destination
     * @param orderID
     * @param filterData provided to fetch order
     * @return orderData orderData from backend
     * @throws de.hybris.platform.sap.saps4omservices.exceptions.OutboundServiceException
     */
    public SAPS4OMData fetchOrderDetails(String destinationId, String destinationTargetId, String orderID, Map<String, List<FilterData>> filterData) throws OutboundServiceException;
}
