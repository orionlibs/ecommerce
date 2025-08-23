package de.hybris.platform.processengine.definition.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "localizedmessage")
public class Localizedmessage
{
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "language", required = true)
    protected String language;


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }


    public String getLanguage()
    {
        return this.language;
    }


    public void setLanguage(String value)
    {
        this.language = value;
    }
}
