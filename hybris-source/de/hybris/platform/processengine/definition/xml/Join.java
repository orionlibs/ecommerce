package de.hybris.platform.processengine.definition.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "join")
public class Join
{
    @XmlAttribute(name = "then")
    protected String then;
    @XmlAttribute(name = "id", required = true)
    protected String id;


    public String getThen()
    {
        return this.then;
    }


    public void setThen(String value)
    {
        this.then = value;
    }


    public String getId()
    {
        return this.id;
    }


    public void setId(String value)
    {
        this.id = value;
    }
}
