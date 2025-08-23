package de.hybris.platform.variants.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedVariantCategory extends Category
{
    public static final String HASIMAGE = "hasImage";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Category.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("hasImage", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isHasImage(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "hasImage");
    }


    public Boolean isHasImage()
    {
        return isHasImage(getSession().getSessionContext());
    }


    public boolean isHasImageAsPrimitive(SessionContext ctx)
    {
        Boolean value = isHasImage(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isHasImageAsPrimitive()
    {
        return isHasImageAsPrimitive(getSession().getSessionContext());
    }


    public void setHasImage(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "hasImage", value);
    }


    public void setHasImage(Boolean value)
    {
        setHasImage(getSession().getSessionContext(), value);
    }


    public void setHasImage(SessionContext ctx, boolean value)
    {
        setHasImage(ctx, Boolean.valueOf(value));
    }


    public void setHasImage(boolean value)
    {
        setHasImage(getSession().getSessionContext(), value);
    }
}
