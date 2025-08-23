package de.hybris.platform.ordermodify.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;

public class OrderModificationRecordEntryModel extends ItemModel
{
    public static final String _TYPECODE = "OrderModificationRecordEntry";
    public static final String _ORDERMODIFICATIONRECORD2ORDERMODIFICATIONRECORDENTRIES = "OrderModificationRecord2OrderModificationRecordEntries";
    public static final String CODE = "code";
    public static final String TIMESTAMP = "timestamp";
    public static final String STATUS = "status";
    public static final String ORIGINALVERSION = "originalVersion";
    public static final String PRINCIPAL = "principal";
    public static final String FAILEDMESSAGE = "failedMessage";
    public static final String NOTES = "notes";
    public static final String MODIFICATIONRECORD = "modificationRecord";
    public static final String ORDERENTRIESMODIFICATIONENTRIES = "orderEntriesModificationEntries";


    public OrderModificationRecordEntryModel()
    {
    }


    public OrderModificationRecordEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderModificationRecordEntryModel(String _code, OrderModificationRecordModel _modificationRecord, OrderHistoryEntryModel _originalVersion, OrderModificationEntryStatus _status, Date _timestamp)
    {
        setCode(_code);
        setModificationRecord(_modificationRecord);
        setOriginalVersion(_originalVersion);
        setStatus(_status);
        setTimestamp(_timestamp);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderModificationRecordEntryModel(String _code, OrderModificationRecordModel _modificationRecord, OrderHistoryEntryModel _originalVersion, ItemModel _owner, OrderModificationEntryStatus _status, Date _timestamp)
    {
        setCode(_code);
        setModificationRecord(_modificationRecord);
        setOriginalVersion(_originalVersion);
        setOwner(_owner);
        setStatus(_status);
        setTimestamp(_timestamp);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "failedMessage", type = Accessor.Type.GETTER)
    public String getFailedMessage()
    {
        return (String)getPersistenceContext().getPropertyValue("failedMessage");
    }


    @Accessor(qualifier = "modificationRecord", type = Accessor.Type.GETTER)
    public OrderModificationRecordModel getModificationRecord()
    {
        return (OrderModificationRecordModel)getPersistenceContext().getPropertyValue("modificationRecord");
    }


    @Accessor(qualifier = "notes", type = Accessor.Type.GETTER)
    public String getNotes()
    {
        return (String)getPersistenceContext().getPropertyValue("notes");
    }


    @Accessor(qualifier = "orderEntriesModificationEntries", type = Accessor.Type.GETTER)
    public Collection<OrderEntryModificationRecordEntryModel> getOrderEntriesModificationEntries()
    {
        return (Collection<OrderEntryModificationRecordEntryModel>)getPersistenceContext().getPropertyValue("orderEntriesModificationEntries");
    }


    @Accessor(qualifier = "originalVersion", type = Accessor.Type.GETTER)
    public OrderHistoryEntryModel getOriginalVersion()
    {
        return (OrderHistoryEntryModel)getPersistenceContext().getPropertyValue("originalVersion");
    }


    @Accessor(qualifier = "principal", type = Accessor.Type.GETTER)
    public PrincipalModel getPrincipal()
    {
        return (PrincipalModel)getPersistenceContext().getPropertyValue("principal");
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public OrderModificationEntryStatus getStatus()
    {
        return (OrderModificationEntryStatus)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "timestamp", type = Accessor.Type.GETTER)
    public Date getTimestamp()
    {
        return (Date)getPersistenceContext().getPropertyValue("timestamp");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "failedMessage", type = Accessor.Type.SETTER)
    public void setFailedMessage(String value)
    {
        getPersistenceContext().setPropertyValue("failedMessage", value);
    }


    @Accessor(qualifier = "modificationRecord", type = Accessor.Type.SETTER)
    public void setModificationRecord(OrderModificationRecordModel value)
    {
        getPersistenceContext().setPropertyValue("modificationRecord", value);
    }


    @Accessor(qualifier = "notes", type = Accessor.Type.SETTER)
    public void setNotes(String value)
    {
        getPersistenceContext().setPropertyValue("notes", value);
    }


    @Accessor(qualifier = "orderEntriesModificationEntries", type = Accessor.Type.SETTER)
    public void setOrderEntriesModificationEntries(Collection<OrderEntryModificationRecordEntryModel> value)
    {
        getPersistenceContext().setPropertyValue("orderEntriesModificationEntries", value);
    }


    @Accessor(qualifier = "originalVersion", type = Accessor.Type.SETTER)
    public void setOriginalVersion(OrderHistoryEntryModel value)
    {
        getPersistenceContext().setPropertyValue("originalVersion", value);
    }


    @Accessor(qualifier = "principal", type = Accessor.Type.SETTER)
    public void setPrincipal(PrincipalModel value)
    {
        getPersistenceContext().setPropertyValue("principal", value);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.SETTER)
    public void setStatus(OrderModificationEntryStatus value)
    {
        getPersistenceContext().setPropertyValue("status", value);
    }


    @Accessor(qualifier = "timestamp", type = Accessor.Type.SETTER)
    public void setTimestamp(Date value)
    {
        getPersistenceContext().setPropertyValue("timestamp", value);
    }
}
