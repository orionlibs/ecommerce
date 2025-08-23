package de.hybris.platform.ordercancel.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OrderEntryCancelRecordEntryModel extends OrderEntryModificationRecordEntryModel
{
    public static final String _TYPECODE = "OrderEntryCancelRecordEntry";
    public static final String CANCELREQUESTQUANTITY = "cancelRequestQuantity";
    public static final String CANCELLEDQUANTITY = "cancelledQuantity";
    public static final String CANCELREASON = "cancelReason";


    public OrderEntryCancelRecordEntryModel()
    {
    }


    public OrderEntryCancelRecordEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderEntryCancelRecordEntryModel(String _code, OrderModificationRecordEntryModel _modificationRecordEntry)
    {
        setCode(_code);
        setModificationRecordEntry(_modificationRecordEntry);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderEntryCancelRecordEntryModel(String _code, OrderModificationRecordEntryModel _modificationRecordEntry, ItemModel _owner)
    {
        setCode(_code);
        setModificationRecordEntry(_modificationRecordEntry);
        setOwner(_owner);
    }


    @Accessor(qualifier = "cancelledQuantity", type = Accessor.Type.GETTER)
    public Integer getCancelledQuantity()
    {
        return (Integer)getPersistenceContext().getPropertyValue("cancelledQuantity");
    }


    @Accessor(qualifier = "cancelReason", type = Accessor.Type.GETTER)
    public CancelReason getCancelReason()
    {
        return (CancelReason)getPersistenceContext().getPropertyValue("cancelReason");
    }


    @Accessor(qualifier = "cancelRequestQuantity", type = Accessor.Type.GETTER)
    public Integer getCancelRequestQuantity()
    {
        return (Integer)getPersistenceContext().getPropertyValue("cancelRequestQuantity");
    }


    @Accessor(qualifier = "cancelledQuantity", type = Accessor.Type.SETTER)
    public void setCancelledQuantity(Integer value)
    {
        getPersistenceContext().setPropertyValue("cancelledQuantity", value);
    }


    @Accessor(qualifier = "cancelReason", type = Accessor.Type.SETTER)
    public void setCancelReason(CancelReason value)
    {
        getPersistenceContext().setPropertyValue("cancelReason", value);
    }


    @Accessor(qualifier = "cancelRequestQuantity", type = Accessor.Type.SETTER)
    public void setCancelRequestQuantity(Integer value)
    {
        getPersistenceContext().setPropertyValue("cancelRequestQuantity", value);
    }
}
