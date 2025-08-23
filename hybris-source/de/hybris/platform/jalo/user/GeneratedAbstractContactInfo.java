package de.hybris.platform.jalo.user;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractContactInfo extends GenericItem
{
    public static final String CODE = "code";
    public static final String USERPOS = "userPOS";
    public static final String USER = "user";
    protected static final BidirectionalOneToManyHandler<GeneratedAbstractContactInfo> USERHANDLER = new BidirectionalOneToManyHandler(GeneratedCoreConstants.TC.ABSTRACTCONTACTINFO, false, "user", "userPOS", true, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("userPOS", Item.AttributeMode.INITIAL);
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


    public User getUser(SessionContext ctx)
    {
        return (User)getProperty(ctx, "user");
    }


    public User getUser()
    {
        return getUser(getSession().getSessionContext());
    }


    public void setUser(SessionContext ctx, User value)
    {
        USERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setUser(User value)
    {
        setUser(getSession().getSessionContext(), value);
    }


    Integer getUserPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "userPOS");
    }


    Integer getUserPOS()
    {
        return getUserPOS(getSession().getSessionContext());
    }


    int getUserPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getUserPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getUserPOSAsPrimitive()
    {
        return getUserPOSAsPrimitive(getSession().getSessionContext());
    }


    void setUserPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "userPOS", value);
    }


    void setUserPOS(Integer value)
    {
        setUserPOS(getSession().getSessionContext(), value);
    }


    void setUserPOS(SessionContext ctx, int value)
    {
        setUserPOS(ctx, Integer.valueOf(value));
    }


    void setUserPOS(int value)
    {
        setUserPOS(getSession().getSessionContext(), value);
    }
}
