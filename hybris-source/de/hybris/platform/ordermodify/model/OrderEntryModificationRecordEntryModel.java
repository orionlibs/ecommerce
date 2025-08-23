package de.hybris.platform.ordermodify.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OrderEntryModificationRecordEntryModel extends ItemModel
{
    public static final String _TYPECODE = "OrderEntryModificationRecordEntry";
    public static final String _ORDERMODIFICATIONRECORDENTRY2ORDERENTRYMODIFICATIONRECORDENTRY = "OrderModificationRecordEntry2OrderEntryModificationRecordEntry";
    public static final String CODE = "code";
    public static final String NOTES = "notes";
    public static final String ORIGINALORDERENTRY = "originalOrderEntry";
    public static final String ORDERENTRY = "orderEntry";
    public static final String MODIFICATIONRECORDENTRY = "modificationRecordEntry";


    public OrderEntryModificationRecordEntryModel()
    {
    }


    public OrderEntryModificationRecordEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderEntryModificationRecordEntryModel(String _code, OrderModificationRecordEntryModel _modificationRecordEntry)
    {
        setCode(_code);
        setModificationRecordEntry(_modificationRecordEntry);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderEntryModificationRecordEntryModel(String _code, OrderModificationRecordEntryModel _modificationRecordEntry, ItemModel _owner)
    {
        setCode(_code);
        setModificationRecordEntry(_modificationRecordEntry);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "modificationRecordEntry", type = Accessor.Type.GETTER)
    public OrderModificationRecordEntryModel getModificationRecordEntry()
    {
        return (OrderModificationRecordEntryModel)getPersistenceContext().getPropertyValue("modificationRecordEntry");
    }


    @Accessor(qualifier = "notes", type = Accessor.Type.GETTER)
    public String getNotes()
    {
        return (String)getPersistenceContext().getPropertyValue("notes");
    }


    @Accessor(qualifier = "orderEntry", type = Accessor.Type.GETTER)
    public OrderEntryModel getOrderEntry()
    {
        return (OrderEntryModel)getPersistenceContext().getPropertyValue("orderEntry");
    }


    @Accessor(qualifier = "originalOrderEntry", type = Accessor.Type.GETTER)
    public OrderEntryModel getOriginalOrderEntry()
    {
        return (OrderEntryModel)getPersistenceContext().getPropertyValue("originalOrderEntry");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "modificationRecordEntry", type = Accessor.Type.SETTER)
    public void setModificationRecordEntry(OrderModificationRecordEntryModel value)
    {
        getPersistenceContext().setPropertyValue("modificationRecordEntry", value);
    }


    @Accessor(qualifier = "notes", type = Accessor.Type.SETTER)
    public void setNotes(String value)
    {
        getPersistenceContext().setPropertyValue("notes", value);
    }


    @Accessor(qualifier = "orderEntry", type = Accessor.Type.SETTER)
    public void setOrderEntry(OrderEntryModel value)
    {
        getPersistenceContext().setPropertyValue("orderEntry", value);
    }


    @Accessor(qualifier = "originalOrderEntry", type = Accessor.Type.SETTER)
    public void setOriginalOrderEntry(OrderEntryModel value)
    {
        getPersistenceContext().setPropertyValue("originalOrderEntry", value);
    }
}
