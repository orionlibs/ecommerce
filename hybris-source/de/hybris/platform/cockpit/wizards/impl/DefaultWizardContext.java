package de.hybris.platform.cockpit.wizards.impl;

import de.hybris.platform.cockpit.wizards.MutableWizardContext;
import java.util.HashMap;
import java.util.Map;

public class DefaultWizardContext implements MutableWizardContext
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


    public Map<String, Object> getAttributes()
    {
        return this.attributes;
    }


    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, Object> entry : this.attributes.entrySet())
        {
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return builder.toString();
    }
}
