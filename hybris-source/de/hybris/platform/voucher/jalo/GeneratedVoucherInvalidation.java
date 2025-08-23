package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.voucher.constants.GeneratedVoucherConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedVoucherInvalidation extends GenericItem
{
    public static final String CODE = "code";
    public static final String USER = "user";
    public static final String ORDER = "order";
    public static final String STATUS = "status";
    public static final String VOUCHER = "voucher";
    protected static final BidirectionalOneToManyHandler<GeneratedVoucherInvalidation> VOUCHERHANDLER = new BidirectionalOneToManyHandler(GeneratedVoucherConstants.TC.VOUCHERINVALIDATION, false, "voucher", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
        tmp.put("order", Item.AttributeMode.INITIAL);
        tmp.put("status", Item.AttributeMode.INITIAL);
        tmp.put("voucher", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    protected void setCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'code' is not changeable", 0);
        }
        setProperty(ctx, "code", value);
    }


    protected void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        VOUCHERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Order getOrder(SessionContext ctx)
    {
        return (Order)getProperty(ctx, "order");
    }


    public Order getOrder()
    {
        return getOrder(getSession().getSessionContext());
    }


    protected void setOrder(SessionContext ctx, Order value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'order' is not changeable", 0);
        }
        setProperty(ctx, "order", value);
    }


    protected void setOrder(Order value)
    {
        setOrder(getSession().getSessionContext(), value);
    }


    public String getStatus(SessionContext ctx)
    {
        return (String)getProperty(ctx, "status");
    }


    public String getStatus()
    {
        return getStatus(getSession().getSessionContext());
    }


    public void setStatus(SessionContext ctx, String value)
    {
        setProperty(ctx, "status", value);
    }


    public void setStatus(String value)
    {
        setStatus(getSession().getSessionContext(), value);
    }


    public User getUser(SessionContext ctx)
    {
        return (User)getProperty(ctx, "user");
    }


    public User getUser()
    {
        return getUser(getSession().getSessionContext());
    }


    protected void setUser(SessionContext ctx, User value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'user' is not changeable", 0);
        }
        setProperty(ctx, "user", value);
    }


    protected void setUser(User value)
    {
        setUser(getSession().getSessionContext(), value);
    }


    public Voucher getVoucher(SessionContext ctx)
    {
        return (Voucher)getProperty(ctx, "voucher");
    }


    public Voucher getVoucher()
    {
        return getVoucher(getSession().getSessionContext());
    }


    protected void setVoucher(SessionContext ctx, Voucher value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'voucher' is not changeable", 0);
        }
        VOUCHERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setVoucher(Voucher value)
    {
        setVoucher(getSession().getSessionContext(), value);
    }
}
