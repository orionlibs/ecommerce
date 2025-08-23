package de.hybris.platform.cockpit.services.config.jaxb.listview;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "group", propOrder = {"label", "propertyOrCustomOrGroup"})
public class Group
{
    protected List<Label> label;
    @XmlElements({@XmlElement(name = "custom", type = Custom.class), @XmlElement(name = "property", type = Property.class), @XmlElement(name = "group", type = Group.class)})
    protected List<Object> propertyOrCustomOrGroup;
    @XmlAttribute(required = true)
    protected String name;


    public List<Label> getLabel()
    {
        if(this.label == null)
        {
            this.label = new ArrayList<>();
        }
        return this.label;
    }


    public List<Object> getPropertyOrCustomOrGroup()
    {
        if(this.propertyOrCustomOrGroup == null)
        {
            this.propertyOrCustomOrGroup = new ArrayList();
        }
        return this.propertyOrCustomOrGroup;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }
}
