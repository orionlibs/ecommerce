package de.hybris.platform.ordermodify.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class OrderModificationRecordModel extends ItemModel
{
    public static final String _TYPECODE = "OrderModificationRecord";
    public static final String _ORDER2ORDERMODIFICATIONRECORDS = "Order2OrderModificationRecords";
    public static final String INPROGRESS = "inProgress";
    public static final String IDENTIFIER = "identifier";
    public static final String ORDER = "order";
    public static final String MODIFICATIONRECORDENTRIES = "modificationRecordEntries";


    public OrderModificationRecordModel()
    {
    }


    public OrderModificationRecordModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderModificationRecordModel(boolean _inProgress, OrderModel _order)
    {
        setInProgress(_inProgress);
        setOrder(_order);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderModificationRecordModel(boolean _inProgress, OrderModel _order, ItemModel _owner)
    {
        setInProgress(_inProgress);
        setOrder(_order);
        setOwner(_owner);
    }


    @Accessor(qualifier = "identifier", type = Accessor.Type.GETTER)
    public String getIdentifier()
    {
        return (String)getPersistenceContext().getPropertyValue("identifier");
    }


    @Accessor(qualifier = "modificationRecordEntries", type = Accessor.Type.GETTER)
    public Collection<OrderModificationRecordEntryModel> getModificationRecordEntries()
    {
        return (Collection<OrderModificationRecordEntryModel>)getPersistenceContext().getPropertyValue("modificationRecordEntries");
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public OrderModel getOrder()
    {
        return (OrderModel)getPersistenceContext().getPropertyValue("order");
    }


    @Accessor(qualifier = "inProgress", type = Accessor.Type.GETTER)
    public boolean isInProgress()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("inProgress"));
    }


    @Accessor(qualifier = "identifier", type = Accessor.Type.SETTER)
    public void setIdentifier(String value)
    {
        getPersistenceContext().setPropertyValue("identifier", value);
    }


    @Accessor(qualifier = "inProgress", type = Accessor.Type.SETTER)
    public void setInProgress(boolean value)
    {
        getPersistenceContext().setPropertyValue("inProgress", toObject(value));
    }


    @Accessor(qualifier = "modificationRecordEntries", type = Accessor.Type.SETTER)
    public void setModificationRecordEntries(Collection<OrderModificationRecordEntryModel> value)
    {
        getPersistenceContext().setPropertyValue("modificationRecordEntries", value);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(OrderModel value)
    {
        getPersistenceContext().setPropertyValue("order", value);
    }
}
