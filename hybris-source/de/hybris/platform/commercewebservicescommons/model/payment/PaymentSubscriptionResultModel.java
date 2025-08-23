package de.hybris.platform.commercewebservicescommons.model.payment;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class PaymentSubscriptionResultModel extends ItemModel
{
    public static final String _TYPECODE = "PaymentSubscriptionResult";
    public static final String CARTID = "cartId";
    public static final String SUCCESS = "success";
    public static final String RESULT = "result";


    public PaymentSubscriptionResultModel()
    {
    }


    public PaymentSubscriptionResultModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PaymentSubscriptionResultModel(String _cartId)
    {
        setCartId(_cartId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PaymentSubscriptionResultModel(String _cartId, ItemModel _owner)
    {
        setCartId(_cartId);
        setOwner(_owner);
    }


    @Accessor(qualifier = "cartId", type = Accessor.Type.GETTER)
    public String getCartId()
    {
        return (String)getPersistenceContext().getPropertyValue("cartId");
    }


    @Accessor(qualifier = "result", type = Accessor.Type.GETTER)
    public Object getResult()
    {
        return getPersistenceContext().getPropertyValue("result");
    }


    @Accessor(qualifier = "success", type = Accessor.Type.GETTER)
    public boolean isSuccess()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("success"));
    }


    @Accessor(qualifier = "cartId", type = Accessor.Type.SETTER)
    public void setCartId(String value)
    {
        getPersistenceContext().setPropertyValue("cartId", value);
    }


    @Accessor(qualifier = "result", type = Accessor.Type.SETTER)
    public void setResult(Object value)
    {
        getPersistenceContext().setPropertyValue("result", value);
    }


    @Accessor(qualifier = "success", type = Accessor.Type.SETTER)
    public void setSuccess(boolean value)
    {
        getPersistenceContext().setPropertyValue("success", toObject(value));
    }
}
