package de.hybris.platform.webservicescommons.mapping.impl;

import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;

@WsDTOMapping
public class DefaultClassMapper
{
    private final Class<?> source;
    private final Class<?> target;


    public DefaultClassMapper(Class<?> source, Class<?> target)
    {
        this.source = source;
        this.target = target;
    }


    public Class<?> getSource()
    {
        return this.source;
    }


    public Class<?> getTarget()
    {
        return this.target;
    }
}
