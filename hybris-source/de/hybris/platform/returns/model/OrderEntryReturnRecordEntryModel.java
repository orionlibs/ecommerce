package de.hybris.platform.returns.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OrderEntryReturnRecordEntryModel extends OrderEntryModificationRecordEntryModel
{
    public static final String _TYPECODE = "OrderEntryReturnRecordEntry";
    public static final String EXPECTEDQUANTITY = "expectedQuantity";
    public static final String RETURNEDQUANTITY = "returnedQuantity";


    public OrderEntryReturnRecordEntryModel()
    {
    }


    public OrderEntryReturnRecordEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderEntryReturnRecordEntryModel(String _code, OrderModificationRecordEntryModel _modificationRecordEntry)
    {
        setCode(_code);
        setModificationRecordEntry(_modificationRecordEntry);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderEntryReturnRecordEntryModel(String _code, OrderModificationRecordEntryModel _modificationRecordEntry, ItemModel _owner)
    {
        setCode(_code);
        setModificationRecordEntry(_modificationRecordEntry);
        setOwner(_owner);
    }


    @Accessor(qualifier = "expectedQuantity", type = Accessor.Type.GETTER)
    public Long getExpectedQuantity()
    {
        return (Long)getPersistenceContext().getPropertyValue("expectedQuantity");
    }


    @Accessor(qualifier = "returnedQuantity", type = Accessor.Type.GETTER)
    public Long getReturnedQuantity()
    {
        return (Long)getPersistenceContext().getPropertyValue("returnedQuantity");
    }


    @Accessor(qualifier = "expectedQuantity", type = Accessor.Type.SETTER)
    public void setExpectedQuantity(Long value)
    {
        getPersistenceContext().setPropertyValue("expectedQuantity", value);
    }


    @Accessor(qualifier = "returnedQuantity", type = Accessor.Type.SETTER)
    public void setReturnedQuantity(Long value)
    {
        getPersistenceContext().setPropertyValue("returnedQuantity", value);
    }
}
