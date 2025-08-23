package de.hybris.platform.cockpit.services.config.jaxb.wizard;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wizard-property-list", propOrder = {"group", "property"})
public class WizardPropertyList
{
    @XmlElement(required = true)
    protected List<Property> property;
    @XmlElement(required = false)
    protected List<Group> group;


    public List<Property> getProperty()
    {
        if(this.property == null)
        {
            this.property = new ArrayList<>();
        }
        return this.property;
    }


    public List<Group> getGroups()
    {
        if(this.group == null)
        {
            this.group = new ArrayList<>();
        }
        return this.group;
    }
}
