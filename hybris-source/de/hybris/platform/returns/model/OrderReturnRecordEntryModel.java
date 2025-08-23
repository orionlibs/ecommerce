package de.hybris.platform.returns.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class OrderReturnRecordEntryModel extends OrderModificationRecordEntryModel
{
    public static final String _TYPECODE = "OrderReturnRecordEntry";
    public static final String RETURNSTATUS = "returnStatus";
    public static final String EXPECTEDQUANTITY = "expectedQuantity";


    public OrderReturnRecordEntryModel()
    {
    }


    public OrderReturnRecordEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderReturnRecordEntryModel(String _code, OrderModificationRecordModel _modificationRecord, OrderHistoryEntryModel _originalVersion, OrderModificationEntryStatus _status, Date _timestamp)
    {
        setCode(_code);
        setModificationRecord(_modificationRecord);
        setOriginalVersion(_originalVersion);
        setStatus(_status);
        setTimestamp(_timestamp);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderReturnRecordEntryModel(String _code, OrderModificationRecordModel _modificationRecord, OrderHistoryEntryModel _originalVersion, ItemModel _owner, OrderModificationEntryStatus _status, Date _timestamp)
    {
        setCode(_code);
        setModificationRecord(_modificationRecord);
        setOriginalVersion(_originalVersion);
        setOwner(_owner);
        setStatus(_status);
        setTimestamp(_timestamp);
    }


    @Accessor(qualifier = "expectedQuantity", type = Accessor.Type.GETTER)
    public Long getExpectedQuantity()
    {
        return (Long)getPersistenceContext().getPropertyValue("expectedQuantity");
    }


    @Accessor(qualifier = "returnStatus", type = Accessor.Type.GETTER)
    public ReturnStatus getReturnStatus()
    {
        return (ReturnStatus)getPersistenceContext().getPropertyValue("returnStatus");
    }


    @Accessor(qualifier = "expectedQuantity", type = Accessor.Type.SETTER)
    public void setExpectedQuantity(Long value)
    {
        getPersistenceContext().setPropertyValue("expectedQuantity", value);
    }


    @Accessor(qualifier = "returnStatus", type = Accessor.Type.SETTER)
    public void setReturnStatus(ReturnStatus value)
    {
        getPersistenceContext().setPropertyValue("returnStatus", value);
    }
}
