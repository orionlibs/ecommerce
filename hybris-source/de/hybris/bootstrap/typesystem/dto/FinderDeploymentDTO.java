package de.hybris.bootstrap.typesystem.dto;

import java.util.List;
import java.util.Map;

public class FinderDeploymentDTO
{
    private final String extensionName;
    private final String beanName;
    private final String name;
    private final List<String> sig;
    private final Map<String, String> dbMethodMappings;
    private final boolean cache;


    public FinderDeploymentDTO(String extensionName, String beanName, String name, List<String> sig, Map<String, String> dbMethodMappings, boolean cache)
    {
        this.extensionName = extensionName;
        this.beanName = beanName;
        this.name = name;
        this.sig = sig;
        this.dbMethodMappings = dbMethodMappings;
        this.cache = cache;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public String getBeanName()
    {
        return this.beanName;
    }


    public String getName()
    {
        return this.name;
    }


    public List<String> getSig()
    {
        return this.sig;
    }


    public Map<String, String> getDbMethodMappings()
    {
        return this.dbMethodMappings;
    }


    public boolean isCache()
    {
        return this.cache;
    }
}
