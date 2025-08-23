package de.hybris.platform.orderhistory.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.Set;

public class OrderHistoryEntryModel extends ItemModel
{
    public static final String _TYPECODE = "OrderHistoryEntry";
    public static final String _ORDERHISTORYRELATION = "OrderHistoryRelation";
    public static final String TIMESTAMP = "timestamp";
    public static final String EMPLOYEE = "employee";
    public static final String DESCRIPTION = "description";
    public static final String PREVIOUSORDERVERSION = "previousOrderVersion";
    public static final String ORDERPOS = "orderPOS";
    public static final String ORDER = "order";
    public static final String DOCUMENTS = "documents";


    public OrderHistoryEntryModel()
    {
    }


    public OrderHistoryEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderHistoryEntryModel(OrderModel _order, Date _timestamp)
    {
        setOrder(_order);
        setTimestamp(_timestamp);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderHistoryEntryModel(OrderModel _order, ItemModel _owner, Date _timestamp)
    {
        setOrder(_order);
        setOwner(_owner);
        setTimestamp(_timestamp);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return (String)getPersistenceContext().getPropertyValue("description");
    }


    @Accessor(qualifier = "documents", type = Accessor.Type.GETTER)
    public Set<MediaModel> getDocuments()
    {
        return (Set<MediaModel>)getPersistenceContext().getPropertyValue("documents");
    }


    @Accessor(qualifier = "employee", type = Accessor.Type.GETTER)
    public EmployeeModel getEmployee()
    {
        return (EmployeeModel)getPersistenceContext().getPropertyValue("employee");
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public OrderModel getOrder()
    {
        return (OrderModel)getPersistenceContext().getPropertyValue("order");
    }


    @Accessor(qualifier = "previousOrderVersion", type = Accessor.Type.GETTER)
    public OrderModel getPreviousOrderVersion()
    {
        return (OrderModel)getPersistenceContext().getPropertyValue("previousOrderVersion");
    }


    @Accessor(qualifier = "timestamp", type = Accessor.Type.GETTER)
    public Date getTimestamp()
    {
        return (Date)getPersistenceContext().getPropertyValue("timestamp");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        getPersistenceContext().setPropertyValue("description", value);
    }


    @Accessor(qualifier = "documents", type = Accessor.Type.SETTER)
    public void setDocuments(Set<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("documents", value);
    }


    @Accessor(qualifier = "employee", type = Accessor.Type.SETTER)
    public void setEmployee(EmployeeModel value)
    {
        getPersistenceContext().setPropertyValue("employee", value);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(OrderModel value)
    {
        getPersistenceContext().setPropertyValue("order", value);
    }


    @Accessor(qualifier = "previousOrderVersion", type = Accessor.Type.SETTER)
    public void setPreviousOrderVersion(OrderModel value)
    {
        getPersistenceContext().setPropertyValue("previousOrderVersion", value);
    }


    @Accessor(qualifier = "timestamp", type = Accessor.Type.SETTER)
    public void setTimestamp(Date value)
    {
        getPersistenceContext().setPropertyValue("timestamp", value);
    }
}
