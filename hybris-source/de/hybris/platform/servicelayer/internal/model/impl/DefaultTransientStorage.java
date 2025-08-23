package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import java.util.HashMap;
import java.util.Objects;

public class DefaultTransientStorage implements InterceptorContext.TransientStorage
{
    private final HashMap<Object, Object> storage = new HashMap<>();


    public Object get(Object key)
    {
        return this.storage.get(Objects.requireNonNull(key, "key mustn't be null."));
    }


    public void put(Object key, Object value)
    {
        this.storage.put(Objects.requireNonNull(key, "key mustn't be null."),
                        Objects.requireNonNull(value, "value mustn't be null."));
    }


    public boolean contains(Object key)
    {
        return this.storage.containsKey(Objects.requireNonNull(key, "key mustn't be null."));
    }


    public void remove(Object key)
    {
        this.storage.remove(Objects.requireNonNull(key, "key mustn't be null."));
    }
}
