package de.hybris.platform.cockpit.services.config.jaxb.base;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory
{
    public PropertyMapping createPropertyMapping()
    {
        return new PropertyMapping();
    }


    public Label createLabel()
    {
        return new Label();
    }


    public Property createProperty()
    {
        return new Property();
    }


    public InitialProperties createInitialProperties()
    {
        return new InitialProperties();
    }


    public Search createSearch()
    {
        return new Search();
    }


    public DefaultProperty createDefaultProperty()
    {
        return new DefaultProperty();
    }


    public PropertyList createPropertyList()
    {
        return new PropertyList();
    }


    public DefaultPropertyList createDefaultPropertyList()
    {
        return new DefaultPropertyList();
    }


    public Parameter createParameter()
    {
        return new Parameter();
    }


    public Base createBase()
    {
        return new Base();
    }
}
