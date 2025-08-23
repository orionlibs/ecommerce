package de.hybris.platform.core;

import de.hybris.platform.util.ObjectAdapter;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractObjectAdapter implements ObjectAdapter
{
    protected final Object impl;
    private Map transientObjects;


    public AbstractObjectAdapter(Object implementation)
    {
        this.impl = implementation;
    }


    public Object getObject()
    {
        return this.impl;
    }


    public Map getTransientObjectMap()
    {
        if(this.transientObjects == null)
        {
            this.transientObjects = new HashMap<>();
        }
        return this.transientObjects;
    }


    public Object getTransientObject(String key)
    {
        if(this.transientObjects == null)
        {
            return null;
        }
        return getTransientObjectMap().get(key);
    }


    public void setTransientObject(String key, Object value)
    {
        if(value == null)
        {
            getTransientObjectMap().remove(key);
        }
        else
        {
            getTransientObjectMap().put(key, value);
        }
    }
}
