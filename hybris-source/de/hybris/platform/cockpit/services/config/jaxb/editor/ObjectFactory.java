package de.hybris.platform.cockpit.services.config.jaxb.editor;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory
{
    public CustomGroup createCustomGroup()
    {
        return new CustomGroup();
    }


    public Editor createEditor()
    {
        return new Editor();
    }


    public Label createLabel()
    {
        return new Label();
    }


    public Parameter createParameter()
    {
        return new Parameter();
    }


    public Property createProperty()
    {
        return new Property();
    }


    public Group createGroup()
    {
        return new Group();
    }
}
