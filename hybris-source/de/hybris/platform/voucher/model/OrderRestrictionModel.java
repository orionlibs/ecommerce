package de.hybris.platform.voucher.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OrderRestrictionModel extends RestrictionModel
{
    public static final String _TYPECODE = "OrderRestriction";
    public static final String TOTAL = "total";
    public static final String CURRENCY = "currency";
    public static final String NET = "net";
    public static final String VALUEOFGOODSONLY = "valueofgoodsonly";


    public OrderRestrictionModel()
    {
    }


    public OrderRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderRestrictionModel(CurrencyModel _currency, Double _total, VoucherModel _voucher)
    {
        setCurrency(_currency);
        setTotal(_total);
        setVoucher(_voucher);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderRestrictionModel(CurrencyModel _currency, ItemModel _owner, Double _total, VoucherModel _voucher)
    {
        setCurrency(_currency);
        setOwner(_owner);
        setTotal(_total);
        setVoucher(_voucher);
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


    @Accessor(qualifier = "total", type = Accessor.Type.GETTER)
    public Double getTotal()
    {
        return (Double)getPersistenceContext().getPropertyValue("total");
    }


    @Accessor(qualifier = "valueofgoodsonly", type = Accessor.Type.GETTER)
    public Boolean getValueofgoodsonly()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("valueofgoodsonly");
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


    @Accessor(qualifier = "total", type = Accessor.Type.SETTER)
    public void setTotal(Double value)
    {
        getPersistenceContext().setPropertyValue("total", value);
    }


    @Accessor(qualifier = "valueofgoodsonly", type = Accessor.Type.SETTER)
    public void setValueofgoodsonly(Boolean value)
    {
        getPersistenceContext().setPropertyValue("valueofgoodsonly", value);
    }
}
