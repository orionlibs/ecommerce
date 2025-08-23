package de.hybris.bootstrap.loader.metrics;

import java.util.Map;

public class ClassLoaderMetric
{
    private final String name;
    private final String classloader;
    private final String source;
    private final ResourceType resourceType;
    private final Map<EventType, Integer> eventTypeCount;
    private final Map<String, Integer> ignoredByRulesCount;


    private ClassLoaderMetric(ClassLoaderMetricBuilder builder)
    {
        this.name = builder.name;
        this.classloader = builder.classloader;
        this.source = builder.source;
        this.resourceType = builder.resourceType;
        this.eventTypeCount = builder.eventTypeCount;
        this.ignoredByRulesCount = builder.ignoredByRulesCount;
    }


    public String getName()
    {
        return this.name;
    }


    public String getClassloader()
    {
        return this.classloader;
    }


    public String getSource()
    {
        return this.source;
    }


    public ResourceType getResourceType()
    {
        return this.resourceType;
    }


    public int getEventTypeCount(EventType eventType)
    {
        Integer eventCount = this.eventTypeCount.get(eventType);
        return (eventCount != null) ? eventCount.intValue() : 0;
    }


    public Map<EventType, Integer> getEventTypeCount()
    {
        return this.eventTypeCount;
    }


    public Map<String, Integer> getIgnoredByRulesCount()
    {
        return this.ignoredByRulesCount;
    }


    public static ClassLoaderMetricBuilder builder()
    {
        return new ClassLoaderMetricBuilder();
    }
}
