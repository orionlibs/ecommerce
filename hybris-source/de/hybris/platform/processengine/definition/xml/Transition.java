package de.hybris.platform.processengine.definition.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transition")
public class Transition
{
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "to", required = true)
    protected String to;


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }


    public String getTo()
    {
        return this.to;
    }


    public void setTo(String value)
    {
        this.to = value;
    }
}
