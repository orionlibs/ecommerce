package de.hybris.platform.webservicescommons.jaxb.adapters;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class VariableMapAdapter extends XmlAdapter<VariableMapAdapter.AdaptedMap, Map<String, Object>>
{
    public AdaptedMap marshal(Map<String, Object> map)
    {
        AdaptedMap adaptedMap = new AdaptedMap();
        for(Map.Entry<String, Object> entry : map.entrySet())
        {
            if(entry.getValue() instanceof Collection)
            {
                for(Object val : entry.getValue())
                {
                    addEntry(adaptedMap, entry.getKey(), val);
                }
                continue;
            }
            addEntry(adaptedMap, entry.getKey(), entry.getValue());
        }
        return adaptedMap;
    }


    protected void addEntry(AdaptedMap adaptedMap, String key, Object value)
    {
        AdaptedEntry adaptedEntry = new AdaptedEntry();
        adaptedEntry.key = key;
        adaptedEntry.value = value;
        adaptedMap.entries.add(adaptedEntry);
    }


    public Map<String, Object> unmarshal(AdaptedMap adaptedMap)
    {
        List<AdaptedEntry> adaptedEntries = adaptedMap.entries;
        Map<String, Object> map = new HashMap<>(adaptedEntries.size());
        for(AdaptedEntry adaptedEntry : adaptedEntries)
        {
            map.compute(adaptedEntry.key, (k, v) -> {
                if(v == null)
                {
                    return adaptedEntry.value;
                }
                if(v instanceof Collection)
                {
                    ((Collection<String>)v).add(adaptedEntry.key);
                    return v;
                }
                return Lists.newArrayList(new Object[] {v, adaptedEntry.key});
            });
        }
        return map;
    }
}
