package de.hybris.platform.jalo.user;

import de.hybris.platform.jalo.Item;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedEmployee extends User
{
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(User.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }
}
