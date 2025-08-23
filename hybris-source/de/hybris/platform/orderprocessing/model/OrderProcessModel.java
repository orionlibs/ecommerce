package de.hybris.platform.orderprocessing.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class OrderProcessModel extends BusinessProcessModel
{
    public static final String _TYPECODE = "OrderProcess";
    public static final String _ORDER2ORDERPROCESS = "Order2OrderProcess";
    public static final String ORDER = "order";
    public static final String CONSIGNMENTPROCESSES = "consignmentProcesses";
    public static final String SENDORDERRETRYCOUNT = "sendOrderRetryCount";


    public OrderProcessModel()
    {
    }


    public OrderProcessModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderProcessModel(String _code, String _processDefinitionName)
    {
        setCode(_code);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderProcessModel(String _code, ItemModel _owner, String _processDefinitionName)
    {
        setCode(_code);
        setOwner(_owner);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Accessor(qualifier = "consignmentProcesses", type = Accessor.Type.GETTER)
    public Collection<ConsignmentProcessModel> getConsignmentProcesses()
    {
        return (Collection<ConsignmentProcessModel>)getPersistenceContext().getPropertyValue("consignmentProcesses");
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public OrderModel getOrder()
    {
        return (OrderModel)getPersistenceContext().getPropertyValue("order");
    }


    @Accessor(qualifier = "sendOrderRetryCount", type = Accessor.Type.GETTER)
    public int getSendOrderRetryCount()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("sendOrderRetryCount"));
    }


    @Accessor(qualifier = "consignmentProcesses", type = Accessor.Type.SETTER)
    public void setConsignmentProcesses(Collection<ConsignmentProcessModel> value)
    {
        getPersistenceContext().setPropertyValue("consignmentProcesses", value);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(OrderModel value)
    {
        getPersistenceContext().setPropertyValue("order", value);
    }


    @Accessor(qualifier = "sendOrderRetryCount", type = Accessor.Type.SETTER)
    public void setSendOrderRetryCount(int value)
    {
        getPersistenceContext().setPropertyValue("sendOrderRetryCount", toObject(value));
    }
}
