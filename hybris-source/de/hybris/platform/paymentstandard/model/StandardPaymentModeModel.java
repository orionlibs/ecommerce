package de.hybris.platform.paymentstandard.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class StandardPaymentModeModel extends PaymentModeModel
{
    public static final String _TYPECODE = "StandardPaymentMode";
    public static final String NET = "net";
    public static final String PAYMENTMODEVALUES = "paymentModeValues";


    public StandardPaymentModeModel()
    {
    }


    public StandardPaymentModeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StandardPaymentModeModel(Boolean _active, String _code, ComposedTypeModel _paymentInfoType)
    {
        setActive(_active);
        setCode(_code);
        setPaymentInfoType(_paymentInfoType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StandardPaymentModeModel(Boolean _active, String _code, ItemModel _owner, ComposedTypeModel _paymentInfoType)
    {
        setActive(_active);
        setCode(_code);
        setOwner(_owner);
        setPaymentInfoType(_paymentInfoType);
    }


    @Accessor(qualifier = "net", type = Accessor.Type.GETTER)
    public Boolean getNet()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("net");
    }


    @Accessor(qualifier = "paymentModeValues", type = Accessor.Type.GETTER)
    public Collection<StandardPaymentModeValueModel> getPaymentModeValues()
    {
        return (Collection<StandardPaymentModeValueModel>)getPersistenceContext().getPropertyValue("paymentModeValues");
    }


    @Accessor(qualifier = "net", type = Accessor.Type.SETTER)
    public void setNet(Boolean value)
    {
        getPersistenceContext().setPropertyValue("net", value);
    }


    @Accessor(qualifier = "paymentModeValues", type = Accessor.Type.SETTER)
    public void setPaymentModeValues(Collection<StandardPaymentModeValueModel> value)
    {
        getPersistenceContext().setPropertyValue("paymentModeValues", value);
    }
}
