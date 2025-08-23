package de.hybris.bootstrap.typesystem.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "modelConstructorType")
public class ModelConstructorType
{
    @XmlAttribute(required = true)
    protected String signature;


    public String getSignature()
    {
        return this.signature;
    }


    public void setSignature(String value)
    {
        this.signature = value;
    }
}
