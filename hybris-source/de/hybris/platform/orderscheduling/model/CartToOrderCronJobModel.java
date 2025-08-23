package de.hybris.platform.orderscheduling.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class CartToOrderCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "CartToOrderCronJob";
    public static final String _CART2CARTTOORDERCRONJOB = "Cart2CartToOrderCronJob";
    public static final String _DELIVERYADDRESSS2CARTTOORDERCRONJOB = "DeliveryAddresss2CartToOrderCronJob";
    public static final String _PAYMENTADDRESSS2CARTTOORDERCRONJOB = "PaymentAddresss2CartToOrderCronJob";
    public static final String _PAYMENTINFO2CARTTOORDERCRONJOB = "PaymentInfo2CartToOrderCronJob";
    public static final String CART = "cart";
    public static final String DELIVERYADDRESS = "deliveryAddress";
    public static final String PAYMENTADDRESS = "paymentAddress";
    public static final String PAYMENTINFO = "paymentInfo";
    public static final String ORDERS = "orders";


    public CartToOrderCronJobModel()
    {
    }


    public CartToOrderCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CartToOrderCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CartToOrderCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "cart", type = Accessor.Type.GETTER)
    public CartModel getCart()
    {
        return (CartModel)getPersistenceContext().getPropertyValue("cart");
    }


    @Accessor(qualifier = "deliveryAddress", type = Accessor.Type.GETTER)
    public AddressModel getDeliveryAddress()
    {
        return (AddressModel)getPersistenceContext().getPropertyValue("deliveryAddress");
    }


    @Accessor(qualifier = "orders", type = Accessor.Type.GETTER)
    public Collection<OrderModel> getOrders()
    {
        return (Collection<OrderModel>)getPersistenceContext().getPropertyValue("orders");
    }


    @Accessor(qualifier = "paymentAddress", type = Accessor.Type.GETTER)
    public AddressModel getPaymentAddress()
    {
        return (AddressModel)getPersistenceContext().getPropertyValue("paymentAddress");
    }


    @Accessor(qualifier = "paymentInfo", type = Accessor.Type.GETTER)
    public PaymentInfoModel getPaymentInfo()
    {
        return (PaymentInfoModel)getPersistenceContext().getPropertyValue("paymentInfo");
    }


    @Accessor(qualifier = "cart", type = Accessor.Type.SETTER)
    public void setCart(CartModel value)
    {
        getPersistenceContext().setPropertyValue("cart", value);
    }


    @Accessor(qualifier = "deliveryAddress", type = Accessor.Type.SETTER)
    public void setDeliveryAddress(AddressModel value)
    {
        getPersistenceContext().setPropertyValue("deliveryAddress", value);
    }


    @Accessor(qualifier = "orders", type = Accessor.Type.SETTER)
    public void setOrders(Collection<OrderModel> value)
    {
        getPersistenceContext().setPropertyValue("orders", value);
    }


    @Accessor(qualifier = "paymentAddress", type = Accessor.Type.SETTER)
    public void setPaymentAddress(AddressModel value)
    {
        getPersistenceContext().setPropertyValue("paymentAddress", value);
    }


    @Accessor(qualifier = "paymentInfo", type = Accessor.Type.SETTER)
    public void setPaymentInfo(PaymentInfoModel value)
    {
        getPersistenceContext().setPropertyValue("paymentInfo", value);
    }
}
