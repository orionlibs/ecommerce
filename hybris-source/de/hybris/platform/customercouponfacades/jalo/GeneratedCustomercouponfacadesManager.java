package de.hybris.platform.customercouponfacades.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.extension.Extension;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCustomercouponfacadesManager extends Extension
{
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public String getName()
    {
        return "customercouponfacades";
    }
}
