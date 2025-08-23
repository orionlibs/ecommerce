package de.hybris.platform.europe1.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.user.User;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPDTRow extends GenericItem
{
    public static final String PRODUCT = "product";
    public static final String PG = "pg";
    public static final String PRODUCTMATCHQUALIFIER = "productMatchQualifier";
    public static final String STARTTIME = "startTime";
    public static final String ENDTIME = "endTime";
    public static final String USER = "user";
    public static final String UG = "ug";
    public static final String USERMATCHQUALIFIER = "userMatchQualifier";
    public static final String PRODUCTID = "productId";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("product", Item.AttributeMode.INITIAL);
        tmp.put("pg", Item.AttributeMode.INITIAL);
        tmp.put("productMatchQualifier", Item.AttributeMode.INITIAL);
        tmp.put("startTime", Item.AttributeMode.INITIAL);
        tmp.put("endTime", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
        tmp.put("ug", Item.AttributeMode.INITIAL);
        tmp.put("userMatchQualifier", Item.AttributeMode.INITIAL);
        tmp.put("productId", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Date getEndTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "endTime");
    }


    public Date getEndTime()
    {
        return getEndTime(getSession().getSessionContext());
    }


    public void setEndTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "endTime", value);
    }


    public void setEndTime(Date value)
    {
        setEndTime(getSession().getSessionContext(), value);
    }


    public EnumerationValue getPg(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "pg");
    }


    public EnumerationValue getPg()
    {
        return getPg(getSession().getSessionContext());
    }


    protected void setPg(SessionContext ctx, EnumerationValue value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'pg' is not changeable", 0);
        }
        setProperty(ctx, "pg", value);
    }


    protected void setPg(EnumerationValue value)
    {
        setPg(getSession().getSessionContext(), value);
    }


    public Product getProduct(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "product");
    }


    public Product getProduct()
    {
        return getProduct(getSession().getSessionContext());
    }


    protected void setProduct(SessionContext ctx, Product value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'product' is not changeable", 0);
        }
        setProperty(ctx, "product", value);
    }


    protected void setProduct(Product value)
    {
        setProduct(getSession().getSessionContext(), value);
    }


    public String getProductId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "productId");
    }


    public String getProductId()
    {
        return getProductId(getSession().getSessionContext());
    }


    protected void setProductId(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'productId' is not changeable", 0);
        }
        setProperty(ctx, "productId", value);
    }


    protected void setProductId(String value)
    {
        setProductId(getSession().getSessionContext(), value);
    }


    public Long getProductMatchQualifier(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "productMatchQualifier");
    }


    public Long getProductMatchQualifier()
    {
        return getProductMatchQualifier(getSession().getSessionContext());
    }


    public long getProductMatchQualifierAsPrimitive(SessionContext ctx)
    {
        Long value = getProductMatchQualifier(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getProductMatchQualifierAsPrimitive()
    {
        return getProductMatchQualifierAsPrimitive(getSession().getSessionContext());
    }


    public void setProductMatchQualifier(SessionContext ctx, Long value)
    {
        setProperty(ctx, "productMatchQualifier", value);
    }


    public void setProductMatchQualifier(Long value)
    {
        setProductMatchQualifier(getSession().getSessionContext(), value);
    }


    public void setProductMatchQualifier(SessionContext ctx, long value)
    {
        setProductMatchQualifier(ctx, Long.valueOf(value));
    }


    public void setProductMatchQualifier(long value)
    {
        setProductMatchQualifier(getSession().getSessionContext(), value);
    }


    public Date getStartTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "startTime");
    }


    public Date getStartTime()
    {
        return getStartTime(getSession().getSessionContext());
    }


    public void setStartTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "startTime", value);
    }


    public void setStartTime(Date value)
    {
        setStartTime(getSession().getSessionContext(), value);
    }


    public EnumerationValue getUg(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "ug");
    }


    public EnumerationValue getUg()
    {
        return getUg(getSession().getSessionContext());
    }


    public void setUg(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "ug", value);
    }


    public void setUg(EnumerationValue value)
    {
        setUg(getSession().getSessionContext(), value);
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
        setProperty(ctx, "user", value);
    }


    public void setUser(User value)
    {
        setUser(getSession().getSessionContext(), value);
    }


    public Long getUserMatchQualifier(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "userMatchQualifier");
    }


    public Long getUserMatchQualifier()
    {
        return getUserMatchQualifier(getSession().getSessionContext());
    }


    public long getUserMatchQualifierAsPrimitive(SessionContext ctx)
    {
        Long value = getUserMatchQualifier(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getUserMatchQualifierAsPrimitive()
    {
        return getUserMatchQualifierAsPrimitive(getSession().getSessionContext());
    }


    public void setUserMatchQualifier(SessionContext ctx, Long value)
    {
        setProperty(ctx, "userMatchQualifier", value);
    }


    public void setUserMatchQualifier(Long value)
    {
        setUserMatchQualifier(getSession().getSessionContext(), value);
    }


    public void setUserMatchQualifier(SessionContext ctx, long value)
    {
        setUserMatchQualifier(ctx, Long.valueOf(value));
    }


    public void setUserMatchQualifier(long value)
    {
        setUserMatchQualifier(getSession().getSessionContext(), value);
    }
}
