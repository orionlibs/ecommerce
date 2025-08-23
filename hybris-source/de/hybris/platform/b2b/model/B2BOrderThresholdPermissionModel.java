package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class B2BOrderThresholdPermissionModel extends B2BPermissionModel
{
    public static final String _TYPECODE = "B2BOrderThresholdPermission";
    public static final String THRESHOLD = "threshold";
    public static final String CURRENCY = "currency";


    public B2BOrderThresholdPermissionModel()
    {
    }


    public B2BOrderThresholdPermissionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BOrderThresholdPermissionModel(B2BUnitModel _Unit, String _code, CurrencyModel _currency, Double _threshold)
    {
        setUnit(_Unit);
        setCode(_code);
        setCurrency(_currency);
        setThreshold(_threshold);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BOrderThresholdPermissionModel(B2BUnitModel _Unit, String _code, CurrencyModel _currency, ItemModel _owner, Double _threshold)
    {
        setUnit(_Unit);
        setCode(_code);
        setCurrency(_currency);
        setOwner(_owner);
        setThreshold(_threshold);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "threshold", type = Accessor.Type.GETTER)
    public Double getThreshold()
    {
        return (Double)getPersistenceContext().getPropertyValue("threshold");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "threshold", type = Accessor.Type.SETTER)
    public void setThreshold(Double value)
    {
        getPersistenceContext().setPropertyValue("threshold", value);
    }
}
