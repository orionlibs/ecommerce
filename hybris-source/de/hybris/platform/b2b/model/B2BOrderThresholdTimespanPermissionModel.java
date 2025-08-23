package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.b2b.enums.B2BPeriodRange;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class B2BOrderThresholdTimespanPermissionModel extends B2BOrderThresholdPermissionModel
{
    public static final String _TYPECODE = "B2BOrderThresholdTimespanPermission";
    public static final String RANGE = "range";


    public B2BOrderThresholdTimespanPermissionModel()
    {
    }


    public B2BOrderThresholdTimespanPermissionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BOrderThresholdTimespanPermissionModel(B2BUnitModel _Unit, String _code, CurrencyModel _currency, B2BPeriodRange _range, Double _threshold)
    {
        setUnit(_Unit);
        setCode(_code);
        setCurrency(_currency);
        setRange(_range);
        setThreshold(_threshold);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BOrderThresholdTimespanPermissionModel(B2BUnitModel _Unit, String _code, CurrencyModel _currency, ItemModel _owner, B2BPeriodRange _range, Double _threshold)
    {
        setUnit(_Unit);
        setCode(_code);
        setCurrency(_currency);
        setOwner(_owner);
        setRange(_range);
        setThreshold(_threshold);
    }


    @Accessor(qualifier = "range", type = Accessor.Type.GETTER)
    public B2BPeriodRange getRange()
    {
        return (B2BPeriodRange)getPersistenceContext().getPropertyValue("range");
    }


    @Accessor(qualifier = "range", type = Accessor.Type.SETTER)
    public void setRange(B2BPeriodRange value)
    {
        getPersistenceContext().setPropertyValue("range", value);
    }
}
