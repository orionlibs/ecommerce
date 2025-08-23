package de.hybris.bootstrap.loader.metrics;

import java.security.CodeSource;

public class ClassLoaderMetricEvent
{
    private final String name;
    private final String classloader;
    private final String source;
    private final EventType eventType;
    private final ResourceType resourceType;
    private final String rejectedByRule;


    private ClassLoaderMetricEvent(ClassloaderMetricEventBuilder builder)
    {
        this.name = builder.name;
        this.classloader = builder.classloader;
        this.source = builder.source;
        this.eventType = builder.eventType;
        this.resourceType = builder.resourceType;
        this.rejectedByRule = builder.rejectedByRule;
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


    public EventType getEventType()
    {
        return this.eventType;
    }


    public ResourceType getResourceType()
    {
        return this.resourceType;
    }


    public String getRejectedByRule()
    {
        return this.rejectedByRule;
    }


    public static ClassloaderMetricEventBuilder forResource()
    {
        return (new ClassloaderMetricEventBuilder()).forResourceType(ResourceType.RESOURCE);
    }


    public static ClassloaderMetricEventBuilder forType(ResourceType resourceType)
    {
        return (new ClassloaderMetricEventBuilder()).forResourceType(resourceType);
    }


    public static ClassloaderMetricEventBuilder forClass()
    {
        return (new ClassloaderMetricEventBuilder()).forResourceType(ResourceType.CLASS);
    }


    public static ClassloaderMetricEventBuilder forClass(Class clazz)
    {
        String classLoader = null;
        if(clazz.getClassLoader() != null)
        {
            classLoader = clazz.getClassLoader().getClass().getName();
        }
        String source = null;
        CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
        if(codeSource != null)
        {
            source = codeSource.getLocation().getFile();
        }
        return (new ClassloaderMetricEventBuilder())
                        .forResourceType(ResourceType.CLASS)
                        .withName(clazz.getName())
                        .withClassloader(classLoader)
                        .withSource(source);
    }
}
