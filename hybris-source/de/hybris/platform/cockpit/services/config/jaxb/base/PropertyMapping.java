package de.hybris.platform.cockpit.services.config.jaxb.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "property-mapping")
public class PropertyMapping
{
    @XmlAttribute(required = true)
    protected String source;
    @XmlAttribute(required = true)
    protected String target;


    public String getSource()
    {
        return this.source;
    }


    public void setSource(String value)
    {
        this.source = value;
    }


    public String getTarget()
    {
        return this.target;
    }


    public void setTarget(String value)
    {
        this.target = value;
    }
}
