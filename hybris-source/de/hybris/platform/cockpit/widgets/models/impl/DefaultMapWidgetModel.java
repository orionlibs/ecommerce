package de.hybris.platform.cockpit.widgets.models.impl;

import de.hybris.platform.cockpit.widgets.models.MapWidgetModel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultMapWidgetModel<K, V> extends AbstractWidgetModel implements MapWidgetModel<K, V>
{
    private final Map<K, V> map = new HashMap<>();


    public V get(K key)
    {
        return this.map.get(key);
    }


    public boolean put(K key, V value)
    {
        boolean changed = false;
        V oldVal = this.map.get(key);
        if((oldVal != null && !oldVal.equals(value)) || (oldVal == null && value != null))
        {
            this.map.put(key, value);
            changed = true;
        }
        return changed;
    }


    public boolean setMap(Map<K, V> map)
    {
        boolean changed = false;
        if(!this.map.equals(map))
        {
            this.map.clear();
            this.map.putAll(map);
            changed = true;
        }
        return changed;
    }


    public Map<K, V> getMap()
    {
        return Collections.unmodifiableMap(this.map);
    }


    public V remove(K key)
    {
        return this.map.remove(key);
    }
}
