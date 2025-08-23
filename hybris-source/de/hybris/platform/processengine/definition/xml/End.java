package de.hybris.platform.processengine.definition.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "end", propOrder = {"value"})
public class End
{
    @XmlValue
    protected String value;
    @XmlAttribute(name = "state")
    protected EndState state;
    @XmlAttribute(name = "id", required = true)
    protected String id;


    public String getValue()
    {
        return this.value;
    }


    public void setValue(String value)
    {
        this.value = value;
    }


    public EndState getState()
    {
        return this.state;
    }


    public void setState(EndState value)
    {
        this.state = value;
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
