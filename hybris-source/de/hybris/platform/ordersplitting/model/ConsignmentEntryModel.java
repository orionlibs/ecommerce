package de.hybris.platform.ordersplitting.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.sap.sapmodel.enums.ConsignmentEntryStatus;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ConsignmentEntryModel extends ItemModel
{
    public static final String _TYPECODE = "ConsignmentEntry";
    public static final String QUANTITY = "quantity";
    public static final String SHIPPEDQUANTITY = "shippedQuantity";
    public static final String ORDERENTRY = "orderEntry";
    public static final String CONSIGNMENT = "consignment";
    public static final String SAPORDERENTRYROWNUMBER = "sapOrderEntryRowNumber";
    public static final String STATUS = "status";


    public ConsignmentEntryModel()
    {
    }


    public ConsignmentEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConsignmentEntryModel(ConsignmentModel _consignment, AbstractOrderEntryModel _orderEntry, Long _quantity)
    {
        setConsignment(_consignment);
        setOrderEntry(_orderEntry);
        setQuantity(_quantity);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConsignmentEntryModel(ConsignmentModel _consignment, AbstractOrderEntryModel _orderEntry, ItemModel _owner, Long _quantity)
    {
        setConsignment(_consignment);
        setOrderEntry(_orderEntry);
        setOwner(_owner);
        setQuantity(_quantity);
    }


    @Accessor(qualifier = "consignment", type = Accessor.Type.GETTER)
    public ConsignmentModel getConsignment()
    {
        return (ConsignmentModel)getPersistenceContext().getPropertyValue("consignment");
    }


    @Accessor(qualifier = "orderEntry", type = Accessor.Type.GETTER)
    public AbstractOrderEntryModel getOrderEntry()
    {
        return (AbstractOrderEntryModel)getPersistenceContext().getPropertyValue("orderEntry");
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.GETTER)
    public Long getQuantity()
    {
        return (Long)getPersistenceContext().getPropertyValue("quantity");
    }


    @Accessor(qualifier = "sapOrderEntryRowNumber", type = Accessor.Type.GETTER)
    public int getSapOrderEntryRowNumber()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("sapOrderEntryRowNumber"));
    }


    @Accessor(qualifier = "shippedQuantity", type = Accessor.Type.GETTER)
    public Long getShippedQuantity()
    {
        return (Long)getPersistenceContext().getPropertyValue("shippedQuantity");
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public ConsignmentEntryStatus getStatus()
    {
        return (ConsignmentEntryStatus)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "consignment", type = Accessor.Type.SETTER)
    public void setConsignment(ConsignmentModel value)
    {
        getPersistenceContext().setPropertyValue("consignment", value);
    }


    @Accessor(qualifier = "orderEntry", type = Accessor.Type.SETTER)
    public void setOrderEntry(AbstractOrderEntryModel value)
    {
        getPersistenceContext().setPropertyValue("orderEntry", value);
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.SETTER)
    public void setQuantity(Long value)
    {
        getPersistenceContext().setPropertyValue("quantity", value);
    }


    @Accessor(qualifier = "sapOrderEntryRowNumber", type = Accessor.Type.SETTER)
    public void setSapOrderEntryRowNumber(int value)
    {
        getPersistenceContext().setPropertyValue("sapOrderEntryRowNumber", toObject(value));
    }


    @Accessor(qualifier = "shippedQuantity", type = Accessor.Type.SETTER)
    public void setShippedQuantity(Long value)
    {
        getPersistenceContext().setPropertyValue("shippedQuantity", value);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.SETTER)
    public void setStatus(ConsignmentEntryStatus value)
    {
        getPersistenceContext().setPropertyValue("status", value);
    }
}
