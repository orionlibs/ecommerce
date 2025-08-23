package de.hybris.platform.sap.sapmodel.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.sap.sapmodel.enums.SAPOrderStatus;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

public class SAPOrderModel extends ItemModel
{
    public static final String _TYPECODE = "SAPOrder";
    public static final String _ORDER2SAPORDER = "Order2SapOrder";
    public static final String _CONSIGNMENTSAPORDERRELATION = "ConsignmentSapOrderRelation";
    public static final String CODE = "code";
    public static final String SAPORDERSTATUS = "sapOrderStatus";
    public static final String ORDERPOS = "orderPOS";
    public static final String ORDER = "order";
    public static final String CONSIGNMENTS = "consignments";


    public SAPOrderModel()
    {
    }


    public SAPOrderModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPOrderModel(OrderModel _order)
    {
        setOrder(_order);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPOrderModel(OrderModel _order, ItemModel _owner)
    {
        setOrder(_order);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "consignments", type = Accessor.Type.GETTER)
    public Set<ConsignmentModel> getConsignments()
    {
        return (Set<ConsignmentModel>)getPersistenceContext().getPropertyValue("consignments");
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public OrderModel getOrder()
    {
        return (OrderModel)getPersistenceContext().getPropertyValue("order");
    }


    @Accessor(qualifier = "sapOrderStatus", type = Accessor.Type.GETTER)
    public SAPOrderStatus getSapOrderStatus()
    {
        return (SAPOrderStatus)getPersistenceContext().getPropertyValue("sapOrderStatus");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "consignments", type = Accessor.Type.SETTER)
    public void setConsignments(Set<ConsignmentModel> value)
    {
        getPersistenceContext().setPropertyValue("consignments", value);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(OrderModel value)
    {
        getPersistenceContext().setPropertyValue("order", value);
    }


    @Accessor(qualifier = "sapOrderStatus", type = Accessor.Type.SETTER)
    public void setSapOrderStatus(SAPOrderStatus value)
    {
        getPersistenceContext().setPropertyValue("sapOrderStatus", value);
    }
}
