package de.hybris.platform.processengine.definition.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "script", propOrder = {"value"})
public class Script
{
    @XmlValue
    protected String value;
    @XmlAttribute(name = "type", required = true)
    protected String type;


    public String getValue()
    {
        return this.value;
    }


    public void setValue(String value)
    {
        this.value = value;
    }


    public String getType()
    {
        return this.type;
    }


    public void setType(String value)
    {
        this.type = value;
    }
}
