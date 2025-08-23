/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.saps4omservices.services.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.sap.saps4omservices.exceptions.OutboundServiceException;
import de.hybris.platform.sap.saps4omservices.services.SapS4OMOutboundService;
import de.hybris.platform.sap.saps4omservices.services.SapS4OMProductAvailability;
import de.hybris.platform.sap.saps4omservices.services.SapS4OMRequestPayloadCreator;
import de.hybris.platform.sap.saps4omservices.services.SapS4OMResponseProcessor;
import de.hybris.platform.sap.saps4omservices.services.SapS4SalesOrderSimulationService;
import de.hybris.platform.saps4omservices.dto.SAPS4OMData;
import de.hybris.platform.saps4omservices.dto.SAPS4OMRequestData;
import de.hybris.platform.store.BaseStoreModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSapS4SalesOrderSimulationService implements SapS4SalesOrderSimulationService
{
    private static final String DEFAULT_PLANT = "defaultPlant";
    private static final String PRICE_INFO_MAP = "priceInfoMap";
    private static final String S4_ORDER_SIMULATE_DESTINATION = "s4omOrderSimulateDestination";
    private static final String S4_ORDER_SIMULATE_DESTINATION_TARGET_ID = "s4omOrderSimulateDestinationTarget";
    public static final String STOCK_AVAILABILITY_MAP = "sapStockAvailabilityMap";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSapS4SalesOrderSimulationService.class);
    private SapS4OMOutboundService sapS4OMOutboundService;
    private SapS4OMRequestPayloadCreator sapS4OMRequestPayloadCreator;
    private SapS4OMResponseProcessor sapS4OMResponseProcessor;


    @Override
    public List<PriceInformation> getPriceDetailsForProduct(ProductModel productModel) throws OutboundServiceException
    {
        LOG.debug("Method call getPriceDetailsForProduct");
        List<ProductModel> productModelList = new ArrayList<>();
        productModelList.add(productModel);
        SAPS4OMRequestData requestData = getSapS4OMRequestPayloadCreator().getPayloadForOrderSimulation(productModelList, false);
        SAPS4OMData salesOrderSimulationResponse = getSapS4OMOutboundService()
                        .simulateOrder(S4_ORDER_SIMULATE_DESTINATION,
                                        S4_ORDER_SIMULATE_DESTINATION_TARGET_ID, requestData);
        final Map<String, Object> simulationDetailsMap = getSapS4OMResponseProcessor().processOrderSimulationResponse(salesOrderSimulationResponse, productModel);
        LOG.debug("Price info map from backed {}", simulationDetailsMap.get(PRICE_INFO_MAP));
        return ((Map<String, List<PriceInformation>>)simulationDetailsMap.get(PRICE_INFO_MAP)).get(productModel.getCode());
    }


    @Override
    public Map<String, List<PriceInformation>> getPriceDetailsForProducts(List<ProductModel> productModels) throws OutboundServiceException
    {
        LOG.debug("Method call getPriceDetailsForProducts");
        SAPS4OMRequestData requestData = getSapS4OMRequestPayloadCreator().getPayloadForOrderSimulation(productModels, false);
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        SAPS4OMData salesOrderSimulationResponse = null;
        salesOrderSimulationResponse = getSapS4OMOutboundService().simulateOrder(
                        S4_ORDER_SIMULATE_DESTINATION, S4_ORDER_SIMULATE_DESTINATION_TARGET_ID,
                        requestData);
        final Map<String, Object> stockPriceMap = getSapS4OMResponseProcessor().processOrderSimulationResponse(salesOrderSimulationResponse, null);
        return (Map<String, List<PriceInformation>>)stockPriceMap.get(PRICE_INFO_MAP);
    }


    @Override
    public void setCartDetails(AbstractOrderModel cartModel) throws OutboundServiceException
    {
        LOG.debug("Method call setCartDetails");
        if(cartModel.getEntries().isEmpty())
        {
            LOG.debug("No entries found in the cart");
            return;
        }
        SAPS4OMRequestData requestData = getSapS4OMRequestPayloadCreator().getPayloadForOrderSimulation(cartModel);
        SAPS4OMData salesOrderSimulationResponse = getSapS4OMOutboundService()
                        .simulateOrder(S4_ORDER_SIMULATE_DESTINATION,
                                        S4_ORDER_SIMULATE_DESTINATION_TARGET_ID, requestData);
        getSapS4OMResponseProcessor().processOrderSimulationResponse(salesOrderSimulationResponse, cartModel);
    }


    @Override
    public Boolean checkCreditLimitExceeded(ItemModel cartModel, UserModel user) throws OutboundServiceException
    {
        LOG.debug("Method call checkCreditLimitExceeded");
        SAPS4OMRequestData requestData = getSapS4OMRequestPayloadCreator().getPayloadForOrderSimulation((AbstractOrderModel)cartModel);
        SAPS4OMData salesOrderSimulationResponse = getSapS4OMOutboundService()
                        .simulateOrder(S4_ORDER_SIMULATE_DESTINATION,
                                        S4_ORDER_SIMULATE_DESTINATION_TARGET_ID, requestData);
        if(salesOrderSimulationResponse.getResult().getCreditDetails() != null)
        {
            String creditStatus = salesOrderSimulationResponse.getResult().getCreditDetails()
                            .getCreditCheckStatus();
            return "B".equalsIgnoreCase(creditStatus);
        }
        LOG.debug("Credit check limit is disabled");
        return false;
    }


    @Override
    public SapS4OMProductAvailability getStockAvailability(ProductModel productModel, BaseStoreModel baseStore) throws OutboundServiceException
    {
        LOG.debug("Method call getStockAvailability");
        List<ProductModel> productModelList = new ArrayList<>();
        productModelList.add(productModel);
        Map<String, SapS4OMProductAvailability> stockLevelMap;
        Map<String, Object> responseMap;
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        SAPS4OMRequestData requestData = getSapS4OMRequestPayloadCreator().getPayloadForOrderSimulation(productModelList, true);
        SAPS4OMData responseData = getSapS4OMOutboundService().simulateOrder(
                        S4_ORDER_SIMULATE_DESTINATION, S4_ORDER_SIMULATE_DESTINATION_TARGET_ID,
                        requestData);
        responseMap = getSapS4OMResponseProcessor().processOrderSimulationResponse(responseData, productModel);
        stockLevelMap = (Map<String, SapS4OMProductAvailability>)responseMap.get(STOCK_AVAILABILITY_MAP);
        return stockLevelMap.get((productModel.getSapPlant() != null) ? productModel.getSapPlant().getCode()
                        : DEFAULT_PLANT);
    }


    protected SapS4OMOutboundService getSapS4OMOutboundService()
    {
        return sapS4OMOutboundService;
    }


    public void setSapS4OMOutboundService(SapS4OMOutboundService sapS4OMOutboundService)
    {
        this.sapS4OMOutboundService = sapS4OMOutboundService;
    }


    public SapS4OMResponseProcessor getSapS4OMResponseProcessor()
    {
        return sapS4OMResponseProcessor;
    }


    public void setSapS4OMResponseProcessor(SapS4OMResponseProcessor sapS4OMResponseProcessor)
    {
        this.sapS4OMResponseProcessor = sapS4OMResponseProcessor;
    }


    public SapS4OMRequestPayloadCreator getSapS4OMRequestPayloadCreator()
    {
        return sapS4OMRequestPayloadCreator;
    }


    public void setSapS4OMRequestPayloadCreator(SapS4OMRequestPayloadCreator sapS4OMRequestPayloadCreator)
    {
        this.sapS4OMRequestPayloadCreator = sapS4OMRequestPayloadCreator;
    }
}
