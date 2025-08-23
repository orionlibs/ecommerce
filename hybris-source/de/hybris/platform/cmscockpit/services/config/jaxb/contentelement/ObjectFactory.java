package de.hybris.platform.cmscockpit.services.config.jaxb.contentelement;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory
{
    public ContentElements createContentElements()
    {
        return new ContentElements();
    }


    public ContentElement createContentElement()
    {
        return new ContentElement();
    }
}
