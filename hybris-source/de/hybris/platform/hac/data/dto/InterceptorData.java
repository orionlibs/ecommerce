package de.hybris.platform.hac.data.dto;

import java.util.Collection;

public class InterceptorData
{
    private final String name;
    private final String type;
    private final int order;
    private final Collection<String> replacedByInterceptors;


    public InterceptorData(String name, String type, int order, Collection<String> replacedByInterceptors)
    {
        this.name = name;
        this.type = type;
        this.order = order;
        this.replacedByInterceptors = replacedByInterceptors;
    }


    public String getName()
    {
        return this.name;
    }


    public String getType()
    {
        return this.type;
    }


    public int getOrder()
    {
        return this.order;
    }


    public Collection<String> getReplacedByInterceptors()
    {
        return this.replacedByInterceptors;
    }
}
