package de.hybris.bootstrap.typesystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.CaseInsensitiveMap;

public class YFinder extends YDeploymentElement
{
    private final String name;
    private final List<String> signatureClassNames;
    private Map<String, String> dbQueryMappings;
    private boolean cache;


    public YFinder(YNamespace container, String deploymentName, String name, List<String> signatureClassNames)
    {
        super(container, deploymentName);
        this.name = name;
        this.signatureClassNames = new ArrayList<>(signatureClassNames);
    }


    public void validate()
    {
        super.validate();
        getSignature();
    }


    public boolean isCached()
    {
        return this.cache;
    }


    public void setCached(boolean cached)
    {
        this.cache = cached;
    }


    public String getName()
    {
        return this.name;
    }


    public String getQuery(String dbName)
    {
        if(this.dbQueryMappings == null)
        {
            return null;
        }
        String query = this.dbQueryMappings.get(dbName);
        return (query != null) ? query : this.dbQueryMappings.get(null);
    }


    public void addQueryMapping(String dbName, String query)
    {
        if(this.dbQueryMappings != null && this.dbQueryMappings.containsKey(dbName))
        {
            throw new IllegalArgumentException("finder " + toString() + " already got mapping for db '" + dbName + "'='" + (String)this.dbQueryMappings
                            .get(dbName) + "' - cannot map to '" + query + "'");
        }
        if(this.dbQueryMappings == null)
        {
            this.dbQueryMappings = (Map<String, String>)new CaseInsensitiveMap();
        }
        this.dbQueryMappings.put(dbName, query);
    }


    public List<String> getSignatureClassNames()
    {
        return this.signatureClassNames;
    }


    public List<Class> getSignature()
    {
        List<Class<?>> ret = Collections.EMPTY_LIST;
        if(this.signatureClassNames != null && !this.signatureClassNames.isEmpty())
        {
            ret = new ArrayList<>(this.signatureClassNames.size());
            for(String className : this.signatureClassNames)
            {
                ret.add(getTypeSystem().resolveClass(this, YDeployment.convertNativeTypes(className)));
            }
        }
        return ret;
    }


    public Map<String, String> getNonDefaultQueryMappings()
    {
        Map<String, String> ret = null;
        if(this.dbQueryMappings != null && this.dbQueryMappings.isEmpty())
        {
            for(Map.Entry<String, String> entry : this.dbQueryMappings.entrySet())
            {
                if(entry.getKey() == null)
                {
                    continue;
                }
                if(ret == null)
                {
                    ret = new LinkedHashMap<>();
                }
                ret.put(entry.getKey(), entry.getValue());
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_MAP;
    }
}
