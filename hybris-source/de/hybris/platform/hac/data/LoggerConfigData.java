package de.hybris.platform.hac.data;

import org.apache.logging.log4j.Level;

public class LoggerConfigData
{
    private String name;
    private String parentName;
    private Level effectiveLevel;


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getParentName()
    {
        return this.parentName;
    }


    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }


    public Level getEffectiveLevel()
    {
        return this.effectiveLevel;
    }


    public void setEffectiveLevel(Level effectiveLevel)
    {
        this.effectiveLevel = effectiveLevel;
    }
}
