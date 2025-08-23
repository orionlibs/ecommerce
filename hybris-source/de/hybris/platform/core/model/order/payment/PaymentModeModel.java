package de.hybris.platform.core.model.order.payment;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

public class PaymentModeModel extends ItemModel
{
    public static final String _TYPECODE = "PaymentMode";
    public static final String ACTIVE = "active";
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";
    public static final String NAME = "name";
    public static final String PAYMENTINFOTYPE = "paymentInfoType";
    public static final String SUPPORTEDDELIVERYMODES = "supportedDeliveryModes";


    public PaymentModeModel()
    {
    }


    public PaymentModeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PaymentModeModel(Boolean _active, String _code, ComposedTypeModel _paymentInfoType)
    {
        setActive(_active);
        setCode(_code);
        setPaymentInfoType(_paymentInfoType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PaymentModeModel(Boolean _active, String _code, ItemModel _owner, ComposedTypeModel _paymentInfoType)
    {
        setActive(_active);
        setCode(_code);
        setOwner(_owner);
        setPaymentInfoType(_paymentInfoType);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return getDescription(null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("description", loc);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public ComposedTypeModel getPaymentinfotype()
    {
        return getPaymentInfoType();
    }


    @Accessor(qualifier = "paymentInfoType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getPaymentInfoType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("paymentInfoType");
    }


    @Deprecated(since = "ages", forRemoval = true)
    public Collection<DeliveryModeModel> getSupporteddeliverymodes()
    {
        return getSupportedDeliveryModes();
    }


    @Accessor(qualifier = "supportedDeliveryModes", type = Accessor.Type.GETTER)
    public Collection<DeliveryModeModel> getSupportedDeliveryModes()
    {
        return (Collection<DeliveryModeModel>)getPersistenceContext().getPropertyValue("supportedDeliveryModes");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("active", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        setDescription(value, null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("description", loc, value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setPaymentinfotype(ComposedTypeModel value)
    {
        setPaymentInfoType(value);
    }


    @Accessor(qualifier = "paymentInfoType", type = Accessor.Type.SETTER)
    public void setPaymentInfoType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("paymentInfoType", value);
    }
}
