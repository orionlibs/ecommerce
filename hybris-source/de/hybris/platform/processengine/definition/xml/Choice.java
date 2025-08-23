package de.hybris.platform.processengine.definition.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "choice")
public class Choice
{
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "then", required = true)
    protected String then;


    public String getId()
    {
        return this.id;
    }


    public void setId(String value)
    {
        this.id = value;
    }


    public String getThen()
    {
        return this.then;
    }


    public void setThen(String value)
    {
        this.then = value;
    }
}
