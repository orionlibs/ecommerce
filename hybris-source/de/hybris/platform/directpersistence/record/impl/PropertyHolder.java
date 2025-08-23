package de.hybris.platform.directpersistence.record.impl;

import java.text.MessageFormat;

public class PropertyHolder
{
    private final String name;
    private final Object value;


    public PropertyHolder(String name, Object value)
    {
        this.name = name;
        this.value = value;
    }


    public String getName()
    {
        return this.name;
    }


    public Object getValue()
    {
        return this.value;
    }


    public String toString()
    {
        return MessageFormat.format("(property: {0}, value: {1})", new Object[] {this.name, this.value});
    }
}
