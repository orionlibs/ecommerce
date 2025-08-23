package de.hybris.platform.core.model.order.delivery;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

public class DeliveryModeModel extends ItemModel
{
    public static final String _TYPECODE = "DeliveryMode";
    public static final String _BASESTORE2DELIVERYMODEREL = "BaseStore2DeliveryModeRel";
    public static final String ACTIVE = "active";
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";
    public static final String NAME = "name";
    public static final String SUPPORTEDPAYMENTMODES = "supportedPaymentModes";
    public static final String SUPPORTEDPAYMENTMODESINTERNAL = "supportedPaymentModesInternal";
    public static final String STORES = "stores";


    public DeliveryModeModel()
    {
    }


    public DeliveryModeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DeliveryModeModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DeliveryModeModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
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


    @Accessor(qualifier = "stores", type = Accessor.Type.GETTER)
    public Set<BaseStoreModel> getStores()
    {
        return (Set<BaseStoreModel>)getPersistenceContext().getPropertyValue("stores");
    }


    @Accessor(qualifier = "supportedPaymentModes", type = Accessor.Type.GETTER)
    public Collection<PaymentModeModel> getSupportedPaymentModes()
    {
        return (Collection<PaymentModeModel>)getPersistenceContext().getPropertyValue("supportedPaymentModes");
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


    @Accessor(qualifier = "stores", type = Accessor.Type.SETTER)
    public void setStores(Set<BaseStoreModel> value)
    {
        getPersistenceContext().setPropertyValue("stores", value);
    }


    @Accessor(qualifier = "supportedPaymentModes", type = Accessor.Type.SETTER)
    public void setSupportedPaymentModes(Collection<PaymentModeModel> value)
    {
        getPersistenceContext().setPropertyValue("supportedPaymentModes", value);
    }
}
