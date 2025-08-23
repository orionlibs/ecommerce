package de.hybris.platform.jalo.order.payment;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPaymentInfo extends GenericItem
{
    public static final String ORIGINAL = "original";
    public static final String CODE = "code";
    public static final String DUPLICATE = "duplicate";
    public static final String USER = "user";
    protected static final BidirectionalOneToManyHandler<GeneratedPaymentInfo> USERHANDLER = new BidirectionalOneToManyHandler(GeneratedCoreConstants.TC.PAYMENTINFO, false, "user", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("original", Item.AttributeMode.INITIAL);
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("duplicate", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
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


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        USERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Boolean isDuplicate(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "duplicate");
    }


    public Boolean isDuplicate()
    {
        return isDuplicate(getSession().getSessionContext());
    }


    public boolean isDuplicateAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDuplicate(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDuplicateAsPrimitive()
    {
        return isDuplicateAsPrimitive(getSession().getSessionContext());
    }


    public void setDuplicate(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "duplicate", value);
    }


    public void setDuplicate(Boolean value)
    {
        setDuplicate(getSession().getSessionContext(), value);
    }


    public void setDuplicate(SessionContext ctx, boolean value)
    {
        setDuplicate(ctx, Boolean.valueOf(value));
    }


    public void setDuplicate(boolean value)
    {
        setDuplicate(getSession().getSessionContext(), value);
    }


    public Item getOriginal(SessionContext ctx)
    {
        return (Item)getProperty(ctx, "original");
    }


    public Item getOriginal()
    {
        return getOriginal(getSession().getSessionContext());
    }


    protected void setOriginal(SessionContext ctx, Item value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'original' is not changeable", 0);
        }
        setProperty(ctx, "original", value);
    }


    protected void setOriginal(Item value)
    {
        setOriginal(getSession().getSessionContext(), value);
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
        USERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setUser(User value)
    {
        setUser(getSession().getSessionContext(), value);
    }
}
