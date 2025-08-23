package de.hybris.platform.voucher.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class RegularCustomerOrderTotalRestrictionModel extends RestrictionModel
{
    public static final String _TYPECODE = "RegularCustomerOrderTotalRestriction";
    public static final String ALLORDERSTOTAL = "allOrdersTotal";
    public static final String CURRENCY = "currency";
    public static final String NET = "net";
    public static final String VALUEOFGOODSONLY = "valueofgoodsonly";


    public RegularCustomerOrderTotalRestrictionModel()
    {
    }


    public RegularCustomerOrderTotalRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RegularCustomerOrderTotalRestrictionModel(Double _allOrdersTotal, CurrencyModel _currency, VoucherModel _voucher)
    {
        setAllOrdersTotal(_allOrdersTotal);
        setCurrency(_currency);
        setVoucher(_voucher);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RegularCustomerOrderTotalRestrictionModel(Double _allOrdersTotal, CurrencyModel _currency, ItemModel _owner, VoucherModel _voucher)
    {
        setAllOrdersTotal(_allOrdersTotal);
        setCurrency(_currency);
        setOwner(_owner);
        setVoucher(_voucher);
    }


    @Accessor(qualifier = "allOrdersTotal", type = Accessor.Type.GETTER)
    public Double getAllOrdersTotal()
    {
        return (Double)getPersistenceContext().getPropertyValue("allOrdersTotal");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "net", type = Accessor.Type.GETTER)
    public Boolean getNet()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("net");
    }


    @Accessor(qualifier = "valueofgoodsonly", type = Accessor.Type.GETTER)
    public Boolean getValueofgoodsonly()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("valueofgoodsonly");
    }


    @Accessor(qualifier = "allOrdersTotal", type = Accessor.Type.SETTER)
    public void setAllOrdersTotal(Double value)
    {
        getPersistenceContext().setPropertyValue("allOrdersTotal", value);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "net", type = Accessor.Type.SETTER)
    public void setNet(Boolean value)
    {
        getPersistenceContext().setPropertyValue("net", value);
    }


    @Accessor(qualifier = "valueofgoodsonly", type = Accessor.Type.SETTER)
    public void setValueofgoodsonly(Boolean value)
    {
        getPersistenceContext().setPropertyValue("valueofgoodsonly", value);
    }
}
