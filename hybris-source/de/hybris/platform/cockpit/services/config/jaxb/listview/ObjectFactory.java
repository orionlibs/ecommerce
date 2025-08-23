package de.hybris.platform.cockpit.services.config.jaxb.listview;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory
{
    public Label createLabel()
    {
        return new Label();
    }


    public ListView createListView()
    {
        return new ListView();
    }


    public Custom createCustom()
    {
        return new Custom();
    }


    public Parameter createParameter()
    {
        return new Parameter();
    }


    public Language createLanguage()
    {
        return new Language();
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
