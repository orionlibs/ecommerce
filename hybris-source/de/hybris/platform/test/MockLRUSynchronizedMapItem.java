package de.hybris.platform.test;

import com.google.common.cache.CacheBuilder;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MockLRUSynchronizedMapItem extends Item
{
    private static final Logger LOG = LoggerFactory.getLogger(MockLRUSynchronizedMapItem.class);
    protected static final ConcurrentMap<String, Map<String, Object>> staticTransientObjects = CacheBuilder.newBuilder()
                    .maximumSize(3L)
                    .build()
                    .asMap();
    final String name;


    public MockLRUSynchronizedMapItem(String name)
    {
        this.name = name;
    }


    public String toString()
    {
        return this.name;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes)
    {
        return null;
    }


    public void setTransientObject(String key, Object value)
    {
        LOG.debug(toString() + " started " + toString() + "--" + key);
        while(true)
        {
            ConcurrentMap<String, Object> oldMap = (ConcurrentMap<String, Object>)staticTransientObjects.get(key);
            ConcurrentMap<String, Object> newMap = (oldMap == null) ? new ConcurrentHashMap<>() : new ConcurrentHashMap<>(oldMap);
            if(value == null)
            {
                newMap.remove(key);
            }
            else
            {
                newMap.put(key, value);
            }
            if(newMap.isEmpty() && oldMap == null)
            {
                LOG.debug(toString() + " Key: " + toString() + " value: null and oldMap null");
                return;
            }
            if(newMap.isEmpty() && staticTransientObjects.remove(key, oldMap))
            {
                LOG.debug(toString() + " Key: " + toString() + " removed with value: " + key);
                return;
            }
            if(oldMap == null && staticTransientObjects.putIfAbsent(key, newMap) == null)
            {
                LOG.debug(toString() + " Key: " + toString() + " added with value: " + key);
                return;
            }
            if(oldMap != null && staticTransientObjects.replace(key, oldMap, newMap))
            {
                LOG.debug(toString() + " Key: " + toString() + " replaced with value: " + key);
                return;
            }
            LOG.debug(toString() + " race occurred for key: " + toString());
        }
    }


    public Object getTransientObject(String key)
    {
        Map<String, Object> transientObjects = staticTransientObjects.get(key);
        if(transientObjects == null)
        {
            return null;
        }
        return transientObjects.get(key);
    }
}
