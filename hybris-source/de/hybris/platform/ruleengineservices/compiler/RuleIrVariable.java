package de.hybris.platform.ruleengineservices.compiler;

import java.io.Serializable;

public class RuleIrVariable implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name;
    private Class<?> type;
    private String[] path;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setType(Class<?> type)
    {
        this.type = type;
    }


    public Class<?> getType()
    {
        return this.type;
    }


    public void setPath(String[] path)
    {
        this.path = path;
    }


    public String[] getPath()
    {
        return this.path;
    }
}
