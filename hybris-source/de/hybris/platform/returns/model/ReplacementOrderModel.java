package de.hybris.platform.returns.model;

import de.hybris.platform.basecommerce.enums.ReturnFulfillmentStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class ReplacementOrderModel extends ReturnOrderModel
{
    public static final String _TYPECODE = "ReplacementOrder";


    public ReplacementOrderModel()
    {
    }


    public ReplacementOrderModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReplacementOrderModel(CurrencyModel _currency, Date _date, ReturnFulfillmentStatus _fulfilmentStatus, UserModel _user)
    {
        setCurrency(_currency);
        setDate(_date);
        setFulfilmentStatus(_fulfilmentStatus);
        setUser(_user);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReplacementOrderModel(CurrencyModel _currency, Date _date, ReturnFulfillmentStatus _fulfilmentStatus, OrderModel _originalVersion, ItemModel _owner, UserModel _user, String _versionID)
    {
        setCurrency(_currency);
        setDate(_date);
        setFulfilmentStatus(_fulfilmentStatus);
        setOriginalVersion(_originalVersion);
        setOwner(_owner);
        setUser(_user);
        setVersionID(_versionID);
    }
}
