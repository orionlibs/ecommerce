package de.hybris.platform.cockpit.services.config.jaxb.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "property")
public class Property
{
    @XmlAttribute(required = true)
    protected String qualifier;


    public String getQualifier()
    {
        return this.qualifier;
    }


    public void setQualifier(String value)
    {
        this.qualifier = value;
    }
}
