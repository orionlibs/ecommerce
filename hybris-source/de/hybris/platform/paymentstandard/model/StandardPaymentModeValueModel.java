package de.hybris.platform.paymentstandard.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class StandardPaymentModeValueModel extends ItemModel
{
    public static final String _TYPECODE = "StandardPaymentModeValue";
    public static final String _STDMODEVALUESRELATION = "StdModeValuesRelation";
    public static final String CURRENCY = "currency";
    public static final String VALUE = "value";
    public static final String PAYMENTMODE = "paymentMode";


    public StandardPaymentModeValueModel()
    {
    }


    public StandardPaymentModeValueModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StandardPaymentModeValueModel(CurrencyModel _currency, StandardPaymentModeModel _paymentMode, Double _value)
    {
        setCurrency(_currency);
        setPaymentMode(_paymentMode);
        setValue(_value);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StandardPaymentModeValueModel(CurrencyModel _currency, ItemModel _owner, StandardPaymentModeModel _paymentMode, Double _value)
    {
        setCurrency(_currency);
        setOwner(_owner);
        setPaymentMode(_paymentMode);
        setValue(_value);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "paymentMode", type = Accessor.Type.GETTER)
    public StandardPaymentModeModel getPaymentMode()
    {
        return (StandardPaymentModeModel)getPersistenceContext().getPropertyValue("paymentMode");
    }


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public Double getValue()
    {
        return (Double)getPersistenceContext().getPropertyValue("value");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "paymentMode", type = Accessor.Type.SETTER)
    public void setPaymentMode(StandardPaymentModeModel value)
    {
        getPersistenceContext().setPropertyValue("paymentMode", value);
    }


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(Double value)
    {
        getPersistenceContext().setPropertyValue("value", value);
    }
}
