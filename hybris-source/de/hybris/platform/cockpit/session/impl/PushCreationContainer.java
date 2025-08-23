package de.hybris.platform.cockpit.session.impl;

import java.util.Map;

public class PushCreationContainer
{
    private String className = null;
    private Map<String, Object> parameters = null;


    public PushCreationContainer(String className, Map<String, Object> parameters)
    {
        this.className = className;
        this.parameters = parameters;
    }


    public String getClassName()
    {
        return this.className;
    }


    public void setClassName(String className)
    {
        this.className = className;
    }


    public Map<String, Object> getParameters()
    {
        return this.parameters;
    }


    public void setParameters(Map<String, Object> parameters)
    {
        this.parameters = parameters;
    }
}
