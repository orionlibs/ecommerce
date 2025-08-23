package de.hybris.platform.impex.jalo.exp;

import de.hybris.platform.jalo.Item;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedReport extends Export
{
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Export.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }
}
