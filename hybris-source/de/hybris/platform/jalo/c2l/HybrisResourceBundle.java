package de.hybris.platform.jalo.c2l;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class HybrisResourceBundle extends ResourceBundle implements Serializable
{
    static final long serialVersionUID = 3734128553292213588L;
    private final Map resourceMap;
    private final Locale locale;


    public HybrisResourceBundle(Locale locale)
    {
        this.resourceMap = new HashMap<>();
        this.locale = locale;
    }


    protected Object handleGetObject(String key) throws MissingResourceException
    {
        if(this.resourceMap.containsKey(key))
        {
            return this.resourceMap.get(key);
        }
        return null;
    }


    public Enumeration getKeys()
    {
        return Collections.enumeration(this.resourceMap.keySet());
    }


    public void setParent(ResourceBundle parent)
    {
        if(!(parent instanceof HybrisResourceBundle))
        {
            throw new IllegalArgumentException();
        }
        super.setParent(parent);
    }


    public Locale getLocale()
    {
        return this.locale;
    }


    public void setObject(String key, Serializable value)
    {
        if(key == null)
        {
            throw new IllegalArgumentException();
        }
        if(value == null)
        {
            if(this.resourceMap.containsKey(key))
            {
                this.resourceMap.remove(key);
            }
            return;
        }
        this.resourceMap.put(key, value);
    }


    public void setString(String key, String value)
    {
        setObject(key, value);
    }
}
