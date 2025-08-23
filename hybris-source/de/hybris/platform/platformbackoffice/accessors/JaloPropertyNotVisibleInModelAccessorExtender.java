package de.hybris.platform.platformbackoffice.accessors;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class JaloPropertyNotVisibleInModelAccessorExtender
{
    private Map<String, Set<Class>> additionalJaloAttributes;
    private Map<String, Set<Class>> initialJaloAttributes;
    private JaloPropertyNotVisibleInModelAccessor accessor;


    @Required
    public void setAdditionalJaloAttributes(Map<String, Set<Class>> additionalJaloAttributes)
    {
        this.additionalJaloAttributes = additionalJaloAttributes;
    }


    @Required
    public void setAccessor(JaloPropertyNotVisibleInModelAccessor accessor)
    {
        this.accessor = accessor;
    }


    public void addJaloAttributesFromExtension()
    {
        this.initialJaloAttributes = new HashMap<>();
        for(Map.Entry<String, Set<Class>> entry : (Iterable<Map.Entry<String, Set<Class>>>)this.accessor.getSupportedJaloAttributes().entrySet())
        {
            this.initialJaloAttributes.put(entry.getKey(), (Set)new HashSet<>((Collection<? extends Class<?>>)entry.getValue()));
        }
        Map<String, Set<Class>> currentJaloAttributes = mergeMaps(this.initialJaloAttributes, this.additionalJaloAttributes);
        this.accessor.setSupportedJaloAttributes(currentJaloAttributes);
    }


    public void removeJaloAttributesFromExtension()
    {
        this.accessor.setSupportedJaloAttributes(this.initialJaloAttributes);
    }


    private Map<String, Set<Class>> mergeMaps(Map<String, Set<Class>> firstMap, Map<String, Set<Class>> secondMap)
    {
        Map<String, Set<Class>> result = new HashMap<>();
        if(firstMap != null)
        {
            for(Map.Entry<String, Set<Class>> entry : firstMap.entrySet())
            {
                result.put(entry.getKey(), (Set)new HashSet<>((Collection<? extends Class<?>>)entry.getValue()));
            }
        }
        if(secondMap != null)
        {
            for(Map.Entry<String, Set<Class>> entry : secondMap.entrySet())
            {
                if(result.containsKey(entry.getKey()))
                {
                    ((Set)result.get(entry.getKey())).addAll(entry.getValue());
                    continue;
                }
                result.put(entry.getKey(), (Set)new HashSet<>((Collection<? extends Class<?>>)entry.getValue()));
            }
        }
        return result;
    }
}
