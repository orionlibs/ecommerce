package de.hybris.bootstrap.typesystem.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "indexKeyType")
public class IndexKeyType
{
    @XmlAttribute(required = true)
    protected String attribute;
    @XmlAttribute
    protected Boolean lower;


    public String getAttribute()
    {
        return this.attribute;
    }


    public void setAttribute(String value)
    {
        this.attribute = value;
    }


    public Boolean isLower()
    {
        return this.lower;
    }


    public void setLower(Boolean value)
    {
        this.lower = value;
    }
}
