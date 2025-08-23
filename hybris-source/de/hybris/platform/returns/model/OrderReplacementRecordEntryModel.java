package de.hybris.platform.returns.model;

import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class OrderReplacementRecordEntryModel extends OrderReturnRecordEntryModel
{
    public static final String _TYPECODE = "OrderReplacementRecordEntry";


    public OrderReplacementRecordEntryModel()
    {
    }


    public OrderReplacementRecordEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderReplacementRecordEntryModel(String _code, OrderModificationRecordModel _modificationRecord, OrderHistoryEntryModel _originalVersion, OrderModificationEntryStatus _status, Date _timestamp)
    {
        setCode(_code);
        setModificationRecord(_modificationRecord);
        setOriginalVersion(_originalVersion);
        setStatus(_status);
        setTimestamp(_timestamp);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderReplacementRecordEntryModel(String _code, OrderModificationRecordModel _modificationRecord, OrderHistoryEntryModel _originalVersion, ItemModel _owner, OrderModificationEntryStatus _status, Date _timestamp)
    {
        setCode(_code);
        setModificationRecord(_modificationRecord);
        setOriginalVersion(_originalVersion);
        setOwner(_owner);
        setStatus(_status);
        setTimestamp(_timestamp);
    }
}
