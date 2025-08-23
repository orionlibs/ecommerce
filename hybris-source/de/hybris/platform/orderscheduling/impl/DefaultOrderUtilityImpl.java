package de.hybris.platform.orderscheduling.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.orderscheduling.OrderUtility;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Date;
import java.util.UUID;
import javax.annotation.Resource;

public class DefaultOrderUtilityImpl implements OrderUtility
{
    @Resource
    private ModelService modelService;
    @Resource
    private OrderService orderService;
    @Resource
    private CalculationService calculationService;


    public OrderModel createOrderFromCart(CartModel cart, AddressModel deliveryAddress, AddressModel paymentAddress, PaymentInfoModel paymentInfo) throws InvalidCartException
    {
        if(cart.getEntries().isEmpty())
        {
            return null;
        }
        OrderModel order = getOrderService().placeOrder(cart, deliveryAddress, paymentAddress, paymentInfo);
        order.setDate(new Date());
        order.setCode(UUID.randomUUID().toString());
        getModelService().save(order);
        runScheduledOrder(order);
        return order;
    }


    public OrderModel createOrderFromOrderTemplate(OrderModel template)
    {
        OrderModel order = (OrderModel)getModelService().clone(template);
        UUID idOne = UUID.randomUUID();
        order.setVersionID(idOne.toString());
        getModelService().save(order);
        runScheduledOrder(order);
        return order;
    }


    public OrderModel runScheduledOrder(OrderModel order)
    {
        try
        {
            getCalculationService().calculate((AbstractOrderModel)order);
        }
        catch(CalculationException e)
        {
            throw new SystemException("Could not calculate order [" + order.getCode() + "] due to : " + e.getMessage(), e);
        }
        runOrder(order);
        return order;
    }


    public void runOrder(OrderModel order)
    {
    }


    protected CalculationService getCalculationService()
    {
        return this.calculationService;
    }


    public void setCalculationService(CalculationService calculationService)
    {
        this.calculationService = calculationService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public void setOrderService(OrderService orderService)
    {
        this.orderService = orderService;
    }


    public OrderService getOrderService()
    {
        return this.orderService;
    }
}
