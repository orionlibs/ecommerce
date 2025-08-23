package de.hybris.platform.webservicescommons.mapping.config;

import java.util.Map;

public class FieldSetLevelMapping
{
    private Class dtoClass;
    private Map<String, String> levelMapping;


    public Class getDtoClass()
    {
        return this.dtoClass;
    }


    public void setDtoClass(Class dtoClass)
    {
        this.dtoClass = dtoClass;
    }


    public Map<String, String> getLevelMapping()
    {
        return this.levelMapping;
    }


    public void setLevelMapping(Map<String, String> levelMapping)
    {
        this.levelMapping = levelMapping;
    }
}
