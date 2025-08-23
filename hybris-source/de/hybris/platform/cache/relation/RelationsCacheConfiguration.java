package de.hybris.platform.cache.relation;

import de.hybris.platform.util.Config;
import java.util.Map;
import java.util.stream.Collectors;

public class RelationsCacheConfiguration
{
    static final String PROPERTY_DEFAULT_CAPACITY = "relation.cache.default.capacity";
    static final String PROPERTY_CAPACITY_PREFIX = "relation.cache.";
    static final String PROPERTY_CAPACITY_SUFFIX = ".capacity";
    private static final int DEFAULT_CAPACITY = 10000;
    private final int defaultCapacity;
    private final Map<String, Integer> capacities;


    public RelationsCacheConfiguration()
    {
        this.defaultCapacity = Config.getInt("relation.cache.default.capacity", 10000);
        this
                        .capacities = (Map<String, Integer>)Config.getParametersByPattern("relation.cache.").entrySet().stream().filter(entry -> ((String)entry.getKey()).endsWith(".capacity"))
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> Integer.valueOf(Integer.parseInt((String)entry.getValue()))));
    }


    public long getCapacityForRelation(String relation)
    {
        return ((Integer)this.capacities.getOrDefault(buildCapacityPropertyForRelation(relation), Integer.valueOf(this.defaultCapacity))).intValue();
    }


    private String buildCapacityPropertyForRelation(String relation)
    {
        return String.format("relation.cache.%s.capacity", new Object[] {relation});
    }
}
