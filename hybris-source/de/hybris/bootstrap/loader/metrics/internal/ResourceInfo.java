package de.hybris.bootstrap.loader.metrics.internal;

import de.hybris.bootstrap.loader.metrics.ResourceType;

public class ResourceInfo
{
    private final String name;
    private final String location;
    private final String classloader;
    private final ResourceType type;


    public ResourceInfo(String name, String location, String classloader, ResourceType type)
    {
        this.name = name;
        this.location = location;
        this.classloader = classloader;
        this.type = type;
    }


    public String getName()
    {
        return this.name;
    }


    public String getLocation()
    {
        return this.location;
    }


    public String getClassLoader()
    {
        return this.classloader;
    }


    public ResourceType getType()
    {
        return this.type;
    }
}
