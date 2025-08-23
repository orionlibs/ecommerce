package de.hybris.platform.basecommerce.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.internal.model.order.InMemoryCartModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class MultiAddressInMemoryCartModel extends InMemoryCartModel
{
    public static final String _TYPECODE = "MultiAddressInMemoryCart";


    public MultiAddressInMemoryCartModel()
    {
    }


    public MultiAddressInMemoryCartModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MultiAddressInMemoryCartModel(CurrencyModel _currency, Date _date, UserModel _user)
    {
        setCurrency(_currency);
        setDate(_date);
        setUser(_user);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MultiAddressInMemoryCartModel(CurrencyModel _currency, Date _date, ItemModel _owner, UserModel _user)
    {
        setCurrency(_currency);
        setDate(_date);
        setOwner(_owner);
        setUser(_user);
    }
}
