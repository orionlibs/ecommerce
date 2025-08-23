package de.hybris.platform.util.config;

import de.hybris.bootstrap.util.ConfigParameterHelper;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FastHashMapConfig extends AbstractConfig
{
    private final Map<String, String> map;


    public FastHashMapConfig(Map<String, String> map)
    {
        this.map = new ConcurrentHashMap<>((int)(map.size() / 0.75F) + 1, 0.75F, 64);
        this.map.putAll(map);
    }


    public Map<String, String> getAllParameters()
    {
        return Collections.unmodifiableMap(this.map);
    }


    public String getParameter(String key)
    {
        return this.map.get(key);
    }


    public Map<String, String> getParametersMatching(String keyRegExp, boolean stripMatchingKey)
    {
        return ConfigParameterHelper.getParametersMatching(this.map, keyRegExp, stripMatchingKey);
    }


    public String setParameter(String key, String value)
    {
        if(value != null)
        {
            String prev = this.map.put(key, value);
            notifyListeners(key, prev, value);
            return prev;
        }
        return removeParameter(key);
    }


    public String removeParameter(String key)
    {
        String prev = this.map.remove(key);
        notifyListeners(key, prev, null);
        return prev;
    }
}
