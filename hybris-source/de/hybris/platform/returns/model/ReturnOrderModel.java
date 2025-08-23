package de.hybris.platform.returns.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.ReturnFulfillmentStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class ReturnOrderModel extends OrderModel
{
    public static final String _TYPECODE = "ReturnOrder";
    public static final String FULFILMENTSTATUS = "fulfilmentStatus";
    public static final String NOTES = "notes";


    public ReturnOrderModel()
    {
    }


    public ReturnOrderModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReturnOrderModel(CurrencyModel _currency, Date _date, ReturnFulfillmentStatus _fulfilmentStatus, UserModel _user)
    {
        setCurrency(_currency);
        setDate(_date);
        setFulfilmentStatus(_fulfilmentStatus);
        setUser(_user);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReturnOrderModel(CurrencyModel _currency, Date _date, ReturnFulfillmentStatus _fulfilmentStatus, OrderModel _originalVersion, ItemModel _owner, UserModel _user, String _versionID)
    {
        setCurrency(_currency);
        setDate(_date);
        setFulfilmentStatus(_fulfilmentStatus);
        setOriginalVersion(_originalVersion);
        setOwner(_owner);
        setUser(_user);
        setVersionID(_versionID);
    }


    @Accessor(qualifier = "fulfilmentStatus", type = Accessor.Type.GETTER)
    public ReturnFulfillmentStatus getFulfilmentStatus()
    {
        return (ReturnFulfillmentStatus)getPersistenceContext().getPropertyValue("fulfilmentStatus");
    }


    @Accessor(qualifier = "notes", type = Accessor.Type.GETTER)
    public String getNotes()
    {
        return (String)getPersistenceContext().getPropertyValue("notes");
    }


    @Accessor(qualifier = "fulfilmentStatus", type = Accessor.Type.SETTER)
    public void setFulfilmentStatus(ReturnFulfillmentStatus value)
    {
        getPersistenceContext().setPropertyValue("fulfilmentStatus", value);
    }


    @Accessor(qualifier = "notes", type = Accessor.Type.SETTER)
    public void setNotes(String value)
    {
        getPersistenceContext().setPropertyValue("notes", value);
    }
}
