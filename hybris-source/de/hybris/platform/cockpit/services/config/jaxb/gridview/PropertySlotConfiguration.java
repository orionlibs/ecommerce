package de.hybris.platform.cockpit.services.config.jaxb.gridview;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "propertySlotConfiguration", propOrder = {"property"})
@XmlSeeAlso({ImageSlotConfiguration.class})
public class PropertySlotConfiguration
{
    @XmlElement(required = true)
    protected Property property;


    public Property getProperty()
    {
        return this.property;
    }


    public void setProperty(Property value)
    {
        this.property = value;
    }
}
