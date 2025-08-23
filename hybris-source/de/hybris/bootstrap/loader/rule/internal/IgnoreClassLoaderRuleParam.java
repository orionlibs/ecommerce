package de.hybris.bootstrap.loader.rule.internal;

public class IgnoreClassLoaderRuleParam
{
    private final Integer order;
    private final String className;
    private String params;


    public IgnoreClassLoaderRuleParam(Integer order, String className)
    {
        this.order = order;
        this.className = className;
    }


    public void setParams(String params)
    {
        this.params = params;
    }


    public Integer getOrder()
    {
        return this.order;
    }


    public String getParams()
    {
        return this.params;
    }


    public String getClassName()
    {
        return this.className;
    }
}
