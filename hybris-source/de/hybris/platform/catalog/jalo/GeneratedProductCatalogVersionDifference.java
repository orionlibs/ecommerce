package de.hybris.platform.catalog.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedProductCatalogVersionDifference extends CatalogVersionDifference
{
    public static final String SOURCEPRODUCT = "sourceProduct";
    public static final String TARGETPRODUCT = "targetProduct";
    public static final String MODE = "mode";
    public static final String SOURCEPRODUCTAPPROVALSTATUS = "sourceProductApprovalStatus";
    public static final String TARGETPRODUCTAPPROVALSTATUS = "targetProductApprovalStatus";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CatalogVersionDifference.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("sourceProduct", Item.AttributeMode.INITIAL);
        tmp.put("targetProduct", Item.AttributeMode.INITIAL);
        tmp.put("mode", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getMode(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "mode");
    }


    public EnumerationValue getMode()
    {
        return getMode(getSession().getSessionContext());
    }


    protected void setMode(SessionContext ctx, EnumerationValue value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'mode' is not changeable", 0);
        }
        setProperty(ctx, "mode", value);
    }


    protected void setMode(EnumerationValue value)
    {
        setMode(getSession().getSessionContext(), value);
    }


    public Product getSourceProduct(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "sourceProduct");
    }


    public Product getSourceProduct()
    {
        return getSourceProduct(getSession().getSessionContext());
    }


    protected void setSourceProduct(SessionContext ctx, Product value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'sourceProduct' is not changeable", 0);
        }
        setProperty(ctx, "sourceProduct", value);
    }


    protected void setSourceProduct(Product value)
    {
        setSourceProduct(getSession().getSessionContext(), value);
    }


    public EnumerationValue getSourceProductApprovalStatus()
    {
        return getSourceProductApprovalStatus(getSession().getSessionContext());
    }


    public Product getTargetProduct(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "targetProduct");
    }


    public Product getTargetProduct()
    {
        return getTargetProduct(getSession().getSessionContext());
    }


    protected void setTargetProduct(SessionContext ctx, Product value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'targetProduct' is not changeable", 0);
        }
        setProperty(ctx, "targetProduct", value);
    }


    protected void setTargetProduct(Product value)
    {
        setTargetProduct(getSession().getSessionContext(), value);
    }


    public EnumerationValue getTargetProductApprovalStatus()
    {
        return getTargetProductApprovalStatus(getSession().getSessionContext());
    }


    public abstract EnumerationValue getSourceProductApprovalStatus(SessionContext paramSessionContext);


    public abstract EnumerationValue getTargetProductApprovalStatus(SessionContext paramSessionContext);
}
