package de.hybris.platform.fraud.jalo;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedProductOrderLimit extends GenericItem
{
    public static final String CODE = "code";
    public static final String INTERVALRESOLUTION = "intervalResolution";
    public static final String INTERVALVALUE = "intervalValue";
    public static final String INTERVALMAXORDERSNUMBER = "intervalMaxOrdersNumber";
    public static final String MAXNUMBERPERORDER = "maxNumberPerOrder";
    public static final String PRODUCTS = "products";
    protected static final OneToManyHandler<Product> PRODUCTSHANDLER = new OneToManyHandler(GeneratedCoreConstants.TC.PRODUCT, false, "productOrderLimit", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("intervalResolution", Item.AttributeMode.INITIAL);
        tmp.put("intervalValue", Item.AttributeMode.INITIAL);
        tmp.put("intervalMaxOrdersNumber", Item.AttributeMode.INITIAL);
        tmp.put("maxNumberPerOrder", Item.AttributeMode.INITIAL);
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


    public Integer getIntervalMaxOrdersNumber(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "intervalMaxOrdersNumber");
    }


    public Integer getIntervalMaxOrdersNumber()
    {
        return getIntervalMaxOrdersNumber(getSession().getSessionContext());
    }


    public int getIntervalMaxOrdersNumberAsPrimitive(SessionContext ctx)
    {
        Integer value = getIntervalMaxOrdersNumber(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getIntervalMaxOrdersNumberAsPrimitive()
    {
        return getIntervalMaxOrdersNumberAsPrimitive(getSession().getSessionContext());
    }


    protected void setIntervalMaxOrdersNumber(SessionContext ctx, Integer value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'intervalMaxOrdersNumber' is not changeable", 0);
        }
        setProperty(ctx, "intervalMaxOrdersNumber", value);
    }


    protected void setIntervalMaxOrdersNumber(Integer value)
    {
        setIntervalMaxOrdersNumber(getSession().getSessionContext(), value);
    }


    protected void setIntervalMaxOrdersNumber(SessionContext ctx, int value)
    {
        setIntervalMaxOrdersNumber(ctx, Integer.valueOf(value));
    }


    protected void setIntervalMaxOrdersNumber(int value)
    {
        setIntervalMaxOrdersNumber(getSession().getSessionContext(), value);
    }


    public EnumerationValue getIntervalResolution(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "intervalResolution");
    }


    public EnumerationValue getIntervalResolution()
    {
        return getIntervalResolution(getSession().getSessionContext());
    }


    protected void setIntervalResolution(SessionContext ctx, EnumerationValue value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'intervalResolution' is not changeable", 0);
        }
        setProperty(ctx, "intervalResolution", value);
    }


    protected void setIntervalResolution(EnumerationValue value)
    {
        setIntervalResolution(getSession().getSessionContext(), value);
    }


    public Integer getIntervalValue(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "intervalValue");
    }


    public Integer getIntervalValue()
    {
        return getIntervalValue(getSession().getSessionContext());
    }


    public int getIntervalValueAsPrimitive(SessionContext ctx)
    {
        Integer value = getIntervalValue(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getIntervalValueAsPrimitive()
    {
        return getIntervalValueAsPrimitive(getSession().getSessionContext());
    }


    protected void setIntervalValue(SessionContext ctx, Integer value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'intervalValue' is not changeable", 0);
        }
        setProperty(ctx, "intervalValue", value);
    }


    protected void setIntervalValue(Integer value)
    {
        setIntervalValue(getSession().getSessionContext(), value);
    }


    protected void setIntervalValue(SessionContext ctx, int value)
    {
        setIntervalValue(ctx, Integer.valueOf(value));
    }


    protected void setIntervalValue(int value)
    {
        setIntervalValue(getSession().getSessionContext(), value);
    }


    public Integer getMaxNumberPerOrder(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxNumberPerOrder");
    }


    public Integer getMaxNumberPerOrder()
    {
        return getMaxNumberPerOrder(getSession().getSessionContext());
    }


    public int getMaxNumberPerOrderAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxNumberPerOrder(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxNumberPerOrderAsPrimitive()
    {
        return getMaxNumberPerOrderAsPrimitive(getSession().getSessionContext());
    }


    protected void setMaxNumberPerOrder(SessionContext ctx, Integer value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'maxNumberPerOrder' is not changeable", 0);
        }
        setProperty(ctx, "maxNumberPerOrder", value);
    }


    protected void setMaxNumberPerOrder(Integer value)
    {
        setMaxNumberPerOrder(getSession().getSessionContext(), value);
    }


    protected void setMaxNumberPerOrder(SessionContext ctx, int value)
    {
        setMaxNumberPerOrder(ctx, Integer.valueOf(value));
    }


    protected void setMaxNumberPerOrder(int value)
    {
        setMaxNumberPerOrder(getSession().getSessionContext(), value);
    }


    public Set<Product> getProducts(SessionContext ctx)
    {
        return (Set<Product>)PRODUCTSHANDLER.getValues(ctx, (Item)this);
    }


    public Set<Product> getProducts()
    {
        return getProducts(getSession().getSessionContext());
    }


    public void setProducts(SessionContext ctx, Set<Product> value)
    {
        PRODUCTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setProducts(Set<Product> value)
    {
        setProducts(getSession().getSessionContext(), value);
    }


    public void addToProducts(SessionContext ctx, Product value)
    {
        PRODUCTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToProducts(Product value)
    {
        addToProducts(getSession().getSessionContext(), value);
    }


    public void removeFromProducts(SessionContext ctx, Product value)
    {
        PRODUCTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromProducts(Product value)
    {
        removeFromProducts(getSession().getSessionContext(), value);
    }
}
