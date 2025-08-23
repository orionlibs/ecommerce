/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.saps4omservices.strategies;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.order.impl.DefaultCommercePlaceOrderStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.sap.saps4omservices.exceptions.OutboundServiceException;
import de.hybris.platform.sap.saps4omservices.services.SapS4OMOutboundService;
import de.hybris.platform.sap.saps4omservices.services.SapS4OMRequestPayloadCreator;
import de.hybris.platform.sap.saps4omservices.services.SapS4OMResponseProcessor;
import de.hybris.platform.sap.saps4omservices.services.SapS4OrderManagementConfigService;
import de.hybris.platform.saps4omservices.dto.SAPS4OMData;
import de.hybris.platform.saps4omservices.dto.SAPS4OMRequestData;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class S4PlaceOrderStrategy extends DefaultCommercePlaceOrderStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(S4PlaceOrderStrategy.class);
    private SapS4OrderManagementConfigService sapS4OrderManagementConfigService;
    private SapS4OMOutboundService sapS4omOutboundService;
    private SapS4OMRequestPayloadCreator sapS4OMRequestPayloadCreator;
    private SapS4OMResponseProcessor sapS4OMResponseProcessor;
    private CartService cartService;
    private List<CommercePlaceOrderMethodHook> sapS4PlaceOrderMethodHooks;
    private static final String S4_ORDER_DESTINATION = "s4omOrderDestination";
    private static final String S4_DESTINATION_TARGET = "s4omOrderDestinationTarget";


    @Override
    public CommerceOrderResult placeOrder(CommerceCheckoutParameter parameter) throws InvalidCartException
    {
        LOG.debug("Entering: S4PlaceOrderStrategy.placeOrder() method");
        if(!getSapS4OrderManagementConfigService().isS4SynchronousOrderEnabled())
        {
            return super.placeOrder(parameter);
        }
        final CartModel cartModel = parameter.getCart();
        validateParameterNotNull(cartModel, "Cart model cannot be null");
        final CustomerModel customer = (CustomerModel)cartModel.getUser();
        validateParameterNotNull(customer, "Customer model cannot be null");
        beforeS4PlaceOrder(parameter);
        final OrderModel orderModel = getOrderService().createOrderFromCart(cartModel);
        SAPS4OMRequestData requestData = getSapS4OMRequestPayloadCreator().getPayloadForOrderCreation(orderModel);
        SAPS4OMData responseData = null;
        try
        {
            responseData = getSapS4omOutboundService().createOrder(S4_ORDER_DESTINATION, S4_DESTINATION_TARGET, requestData);
        }
        catch(OutboundServiceException e)
        {
            throw new UnknownIdentifierException(e.getMessage());
        }
        getSapS4OMResponseProcessor().processOrderCreationResponse(responseData, orderModel);
        final CommerceOrderResult result = new CommerceOrderResult();
        result.setOrder(orderModel);
        getOrderService().submitOrder(orderModel);
        getCartService().removeSessionCart();
        afterS4PlaceOrder(parameter, result);
        return result;
    }


    protected void beforeS4PlaceOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException
    {
        if(getCommercePlaceOrderMethodHooks() != null && parameter.isEnableHooks())
        {
            for(final CommercePlaceOrderMethodHook commercePlaceOrderMethodHook : getSapS4PlaceOrderMethodHooks())
            {
                commercePlaceOrderMethodHook.beforePlaceOrder(parameter);
            }
        }
    }


    protected void afterS4PlaceOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult orderModel) throws InvalidCartException
    {
        if(getCommercePlaceOrderMethodHooks() != null && parameter.isEnableHooks())
        {
            for(final CommercePlaceOrderMethodHook commercePlaceOrderMethodHook : getSapS4PlaceOrderMethodHooks())
            {
                commercePlaceOrderMethodHook.afterPlaceOrder(parameter, orderModel);
            }
        }
    }


    public SapS4OrderManagementConfigService getSapS4OrderManagementConfigService()
    {
        return sapS4OrderManagementConfigService;
    }


    public void setSapS4OrderManagementConfigService(SapS4OrderManagementConfigService sapS4OrderManagementConfigService)
    {
        this.sapS4OrderManagementConfigService = sapS4OrderManagementConfigService;
    }


    public SapS4OMOutboundService getSapS4omOutboundService()
    {
        return sapS4omOutboundService;
    }


    public void setSapS4omOutboundService(SapS4OMOutboundService sapS4omOutboundService)
    {
        this.sapS4omOutboundService = sapS4omOutboundService;
    }


    public SapS4OMRequestPayloadCreator getSapS4OMRequestPayloadCreator()
    {
        return sapS4OMRequestPayloadCreator;
    }


    public void setSapS4OMRequestPayloadCreator(SapS4OMRequestPayloadCreator sapS4OMRequestPayloadCreator)
    {
        this.sapS4OMRequestPayloadCreator = sapS4OMRequestPayloadCreator;
    }


    public SapS4OMResponseProcessor getSapS4OMResponseProcessor()
    {
        return sapS4OMResponseProcessor;
    }


    public void setSapS4OMResponseProcessor(SapS4OMResponseProcessor sapS4OMResponseProcessor)
    {
        this.sapS4OMResponseProcessor = sapS4OMResponseProcessor;
    }


    public CartService getCartService()
    {
        return cartService;
    }


    public void setCartService(CartService cartService)
    {
        this.cartService = cartService;
    }


    public List<CommercePlaceOrderMethodHook> getSapS4PlaceOrderMethodHooks()
    {
        return sapS4PlaceOrderMethodHooks;
    }


    public void setSapS4PlaceOrderMethodHooks(List<CommercePlaceOrderMethodHook> sapS4PlaceOrderMethodHooks)
    {
        this.sapS4PlaceOrderMethodHooks = sapS4PlaceOrderMethodHooks;
    }
}
