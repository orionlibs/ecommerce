package de.hybris.platform.spring;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.ObjectFactory;

class ScopeMap
{
    private static final Logger log = Logger.getLogger(ScopeMap.class);
    private final Map<String, Object> beans;
    private final Map<String, Runnable> destructionCallbacks;


    ScopeMap(int initialCapacity)
    {
        this(new HashMap<>(initialCapacity), new HashMap<>(10));
    }


    protected ScopeMap(Map<String, Object> beans, Map<String, Runnable> destructionCallbacks)
    {
        this.beans = beans;
        this.destructionCallbacks = destructionCallbacks;
    }


    Object get(String name, ObjectFactory<?> factory)
    {
        Object ret = lookup(name);
        if(ret == null)
        {
            ret = factory.getObject();
            put(name, ret);
            if(log.isDebugEnabled())
            {
                log.debug("Created new object for " + name);
            }
        }
        else if(log.isDebugEnabled())
        {
            log.debug("Returned existing object for " + name);
        }
        return ret;
    }


    Object lookup(String name)
    {
        return this.beans.get(name);
    }


    void put(String name, Object value)
    {
        this.beans.put(name, value);
    }


    Object remove(String name)
    {
        removeDestructionCallback(name);
        return removeBean(name);
    }


    protected Object removeBean(String name)
    {
        return this.beans.remove(name);
    }


    protected void removeDestructionCallback(String name)
    {
        this.destructionCallbacks.remove(name);
    }


    void addDestructionCallback(String name, Runnable callback)
    {
        this.destructionCallbacks.put(name, callback);
    }


    void performDestructionCallbacks()
    {
        for(Map.Entry<String, Runnable> e : this.destructionCallbacks.entrySet())
        {
            ((Runnable)e.getValue()).run();
        }
        this.destructionCallbacks.clear();
    }


    int size()
    {
        return this.beans.size();
    }
}
