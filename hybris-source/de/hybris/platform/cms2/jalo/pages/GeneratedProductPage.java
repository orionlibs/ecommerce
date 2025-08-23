package de.hybris.platform.cms2.jalo.pages;

import de.hybris.platform.jalo.Item;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedProductPage extends AbstractPage
{
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractPage.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }
}
