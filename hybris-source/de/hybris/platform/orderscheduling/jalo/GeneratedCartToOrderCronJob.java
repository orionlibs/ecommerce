package de.hybris.platform.orderscheduling.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCartToOrderCronJob extends CronJob
{
    public static final String CART = "cart";
    public static final String DELIVERYADDRESS = "deliveryAddress";
    public static final String PAYMENTADDRESS = "paymentAddress";
    public static final String PAYMENTINFO = "paymentInfo";
    protected static final BidirectionalOneToManyHandler<GeneratedCartToOrderCronJob> CARTHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.CARTTOORDERCRONJOB, false, "cart", null, false, true, 0);
    protected static final BidirectionalOneToManyHandler<GeneratedCartToOrderCronJob> DELIVERYADDRESSHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.CARTTOORDERCRONJOB, false, "deliveryAddress", null, false, true, 0);
    protected static final BidirectionalOneToManyHandler<GeneratedCartToOrderCronJob> PAYMENTADDRESSHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.CARTTOORDERCRONJOB, false, "paymentAddress", null, false, true, 0);
    protected static final BidirectionalOneToManyHandler<GeneratedCartToOrderCronJob> PAYMENTINFOHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.CARTTOORDERCRONJOB, false, "paymentInfo", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("cart", Item.AttributeMode.INITIAL);
        tmp.put("deliveryAddress", Item.AttributeMode.INITIAL);
        tmp.put("paymentAddress", Item.AttributeMode.INITIAL);
        tmp.put("paymentInfo", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Cart getCart(SessionContext ctx)
    {
        return (Cart)getProperty(ctx, "cart");
    }


    public Cart getCart()
    {
        return getCart(getSession().getSessionContext());
    }


    public void setCart(SessionContext ctx, Cart value)
    {
        CARTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setCart(Cart value)
    {
        setCart(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        CARTHANDLER.newInstance(ctx, allAttributes);
        DELIVERYADDRESSHANDLER.newInstance(ctx, allAttributes);
        PAYMENTADDRESSHANDLER.newInstance(ctx, allAttributes);
        PAYMENTINFOHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Address getDeliveryAddress(SessionContext ctx)
    {
        return (Address)getProperty(ctx, "deliveryAddress");
    }


    public Address getDeliveryAddress()
    {
        return getDeliveryAddress(getSession().getSessionContext());
    }


    public void setDeliveryAddress(SessionContext ctx, Address value)
    {
        DELIVERYADDRESSHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setDeliveryAddress(Address value)
    {
        setDeliveryAddress(getSession().getSessionContext(), value);
    }


    public Address getPaymentAddress(SessionContext ctx)
    {
        return (Address)getProperty(ctx, "paymentAddress");
    }


    public Address getPaymentAddress()
    {
        return getPaymentAddress(getSession().getSessionContext());
    }


    public void setPaymentAddress(SessionContext ctx, Address value)
    {
        PAYMENTADDRESSHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setPaymentAddress(Address value)
    {
        setPaymentAddress(getSession().getSessionContext(), value);
    }


    public PaymentInfo getPaymentInfo(SessionContext ctx)
    {
        return (PaymentInfo)getProperty(ctx, "paymentInfo");
    }


    public PaymentInfo getPaymentInfo()
    {
        return getPaymentInfo(getSession().getSessionContext());
    }


    public void setPaymentInfo(SessionContext ctx, PaymentInfo value)
    {
        PAYMENTINFOHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setPaymentInfo(PaymentInfo value)
    {
        setPaymentInfo(getSession().getSessionContext(), value);
    }
}
