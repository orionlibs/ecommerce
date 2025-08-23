package de.hybris.bootstrap.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TenantInfo
{
    public static final String TENANT_WEBROOT_CONFIG_REGEXP_PATTERN = "([^\\.]+)\\.webroot";
    private final String tenantId;
    private final Map<String, String> tenantProperties;
    private final Map<String, String> extension2Webroot;
    private final Map<String, String> webroot2Extension;
    private final Set<String> webroots;


    public TenantInfo(String tenantId, Map<String, String> tenantProperties)
    {
        this.tenantId = tenantId;
        this.tenantProperties = Collections.unmodifiableMap(tenantProperties);
        this.extension2Webroot = Collections.unmodifiableMap(extractWebMappings(tenantProperties));
        this.webroot2Extension = Collections.unmodifiableMap(invertMap(this.extension2Webroot));
        this.webroots = Collections.unmodifiableSet(new LinkedHashSet<>(this.extension2Webroot.values()));
    }


    private static Map<String, String> invertMap(Map<String, String> map)
    {
        if(map.isEmpty())
        {
            return map;
        }
        Map<String, String> out = new HashMap<>(map.size());
        for(Map.Entry<String, String> entry : map.entrySet())
        {
            out.put(entry.getValue(), entry.getKey());
        }
        return out;
    }


    private Map<String, String> extractWebMappings(Map<String, String> tenantProperties)
    {
        Map<String, String> ret = new LinkedHashMap<>();
        Pattern pattern = Pattern.compile("([^\\.]+)\\.webroot", 2);
        for(Map.Entry<String, String> e : tenantProperties.entrySet())
        {
            String prop = e.getKey();
            Matcher matcher = pattern.matcher(prop);
            if(matcher.matches())
            {
                String extName = matcher.group(1);
                String webroot = e.getValue();
                ret.put(extName, webroot);
            }
        }
        return ret;
    }


    public String getTenantId()
    {
        return this.tenantId;
    }


    public Properties getTenantProperties()
    {
        Properties props = new Properties();
        props.putAll(this.tenantProperties);
        return props;
    }


    public String getWebMapping(String extName)
    {
        return this.extension2Webroot.get(extName);
    }


    public String getExtensionForWebroot(String webroot)
    {
        return this.webroot2Extension.get(webroot);
    }


    public boolean isWebrootOwner(String webroot)
    {
        return this.webroots.contains(webroot);
    }
}
