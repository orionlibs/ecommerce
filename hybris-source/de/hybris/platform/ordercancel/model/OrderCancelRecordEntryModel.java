package de.hybris.platform.ordercancel.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.basecommerce.enums.OrderCancelEntryStatus;
import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class OrderCancelRecordEntryModel extends OrderModificationRecordEntryModel
{
    public static final String _TYPECODE = "OrderCancelRecordEntry";
    public static final String REFUSEDMESSAGE = "refusedMessage";
    public static final String CANCELRESULT = "cancelResult";
    public static final String CANCELREASON = "cancelReason";


    public OrderCancelRecordEntryModel()
    {
    }


    public OrderCancelRecordEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderCancelRecordEntryModel(String _code, OrderModificationRecordModel _modificationRecord, OrderHistoryEntryModel _originalVersion, OrderModificationEntryStatus _status, Date _timestamp)
    {
        setCode(_code);
        setModificationRecord(_modificationRecord);
        setOriginalVersion(_originalVersion);
        setStatus(_status);
        setTimestamp(_timestamp);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderCancelRecordEntryModel(String _code, OrderModificationRecordModel _modificationRecord, OrderHistoryEntryModel _originalVersion, ItemModel _owner, OrderModificationEntryStatus _status, Date _timestamp)
    {
        setCode(_code);
        setModificationRecord(_modificationRecord);
        setOriginalVersion(_originalVersion);
        setOwner(_owner);
        setStatus(_status);
        setTimestamp(_timestamp);
    }


    @Accessor(qualifier = "cancelReason", type = Accessor.Type.GETTER)
    public CancelReason getCancelReason()
    {
        return (CancelReason)getPersistenceContext().getPropertyValue("cancelReason");
    }


    @Accessor(qualifier = "cancelResult", type = Accessor.Type.GETTER)
    public OrderCancelEntryStatus getCancelResult()
    {
        return (OrderCancelEntryStatus)getPersistenceContext().getPropertyValue("cancelResult");
    }


    @Accessor(qualifier = "refusedMessage", type = Accessor.Type.GETTER)
    public String getRefusedMessage()
    {
        return (String)getPersistenceContext().getPropertyValue("refusedMessage");
    }


    @Accessor(qualifier = "cancelReason", type = Accessor.Type.SETTER)
    public void setCancelReason(CancelReason value)
    {
        getPersistenceContext().setPropertyValue("cancelReason", value);
    }


    @Accessor(qualifier = "cancelResult", type = Accessor.Type.SETTER)
    public void setCancelResult(OrderCancelEntryStatus value)
    {
        getPersistenceContext().setPropertyValue("cancelResult", value);
    }


    @Accessor(qualifier = "refusedMessage", type = Accessor.Type.SETTER)
    public void setRefusedMessage(String value)
    {
        getPersistenceContext().setPropertyValue("refusedMessage", value);
    }
}
