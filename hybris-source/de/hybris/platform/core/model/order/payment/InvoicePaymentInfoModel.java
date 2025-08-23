package de.hybris.platform.core.model.order.payment;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class InvoicePaymentInfoModel extends PaymentInfoModel
{
    public static final String _TYPECODE = "InvoicePaymentInfo";


    public InvoicePaymentInfoModel()
    {
    }


    public InvoicePaymentInfoModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public InvoicePaymentInfoModel(String _code, UserModel _user)
    {
        setCode(_code);
        setUser(_user);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public InvoicePaymentInfoModel(String _code, ItemModel _original, ItemModel _owner, UserModel _user)
    {
        setCode(_code);
        setOriginal(_original);
        setOwner(_owner);
        setUser(_user);
    }
}
