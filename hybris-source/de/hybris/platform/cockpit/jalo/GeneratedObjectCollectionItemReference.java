package de.hybris.platform.cockpit.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedObjectCollectionItemReference extends ObjectCollectionElement
{
    public static final String ITEM = "item";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(ObjectCollectionElement.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("item", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Item getItem(SessionContext ctx)
    {
        return (Item)getProperty(ctx, "item");
    }


    public Item getItem()
    {
        return getItem(getSession().getSessionContext());
    }


    public void setItem(SessionContext ctx, Item value)
    {
        setProperty(ctx, "item", value);
    }


    public void setItem(Item value)
    {
        setItem(getSession().getSessionContext(), value);
    }
}
