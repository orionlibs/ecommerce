package de.hybris.platform.variants.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedVariantValueCategory extends Category
{
    public static final String SEQUENCE = "sequence";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Category.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("sequence", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getSequence(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "sequence");
    }


    public Integer getSequence()
    {
        return getSequence(getSession().getSessionContext());
    }


    public int getSequenceAsPrimitive(SessionContext ctx)
    {
        Integer value = getSequence(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getSequenceAsPrimitive()
    {
        return getSequenceAsPrimitive(getSession().getSessionContext());
    }


    public void setSequence(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "sequence", value);
    }


    public void setSequence(Integer value)
    {
        setSequence(getSession().getSessionContext(), value);
    }


    public void setSequence(SessionContext ctx, int value)
    {
        setSequence(ctx, Integer.valueOf(value));
    }


    public void setSequence(int value)
    {
        setSequence(getSession().getSessionContext(), value);
    }
}
