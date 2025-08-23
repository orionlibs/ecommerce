package de.hybris.platform.jalo.order;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.ItemCloneCreator;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.UserManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class Order extends GeneratedOrder
{
    private static final Logger LOG = Logger.getLogger(Order.class);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
    public static final String START_DATE = "Order.startDate";
    public static final String END_DATE = "Order.endDate";


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(!Order.class.isAssignableFrom(type.getJaloClass()))
        {
            throw new JaloInvalidParameterException("Order type " + type + " is incompatible to default Order class", 0);
        }
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("user", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("currency", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("date", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("net", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing parameters " + missing + " to create a order ", 0);
        }
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("user", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("currency", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("date", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("net", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(GeneratedOrder.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.remove("paymentAddress");
        tmp.remove("deliveryAddress");
        tmp.remove("paymentInfo");
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    @ForceJALO(reason = "something else")
    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    @ForceJALO(reason = "abstract method implementation")
    protected String getAbstractOrderEntryTypeCode()
    {
        return OrderManager.getInstance().getOrderEntryTypeCode();
    }


    @ForceJALO(reason = "consistency check")
    public void setPaymentAddress(Address adr)
    {
        setPaymentAddress(getSession().getSessionContext(), adr);
    }


    public void setPaymentAddressNoCopy(Address address)
    {
        setPaymentAddressNoCopy(getSession().getSessionContext(), address);
    }


    @ForceJALO(reason = "consistency check")
    public void setPaymentAddress(SessionContext ctx, Address address)
    {
        Address oldOne = getPaymentAddress(ctx);
        if(oldOne != address && (oldOne == null || !oldOne.equals(address)))
        {
            changePaymentAddress(ctx, address, oldOne);
        }
    }


    protected void changePaymentAddress(SessionContext ctx, Address address, Address oldOne)
    {
        if(address != null && address.isDuplicate().booleanValue() && Boolean.TRUE
                        .equals(getSession().getAttribute("import.mode")))
        {
            setPaymentAddressNoCopy(ctx, address);
        }
        else
        {
            setPaymentAddressNoCopy(ctx, copyAddressIfNecessary(ctx, address));
        }
        removeOldCopyAddress(ctx, oldOne);
    }


    protected void removeOldCopyAddress(SessionContext ctx, Address oldOne)
    {
        if(oldOne != null && !oldOne.equals(getDeliveryAddress(ctx)) && !oldOne.equals(getPaymentAddress(ctx)))
        {
            if(!oldOne.isDuplicate().booleanValue())
            {
                throw new JaloSystemException(null, "order holds a non-copy address", 290774);
            }
            try
            {
                oldOne.remove(ctx);
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloSystemException(e);
            }
        }
    }


    protected Address copyAddressIfNecessary(SessionContext ctx, Address original)
    {
        if(original == null)
        {
            return null;
        }
        if(original.isDuplicate().booleanValue())
        {
            if(equals(original.getOwner()))
            {
                return original;
            }
            LOG.warn("Copying a copy-address again since it belongs to different owner " + original.getOwner() + "..");
        }
        ItemCloneCreator.CopyContext cctx = new ItemCloneCreator.CopyContext();
        cctx.addPreset((Item)original, "duplicate", Boolean.TRUE);
        cctx.addPreset((Item)original, "original", original);
        cctx.addPreset((Item)original, Address.OWNER, this);
        try
        {
            return (Address)(new ItemCloneCreator()).copy((Item)original, cctx);
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
    }


    protected void removeOldCopyPaymentInfo(SessionContext ctx, PaymentInfo oldOne)
    {
        if(oldOne != null)
        {
            if(!oldOne.isDuplicateAsPrimitive())
            {
                throw new JaloSystemException(null, "order holds a non-copy payment info", 290774);
            }
            try
            {
                oldOne.remove(ctx);
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloSystemException(e);
            }
        }
    }


    protected PaymentInfo copyPaymentInfoIfNecessary(SessionContext ctx, PaymentInfo original)
    {
        if(original == null)
        {
            return null;
        }
        if(original.isDuplicateAsPrimitive())
        {
            if(equals(original.getOwner()))
            {
                return original;
            }
            LOG.warn("Copying a copy-payment info again since it belongs to different owner " + original.getOwner() + "..");
        }
        ItemCloneCreator.CopyContext cctx = new ItemCloneCreator.CopyContext();
        cctx.addPreset((Item)original, "duplicate", Boolean.TRUE);
        cctx.addPreset((Item)original, "original", original);
        cctx.addPreset((Item)original, PaymentInfo.OWNER, this);
        try
        {
            return (PaymentInfo)(new ItemCloneCreator()).copy((Item)original, cctx);
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public void setPaymentAddressNoCopy(SessionContext ctx, Address address)
    {
        super.setPaymentAddress(ctx, address);
    }


    @ForceJALO(reason = "consistency check")
    public void setDeliveryAddress(Address adr)
    {
        setDeliveryAddress(getSession().getSessionContext(), adr);
    }


    public void setDeliveryAddressNoCopy(Address address)
    {
        setDeliveryAddressNoCopy(getSession().getSessionContext(), address);
    }


    @ForceJALO(reason = "consistency check")
    public void setDeliveryAddress(SessionContext ctx, Address address)
    {
        Address oldOne = getDeliveryAddress(ctx);
        if(oldOne != address && (oldOne == null || !oldOne.equals(address)))
        {
            changeDeliveryAddress(ctx, address, oldOne);
        }
    }


    protected void changeDeliveryAddress(SessionContext ctx, Address address, Address oldOne)
    {
        if(address != null && address.isDuplicate().booleanValue() && Boolean.TRUE
                        .equals(getSession().getAttribute("import.mode")))
        {
            setDeliveryAddressNoCopy(ctx, address);
        }
        else
        {
            setDeliveryAddressNoCopy(ctx, copyAddressIfNecessary(ctx, address));
        }
        removeOldCopyAddress(ctx, oldOne);
    }


    public void setDeliveryAddressNoCopy(SessionContext ctx, Address address)
    {
        super.setDeliveryAddress(ctx, address);
    }


    @ForceJALO(reason = "consistency check")
    public void setPaymentInfo(PaymentInfo info)
    {
        setPaymentInfo(getSession().getSessionContext(), info);
    }


    public void setPaymentInfoNoCopy(PaymentInfo info)
    {
        setPaymentInfoNoCopy(getSession().getSessionContext(), info);
    }


    @ForceJALO(reason = "consistency check")
    public void setPaymentInfo(SessionContext ctx, PaymentInfo info)
    {
        PaymentInfo oldOne = getPaymentInfo(ctx);
        if(oldOne != info && (oldOne == null || !oldOne.equals(info)))
        {
            changePaymentInfo(ctx, info, oldOne);
        }
    }


    protected void changePaymentInfo(SessionContext ctx, PaymentInfo info, PaymentInfo oldOne)
    {
        if(info != null && info.isDuplicateAsPrimitive() && Boolean.TRUE
                        .equals(getSession().getAttribute("import.mode")))
        {
            setPaymentInfoNoCopy(ctx, info);
        }
        else
        {
            setPaymentInfoNoCopy(ctx, copyPaymentInfoIfNecessary(ctx, info));
        }
        removeOldCopyPaymentInfo(ctx, oldOne);
    }


    public void setPaymentInfoNoCopy(SessionContext ctx, PaymentInfo info)
    {
        super.setPaymentInfo(ctx, info);
    }


    protected OrderEntry createNewEntry(SessionContext ctx, ComposedType entryType, Product product, long quantity, Unit unit, int position)
    {
        if(entryType != null && !OrderEntry.class.isAssignableFrom(entryType.getJaloClass()))
        {
            throw new JaloInvalidParameterException("type is not assignable from OrderEntry", 0);
        }
        try
        {
            Item.ItemAttributeMap params = new Item.ItemAttributeMap();
            params.put("order", this);
            params.put("entryNumber", Integer.valueOf(position));
            params.put("quantity", Long.valueOf(quantity));
            params.put("unit", unit);
            params.put("product", product);
            if(entryType == null)
            {
                entryType = TypeManager.getInstance().getComposedType(OrderEntry.class);
            }
            return (OrderEntry)entryType.newInstance(getSession().getSessionContext(), (Map)params);
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @ForceJALO(reason = "something else")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        UserManager um = UserManager.getInstance();
        Address p_address = getPaymentAddress();
        Address d_address = getDeliveryAddress();
        boolean theSame = (p_address != null && d_address != null && p_address.equals(d_address));
        if(p_address != null)
        {
            if(p_address.isDuplicate().booleanValue())
            {
                um.removeItem((Item)p_address);
            }
        }
        if(!theSame && d_address != null)
        {
            if(d_address.isDuplicate().booleanValue())
            {
                um.removeItem((Item)d_address);
            }
        }
        PaymentInfo info = getPaymentInfo();
        if(info != null && info.isDuplicate().booleanValue())
        {
            info.remove();
        }
        super.remove(ctx);
    }
}
