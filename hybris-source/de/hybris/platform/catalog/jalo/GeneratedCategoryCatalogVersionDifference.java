package de.hybris.platform.catalog.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCategoryCatalogVersionDifference extends CatalogVersionDifference
{
    public static final String SOURCECATEGORY = "sourceCategory";
    public static final String TARGETCATEGORY = "targetCategory";
    public static final String MODE = "mode";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CatalogVersionDifference.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("sourceCategory", Item.AttributeMode.INITIAL);
        tmp.put("targetCategory", Item.AttributeMode.INITIAL);
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


    public Category getSourceCategory(SessionContext ctx)
    {
        return (Category)getProperty(ctx, "sourceCategory");
    }


    public Category getSourceCategory()
    {
        return getSourceCategory(getSession().getSessionContext());
    }


    protected void setSourceCategory(SessionContext ctx, Category value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'sourceCategory' is not changeable", 0);
        }
        setProperty(ctx, "sourceCategory", value);
    }


    protected void setSourceCategory(Category value)
    {
        setSourceCategory(getSession().getSessionContext(), value);
    }


    public Category getTargetCategory(SessionContext ctx)
    {
        return (Category)getProperty(ctx, "targetCategory");
    }


    public Category getTargetCategory()
    {
        return getTargetCategory(getSession().getSessionContext());
    }


    protected void setTargetCategory(SessionContext ctx, Category value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'targetCategory' is not changeable", 0);
        }
        setProperty(ctx, "targetCategory", value);
    }


    protected void setTargetCategory(Category value)
    {
        setTargetCategory(getSession().getSessionContext(), value);
    }
}
