package de.hybris.platform.basecommerce.jalo.externaltax;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedProductTaxCode extends GenericItem
{
    public static final String PRODUCTCODE = "productCode";
    public static final String TAXAREA = "taxArea";
    public static final String TAXCODE = "taxCode";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("productCode", Item.AttributeMode.INITIAL);
        tmp.put("taxArea", Item.AttributeMode.INITIAL);
        tmp.put("taxCode", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getProductCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "productCode");
    }


    public String getProductCode()
    {
        return getProductCode(getSession().getSessionContext());
    }


    protected void setProductCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'productCode' is not changeable", 0);
        }
        setProperty(ctx, "productCode", value);
    }


    protected void setProductCode(String value)
    {
        setProductCode(getSession().getSessionContext(), value);
    }


    public String getTaxArea(SessionContext ctx)
    {
        return (String)getProperty(ctx, "taxArea");
    }


    public String getTaxArea()
    {
        return getTaxArea(getSession().getSessionContext());
    }


    protected void setTaxArea(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'taxArea' is not changeable", 0);
        }
        setProperty(ctx, "taxArea", value);
    }


    protected void setTaxArea(String value)
    {
        setTaxArea(getSession().getSessionContext(), value);
    }


    public String getTaxCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "taxCode");
    }


    public String getTaxCode()
    {
        return getTaxCode(getSession().getSessionContext());
    }


    public void setTaxCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "taxCode", value);
    }


    public void setTaxCode(String value)
    {
        setTaxCode(getSession().getSessionContext(), value);
    }
}
