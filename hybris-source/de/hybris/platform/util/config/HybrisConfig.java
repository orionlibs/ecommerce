package de.hybris.platform.util.config;

import de.hybris.platform.util.Config;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HybrisConfig extends AbstractConfig
{
    public static final String STANDALONE_PREFIX = "standalone";
    private final ConfigIntf config;
    private final boolean standaloneMode;


    public HybrisConfig(Properties platformProperties, boolean standaloneMode, int clusterNode)
    {
        this.standaloneMode = standaloneMode;
        this.config = buildConfig(platformProperties, clusterNode);
    }


    private ConfigIntf buildConfig(Properties platformProperties, int clusterNode)
    {
        Map<String, String> mapStandaloneCluster = new HashMap<>();
        Map<String, String> mapStandalone = new HashMap<>();
        Map<String, String> mapCluster = new HashMap<>();
        Map<String, String> mapFallback = new HashMap<>();
        Pattern pattern = Pattern.compile("^(standalone\\.)?(cluster\\.(\\d+)\\.)?(.*)$");
        String idStr = initClusterIdProperty(platformProperties, clusterNode);
        int count = 0;
        for(Object prop : platformProperties.keySet())
        {
            String key = (String)prop;
            String value = platformProperties.getProperty(key);
            Matcher matcher = pattern.matcher(key);
            if(matcher.matches())
            {
                if(matcher.group(1) != null)
                {
                    if(!this.standaloneMode)
                    {
                        continue;
                    }
                    if(matcher.group(2) != null)
                    {
                        if(!idStr.equals(matcher.group(3)))
                        {
                            continue;
                        }
                        mapStandaloneCluster.put(matcher.group(4), value);
                    }
                    else
                    {
                        mapStandalone.put(matcher.group(4), value);
                    }
                }
                if(matcher.group(2) != null)
                {
                    if(!idStr.equals(matcher.group(3)))
                    {
                        continue;
                    }
                    mapCluster.put(matcher.group(4), value);
                }
                else
                {
                    mapFallback.put(matcher.group(4), value);
                }
                count++;
                continue;
            }
            System.err.println("wrong config key '" + key + "'");
        }
        Map<String, String> merged = new HashMap<>(count);
        merged.putAll(mapFallback);
        merged.putAll(mapCluster);
        merged.putAll(mapStandalone);
        merged.putAll(mapStandaloneCluster);
        return (ConfigIntf)new FastHashMapConfig(merged);
    }


    private String initClusterIdProperty(Properties platformProperties, int clusterNode)
    {
        try
        {
            if(clusterNode != -1)
            {
                platformProperties.setProperty(Config.Params.CLUSTER_ID, String.valueOf(clusterNode));
            }
            return platformProperties.getProperty(Config.Params.CLUSTER_ID);
        }
        catch(NumberFormatException e)
        {
            throw new RuntimeException("cluster id invalid or not set");
        }
    }


    public Map<String, String> getAllParameters()
    {
        Map<Object, Object> newP = new HashMap<>();
        for(Map.Entry entry : this.config.getAllParameters().entrySet())
        {
            newP.put(entry.getKey(), entry.getValue());
        }
        return (Map)Collections.unmodifiableMap(newP);
    }


    public String getParameter(String key)
    {
        return this.config.getParameter(key);
    }


    public Map<String, String> getParametersMatching(String keyRegExp, boolean stripMatchingKey)
    {
        return this.config.getParametersMatching(keyRegExp, stripMatchingKey);
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
}
