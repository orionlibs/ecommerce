package de.hybris.platform.cockpit.services.config.jaxb.base;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "default-property-list", propOrder = {"property"})
public class DefaultPropertyList
{
    @XmlElement(required = true)
    protected List<DefaultProperty> property;


    public List<DefaultProperty> getProperty()
    {
        if(this.property == null)
        {
            this.property = new ArrayList<>();
        }
        return this.property;
    }
}
