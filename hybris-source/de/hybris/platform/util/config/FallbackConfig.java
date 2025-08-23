package de.hybris.platform.util.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class FallbackConfig extends AbstractConfig
{
    private final ConfigIntf config;
    private final ConfigIntf fallback;


    public FallbackConfig(ConfigIntf config, ConfigIntf fallback)
    {
        this.config = config;
        this.fallback = fallback;
    }


    public ConfigIntf getConfigWithoutFallback()
    {
        return this.config;
    }


    public String getParameter(String key)
    {
        String val = this.config.getParameter(key);
        if(val == null)
        {
            val = this.fallback.getParameter(key);
        }
        return val;
    }


    public Map<String, String> getParametersMatching(String keyRegExp, boolean stripMatchingKey)
    {
        Map<String, String> parametersMatching = new HashMap<>(this.fallback.getParametersMatching(keyRegExp, stripMatchingKey));
        parametersMatching.putAll(this.config.getParametersMatching(keyRegExp, stripMatchingKey));
        return parametersMatching;
    }


    public String setParameter(String key, String value)
    {
        String prev = this.config.setParameter(key, value);
        notifyListeners(key, prev, value);
        return prev;
    }


    public String removeParameter(String key)
    {
        String prev = this.config.removeParameter(key);
        notifyListeners(key, prev, null);
        return prev;
    }


    public Map<String, String> getAllParameters()
    {
        Map<String, String> ret = new TreeMap<>(this.fallback.getAllParameters());
        ret.putAll(this.config.getAllParameters());
        return Collections.unmodifiableMap(ret);
    }
}
