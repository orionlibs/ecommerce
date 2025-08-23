package de.hybris.bootstrap.beangenerator.definitions.model;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AttributeContainer<T extends AttributePrototype> extends ClassNameAware
{
    protected final Map<String, T> attributes = new LinkedHashMap<>();


    public AttributeContainer(String extensionName, String className)
    {
        super(extensionName, className);
    }


    public boolean hasAttribute(String name)
    {
        return this.attributes.containsKey(name);
    }


    public void addAttribute(T attribute)
    {
        if(this.attributes.containsKey(attribute.getName()))
        {
            throw new IllegalArgumentException("An attribute with the name " + attribute.getName() + " is already defined for the bean of type " + this.className);
        }
        this.attributes.put(attribute.getName(), attribute);
    }
}
