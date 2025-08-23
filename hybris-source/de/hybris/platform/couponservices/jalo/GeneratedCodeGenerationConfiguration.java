package de.hybris.platform.couponservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCodeGenerationConfiguration extends GenericItem
{
    public static final String NAME = "name";
    public static final String CODESEPARATOR = "codeSeparator";
    public static final String COUPONPARTCOUNT = "couponPartCount";
    public static final String COUPONPARTLENGTH = "couponPartLength";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("codeSeparator", Item.AttributeMode.INITIAL);
        tmp.put("couponPartCount", Item.AttributeMode.INITIAL);
        tmp.put("couponPartLength", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCodeSeparator(SessionContext ctx)
    {
        return (String)getProperty(ctx, "codeSeparator");
    }


    public String getCodeSeparator()
    {
        return getCodeSeparator(getSession().getSessionContext());
    }


    public void setCodeSeparator(SessionContext ctx, String value)
    {
        setProperty(ctx, "codeSeparator", value);
    }


    public void setCodeSeparator(String value)
    {
        setCodeSeparator(getSession().getSessionContext(), value);
    }


    public Integer getCouponPartCount(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "couponPartCount");
    }


    public Integer getCouponPartCount()
    {
        return getCouponPartCount(getSession().getSessionContext());
    }


    public int getCouponPartCountAsPrimitive(SessionContext ctx)
    {
        Integer value = getCouponPartCount(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getCouponPartCountAsPrimitive()
    {
        return getCouponPartCountAsPrimitive(getSession().getSessionContext());
    }


    public void setCouponPartCount(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "couponPartCount", value);
    }


    public void setCouponPartCount(Integer value)
    {
        setCouponPartCount(getSession().getSessionContext(), value);
    }


    public void setCouponPartCount(SessionContext ctx, int value)
    {
        setCouponPartCount(ctx, Integer.valueOf(value));
    }


    public void setCouponPartCount(int value)
    {
        setCouponPartCount(getSession().getSessionContext(), value);
    }


    public Integer getCouponPartLength(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "couponPartLength");
    }


    public Integer getCouponPartLength()
    {
        return getCouponPartLength(getSession().getSessionContext());
    }


    public int getCouponPartLengthAsPrimitive(SessionContext ctx)
    {
        Integer value = getCouponPartLength(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getCouponPartLengthAsPrimitive()
    {
        return getCouponPartLengthAsPrimitive(getSession().getSessionContext());
    }


    public void setCouponPartLength(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "couponPartLength", value);
    }


    public void setCouponPartLength(Integer value)
    {
        setCouponPartLength(getSession().getSessionContext(), value);
    }


    public void setCouponPartLength(SessionContext ctx, int value)
    {
        setCouponPartLength(ctx, Integer.valueOf(value));
    }


    public void setCouponPartLength(int value)
    {
        setCouponPartLength(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        setProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }
}
