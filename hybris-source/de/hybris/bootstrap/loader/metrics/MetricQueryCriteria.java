package de.hybris.bootstrap.loader.metrics;

import java.util.Set;

public class MetricQueryCriteria
{
    private final ResourceType resourceType;
    private final EventType eventType;
    private final boolean sortAsc;
    private final int minimumMatching;
    private final Set<String> excludedSuffixes;
    public static final MetricQueryCriteria FIND_ALL = query().build();


    public MetricQueryCriteria(MetricQueryCriteriaBuilder builder)
    {
        this.resourceType = builder.resourceType;
        this.eventType = builder.eventType;
        this.sortAsc = builder.sortAsc;
        this.minimumMatching = builder.minimumMatching;
        this.excludedSuffixes = builder.excludedSuffixes;
    }


    public ResourceType getResourceType()
    {
        return this.resourceType;
    }


    public EventType getEventType()
    {
        return this.eventType;
    }


    public boolean isSortAsc()
    {
        return this.sortAsc;
    }


    public boolean isSortDesc()
    {
        return !this.sortAsc;
    }


    public int getMinimumMatching()
    {
        return this.minimumMatching;
    }


    public Set<String> getExcludedSuffixes()
    {
        return this.excludedSuffixes;
    }


    public static MetricQueryCriteriaBuilder query()
    {
        return new MetricQueryCriteriaBuilder();
    }
}
