package de.hybris.platform.acceleratorservices.orderprocessing.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OrderModificationProcessModel extends OrderProcessModel
{
    public static final String _TYPECODE = "OrderModificationProcess";
    public static final String ORDERMODIFICATIONRECORDENTRY = "orderModificationRecordEntry";


    public OrderModificationProcessModel()
    {
    }


    public OrderModificationProcessModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderModificationProcessModel(String _code, String _processDefinitionName)
    {
        setCode(_code);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderModificationProcessModel(String _code, ItemModel _owner, String _processDefinitionName)
    {
        setCode(_code);
        setOwner(_owner);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Accessor(qualifier = "orderModificationRecordEntry", type = Accessor.Type.GETTER)
    public OrderModificationRecordEntryModel getOrderModificationRecordEntry()
    {
        return (OrderModificationRecordEntryModel)getPersistenceContext().getPropertyValue("orderModificationRecordEntry");
    }


    @Accessor(qualifier = "orderModificationRecordEntry", type = Accessor.Type.SETTER)
    public void setOrderModificationRecordEntry(OrderModificationRecordEntryModel value)
    {
        getPersistenceContext().setPropertyValue("orderModificationRecordEntry", value);
    }
}
