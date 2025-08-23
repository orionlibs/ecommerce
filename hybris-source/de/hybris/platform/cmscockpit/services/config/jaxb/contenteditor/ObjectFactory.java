package de.hybris.platform.cmscockpit.services.config.jaxb.contenteditor;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory
{
    public EditorList createEditorList()
    {
        return new EditorList();
    }


    public Property createProperty()
    {
        return new Property();
    }


    public ContentEditor createContentEditor()
    {
        return new ContentEditor();
    }


    public Parameter createParameter()
    {
        return new Parameter();
    }
}
