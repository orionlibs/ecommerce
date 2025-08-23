package de.hybris.bootstrap.config.tenants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "param", propOrder = {"value"})
public class Param
{
    @XmlAttribute(required = true)
    protected String value;
    @XmlAttribute(required = true)
    protected String name;


    public Param()
    {
    }


    Param(String name, String value)
    {
        this.name = name;
        this.value = value;
    }


    public String getValue()
    {
        return this.value;
    }


    public String getName()
    {
        return this.name;
    }
}
