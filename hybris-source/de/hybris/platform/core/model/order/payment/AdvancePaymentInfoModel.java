package de.hybris.platform.core.model.order.payment;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AdvancePaymentInfoModel extends PaymentInfoModel
{
    public static final String _TYPECODE = "AdvancePaymentInfo";


    public AdvancePaymentInfoModel()
    {
    }


    public AdvancePaymentInfoModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AdvancePaymentInfoModel(String _code, UserModel _user)
    {
        setCode(_code);
        setUser(_user);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AdvancePaymentInfoModel(String _code, ItemModel _original, ItemModel _owner, UserModel _user)
    {
        setCode(_code);
        setOriginal(_original);
        setOwner(_owner);
        setUser(_user);
    }
}
