package de.hybris.platform.cockpit.services.search.impl;

import de.hybris.platform.cockpit.services.search.ConditionTranslatorContext;
import java.util.HashMap;
import java.util.Map;

public class AbstractConditionTranslatorContext implements ConditionTranslatorContext
{
    private final Map<String, Object> attributes = new HashMap<>();


    public Object getAttribute(String key)
    {
        return this.attributes.get(key);
    }


    public void setAttribute(String key, Object value)
    {
        this.attributes.put(key, value);
    }
}
