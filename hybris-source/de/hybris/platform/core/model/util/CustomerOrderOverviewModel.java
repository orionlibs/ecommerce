package de.hybris.platform.core.model.util;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CustomerOrderOverviewModel extends ItemModel
{
    public static final String _TYPECODE = "CustomerOrderOverview";
    public static final String CUSTOMER = "customer";
    public static final String ORDERCOUNT = "orderCount";
    public static final String ORDERTOTALS = "orderTotals";
    public static final String CURRENCY = "currency";


    public CustomerOrderOverviewModel()
    {
    }


    public CustomerOrderOverviewModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CustomerOrderOverviewModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "customer", type = Accessor.Type.GETTER)
    public CustomerModel getCustomer()
    {
        return (CustomerModel)getPersistenceContext().getPropertyValue("customer");
    }


    @Accessor(qualifier = "orderCount", type = Accessor.Type.GETTER)
    public Integer getOrderCount()
    {
        return (Integer)getPersistenceContext().getPropertyValue("orderCount");
    }


    @Accessor(qualifier = "orderTotals", type = Accessor.Type.GETTER)
    public Double getOrderTotals()
    {
        return (Double)getPersistenceContext().getPropertyValue("orderTotals");
    }
}
